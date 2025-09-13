@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.data.remote.sync.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import kotlinx.serialization.Serializable

fun Long.toIso(): String = java.time.Instant.ofEpochMilli(this)
        .toString()

fun String.isoToMillis(): Long = java.time.Instant.parse(this)
        .toEpochMilli()

fun NoteEntity.toRemoteDto(): RemoteNoteDto = RemoteNoteDto(
        id = remoteId ?: java.util.UUID.randomUUID()
                .toString(),
        title = title,
        content = content,
        createdAt = createdOn.toIso(),
        lastEditedAt = lastEditedOn.toIso(),
)

fun RemoteNoteDto.toEntity(): NoteEntity = NoteEntity(
        id = 0L,
        remoteId = id,
        title = title,
        content = content,
        createdOn = createdAt.isoToMillis(),
        lastEditedOn = lastEditedAt.isoToMillis(),
)

@Serializable
data class RemoteNoteDto(
    val id: String,            // server UUID
    val title: String,
    val content: String,
    val createdAt: String,     // ISO 8601 (e.g., 2025-08-19T07:00:00Z)
    val lastEditedAt: String,
    val isDeleted: Boolean = false
)

@Serializable
data class NotesPage(
    val notes: List<RemoteNoteDto>,
    val noteCount: Int =0
)



