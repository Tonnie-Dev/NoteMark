package com.tonyxlab.notemark.data.remote.sync.client


import com.tonyxlab.notemark.BuildConfig
import com.tonyxlab.notemark.data.remote.sync.dto.NotesPage
import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNote
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.util.ApiEndpoints
import com.tonyxlab.notemark.util.Constants.EMAIL_HEADER_KEY
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.bearerAuth
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

private const val EMAIL_HEADER_KEY = "X-User-Email"

class NotesRemoteKtor(
    private val client: HttpClient,
    private val baseUrl: String,
    private val tokenProvider: suspend () -> String?,   // DataStore::getAccessToken
    private val emailProvider: (suspend () -> String?)? = null  // DataStore::getUsername or BuildConfig
) : NotesRemote {

    override suspend fun create(body: RemoteNote): RemoteNote = withHeaders { token, email ->
        client.post("$baseUrl/api/notes") {
            if (!token.isNullOrBlank()) bearerAuth(token)
            if (email != null) header(EMAIL_HEADER_KEY, email)
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun update(body: RemoteNote): RemoteNote = withHeaders { token, email ->
        client.put("$baseUrl/api/notes") {
            if (!token.isNullOrBlank()) bearerAuth(token)
            if (email != null) header(EMAIL_HEADER_KEY, email)
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun delete(remoteId: String): Unit = withHeaders { token, email ->
        client.delete("$baseUrl/api/notes/$remoteId") {
            if (!token.isNullOrBlank()) bearerAuth(token)
            if (email != null) header(EMAIL_HEADER_KEY, email)
        }
    }

    override suspend fun getAll(): List<RemoteNote> = withHeaders { token, email ->
        // Try single-shot page=-1
        val first: NotesPage = client.get("$baseUrl/api/notes") {
            if (!token.isNullOrBlank()) bearerAuth(token)
            if (email != null) header(EMAIL_HEADER_KEY, email)
            parameter("page", -1)
        }.body()

        if (first.total <= first.notes.size) first.notes else {
            val all = first.notes.toMutableList()
            var page = 0
            val size = 50
            while (all.size < first.total) {
                val p: NotesPage = client.get("$baseUrl/api/notes") {
                    if (!token.isNullOrBlank()) bearerAuth(token)
                    if (email != null) header(EMAIL_HEADER_KEY, email)
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

    override suspend fun ping(): Boolean = try {
        client.get("$baseUrl/health")
        Timber.tag("NotesRemoteKtor").i("Health OK: $baseUrl")
        true
    } catch (e: Exception) {
        Timber.tag("NotesRemoteKtor").i("Ping failed: $baseUrl -> $e")
        false
    }

    /**
     * Obtain fresh token/email for this call; map 5xx/timeout to IOException so WM retries.
     * IMPORTANT: We pass token/email into the request builder itself; no separate client.request().
     */
    private suspend inline fun <T> withHeaders(
        crossinline call: suspend (token: String?, email: String?) -> T
    ): T {
        val token = tokenProvider() ?: throw IllegalStateException("Missing access token")
        val email = emailProvider?.invoke()
        return try {
            call(token, email)
        } catch (e: HttpRequestTimeoutException) {
            throw java.io.IOException(e)
        } catch (e: ResponseException) {
            if (e.response.status.value >= 500) throw java.io.IOException(e) else throw e
        }
    }
}