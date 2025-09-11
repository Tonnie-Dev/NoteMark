package com.tonyxlab.notemark.data.remote.sync.client

import com.tonyxlab.notemark.data.remote.sync.dto.NotesPage
import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNoteDto
import com.tonyxlab.notemark.util.Constants.EMAIL_HEADER_KEY
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.isSuccess

private const val EMAIL_HEADER_KEY = "X-User-Email"

class RemoteNoteWriterImpl(
    private val client: HttpClient,
    private val baseUrl: String,
        // you can still keep the provider-based ctor if you want both paths available
    private val tokenProvider: (suspend () -> String?)? = null,
    private val emailProvider: (suspend () -> String?)? = null,
) : RemoteNoteWriter {

    // ---------------- Worker-passed-token overloads (the robust path) ----------------
    override suspend fun create(token: String, email: String?, body: RemoteNoteDto): RemoteNoteDto =
        mapNetworkErrors {
            client.post("$baseUrl/api/notes") {
                attachAuth(token, email)
                contentType(ContentType.Application.Json)
                setBody(body)
            }
                    .body()
        }

    override suspend fun update(token: String, email: String?, body: RemoteNoteDto): RemoteNoteDto =
        mapNetworkErrors {
            val response = client.put("$baseUrl/api/notes") {
                attachAuth(token, email)
                contentType(ContentType.Application.Json)
                setBody(body)
            }

            // If backend returns 204 or no payload, just return what we sent (or re-fetch if you prefer)
            if (response.status.value == 204 || response.contentLength() == 0L) {
                return@mapNetworkErrors body
            }

            // Some servers reply 200 with text/plain or empty JSON – guard that too
            val text = response.bodyAsText()
            if (text.isBlank()) return@mapNetworkErrors body

            // Otherwise parse as JSON RemoteNote
            // (body<T>() will work because ContentNegotiation is installed)
            response.call.body()
        }

    override suspend fun delete(token: String, email: String?, remoteId: String) =
        mapNetworkErrors {
            client.delete("$baseUrl/api/notes/$remoteId") {
                attachAuth(token, email)
            }
        }

    // RemoteNoteWriterImpl.kt
    override suspend fun getAll(token: String, email: String?): List<RemoteNoteDto> =
        mapNetworkErrors {
            val response = client.get("$baseUrl/api/notes") {
                attachAuth(token, email)
                // Avoid page=-1 unless your backend supports it
                parameter("page", 0)
                parameter("size", 50)
                expectSuccess = false
            }

            when (response.status.value) {
                401 -> throw AuthException("Unauthorized (401) when fetching notes")
            }

            if (!response.status.isSuccess()) {
                val text = response.bodyAsText() // helpful for server error messages
                throw ResponseException(response, "Unexpected ${response.status}: $text")
            }

            val notePage: NotesPage = response.body()

            if (notePage.noteCount <= notePage.notes.size)
                notePage.notes
            else {
                val noteList = notePage.notes.toMutableList()
                var page = 1
                val size = 50
                while (noteList.size < notePage.noteCount) {
                    val pResp = client.get("$baseUrl/api/notes") {
                        attachAuth(token, email)
                        parameter("page", page)
                        parameter("size", size)
                        expectSuccess = false
                    }
                    if (!pResp.status.isSuccess()) break
                    val p: NotesPage = pResp.body()
                    if (p.notes.isEmpty()) break
                    noteList += p.notes
                    page++
                }
                noteList
            }
        }

    class AuthException(msg: String) : Exception(msg)


    // Map 5xx/timeout to IOException so WM can RETRY; 4xx bubble up.
    private suspend inline fun <T> mapNetworkErrors(crossinline block: suspend () -> T): T =
        try {
            block()
        } catch (e: HttpRequestTimeoutException) {
            throw java.io.IOException(e)
        } catch (e: ResponseException) {
            if (e.response.status.value >= 500) throw java.io.IOException(e) else throw e
        }
}

fun HttpRequestBuilder.attachAuth(token: String?, email: String?) {
    if (!token.isNullOrBlank()) bearerAuth(token)
    if (!email.isNullOrBlank()) header(EMAIL_HEADER_KEY, email)
}


