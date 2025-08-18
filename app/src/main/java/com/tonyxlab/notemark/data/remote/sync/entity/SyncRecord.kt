package com.tonyxlab.notemark.data.remote.sync.entity

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "sync_record")
data class SyncRecord(
    val id: UUID,
    val userId: String,
    val noteId: String,
    val operation: SyncOperation,
    val payload: String,
    val timestamp: Long
)
enum class SyncOperation { CREATE, UPDATE, DELETE}