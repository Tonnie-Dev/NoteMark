package com.tonyxlab.notemark.data.remote.sync.dto

import androidx.datastore.dataStore

data class UploadItem(
    val localId: String,          // SyncRecord.id as String
    val operation: String,        // "CREATE" | "UPDATE" | "DELETE"
    val payload: String,          // JSON snapshot (NoteEntity)
    val clientTimestamp: Long
)

data class UploadRequest(
    val userId: String,
    val items: List<UploadItem>
)

data class UploadResultItem(
    val localId: String,          // echoed back for mapping
    val status: String,           // "OK" | "FAILED"
    val message: String? = null
)

data class UploadResponse(val items: List<UploadResultItem>)


data class NoteDeltaDto(         // server -> client
    val id: String,
    val title: String,
    val content: String,
    val updatedAt: Long,
    val deleted: Boolean
)

data class DownloadResponse(
    val items: List<NoteDeltaDto>,
    val nextToken: String?
)

// A very thin abstraction; implement with Ktor/Retrofit as you prefer.
interface SyncRemote {
    suspend fun upload(accessToken: String, body: UploadRequest): UploadResponse
    suspend fun download(accessToken: String, deltaToken: String?): DownloadResponse
}