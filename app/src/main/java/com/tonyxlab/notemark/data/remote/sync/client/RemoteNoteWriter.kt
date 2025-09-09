package com.tonyxlab.notemark.data.remote.sync.client

import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNoteDto
import io.ktor.client.statement.HttpResponse

interface RemoteNoteWriter {
    suspend fun create(token: String, email: String?, body: RemoteNoteDto): RemoteNoteDto
    suspend fun update(token: String, email: String?, body: RemoteNoteDto): RemoteNoteDto
    suspend fun delete(token: String, email: String?, remoteId: String): HttpResponse
    suspend fun getAll(token: String, email: String?): List<RemoteNoteDto>
}