@file:RequiresApi(Build.VERSION_CODES.O)
package com.tonyxlab.notemark.data.remote.sync.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity


interface NotesRemote {
    suspend fun create(body: RemoteNote): RemoteNote
    suspend fun update(body: RemoteNote): RemoteNote
    suspend fun delete(remoteId: String)
    suspend fun getAll(): List<RemoteNote>   // returns full snapshot
}

data class RemoteNote(
    val id: String, val title: String, val content: String,
    val createdAt: String, val lastEditedAt: String
)

fun Long.toIso(): String = java.time.Instant.ofEpochMilli(this).toString()

fun String.isoToMillis(): Long = java.time.Instant.parse(this).toEpochMilli()

fun NoteEntity.toRemote(): RemoteNote = RemoteNote(
        id = remoteId ?: java.util.UUID.randomUUID().toString(),
        title = title, content = content,
        createdAt = createdOn.toIso(), lastEditedAt = lastEditedOn.toIso()
)
fun RemoteNote.toEntity(): NoteEntity = NoteEntity(
        id = 0L, remoteId = id, title = title, content = content,
        createdOn = createdAt.isoToMillis(), lastEditedOn = lastEditedAt.isoToMillis()
)




/*
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
}*/
