package com.tonyxlab.notemark.data.remote.sync.client

import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNote
import io.ktor.client.statement.HttpResponse

interface NotesRemote {
    suspend fun create(token: String, email: String?, body: RemoteNote): RemoteNote
    suspend fun update(token: String, email: String?, body: RemoteNote): RemoteNote
    suspend fun delete(token: String, email: String?, remoteId: String): HttpResponse
    suspend fun getAll(token: String, email: String?): List<RemoteNote>
}