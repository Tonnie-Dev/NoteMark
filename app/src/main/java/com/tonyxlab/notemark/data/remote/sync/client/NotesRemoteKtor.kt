package com.tonyxlab.notemark.data.remote.sync.client


import com.tonyxlab.notemark.data.remote.sync.dto.NotesPage
import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class NotesRemoteKtor(
    private val client: HttpClient,
    private val baseUrl: String,                         // e.g., BuildConfig.API_BASE_URL
    private val tokenProvider: suspend () -> String?     // from DataStore
) : NotesRemote {

    override suspend fun create(body: RemoteNote): RemoteNote = requestWithAuth {
        client.post("$baseUrl/api/notes") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun update(body: RemoteNote): RemoteNote = requestWithAuth {
        client.put("$baseUrl/api/notes") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun delete(remoteId: String) {
        requestWithAuth {
            client.delete("$baseUrl/api/notes/$remoteId")
        }
    }

    override suspend fun getAll(): List<RemoteNote> = requestWithAuth {
        // Try single-shot page=-1 first
        val first: NotesPage = client.get("$baseUrl/api/notes") {
            parameter("page", -1)
        }.body()

        if (first.total <= first.notes.size) {
            first.notes
        } else {
            // Fallback: page through
            val all = first.notes.toMutableList()
            var page = 0
            val size = 50 // pick a sensible page size
            while (all.size < first.total) {
                val p: NotesPage = client.get("$baseUrl/api/notes") {
                    parameter("page", page)
                    parameter("size", size)
                }.body()
                if (p.notes.isEmpty()) break
                all += p.notes
                page++
            }
            all
        }
    }

    /**
     * Wrap each call to attach Authorization and normalize transient errors.
     * - Adds Bearer token per request (fresh from DataStore).
     * - Rethrows 5xx/timeout as IOException so WorkManager will RETRY.
     * - 4xx bubble up as-is -> Worker will FAIL (good, usually caller/auth issue).
     */
    private suspend fun <T> requestWithAuth(block: suspend () -> T): T {
        val token = tokenProvider() ?: throw IllegalStateException("Missing access token")
        return try {
            client.request {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            block()
        } catch (e: HttpRequestTimeoutException) {
            // Map to IOException for WM retry policy
            throw java.io.IOException(e)
        } catch (e: ResponseException) {
            // Ktor wraps 4xx/5xx as ResponseException. Retry 5xx; fail 4xx.
            val status = e.response.status.value
            if (status >= 500) throw java.io.IOException(e) else throw e
        }
    }
}
