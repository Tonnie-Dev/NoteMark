package com.tonyxlab.notemark.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.data.remote.sync.entity.SyncRecord

@Dao
interface SyncDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(syncRecord: SyncRecord)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(syncRecords: List<SyncRecord>)

    @Query(
            """
            SELECT * FROM sync_record 
            WHERE userId = :userId
            ORDER BY timestamp
            LIMIT :limit
            """
    )
    suspend fun loadBatch(userId: String, limit: Int): List<SyncRecord>

    @Query(
            """
        SELECT NOT EXISTS(
            SELECT 1 FROM sync_record
            WHERE userId = :userId
            LIMIT 1
        )
    """
    )
    suspend fun isSyncQueueEmpty(userId: String): Boolean

    @Query("SELECT COUNT(*) FROM sync_record WHERE userId =:userId")
    suspend fun countForUser(userId: String): Int

    @Query("SELECT * FROM sync_record WHERE noteId = :noteId LIMIT 1")
    suspend fun findByNoteId(noteId: String): SyncRecord?

    @Query(
            """
UPDATE sync_record 
SET payload = :payload, timestamp = :timestamp, operation = :operation
WHERE id = :id
"""
    )
    suspend fun updateExisting(
        id: String,
        payload: String,
        operation: SyncOperation,
        timestamp: Long
    )

    @Query("DELETE FROM sync_record")
    suspend fun clearSyncQueue()

    @Query("DELETE FROM sync_record WHERE id =:id")
    suspend fun deleteByRecordId(id: String)

    @Query("DELETE FROM sync_record WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    suspend fun upsertLatest(record: SyncRecord) {
        val existing = findByNoteId(record.noteId)
        if (existing == null || existing.operation == SyncOperation.DELETE) {
            insert(record)
        } else {
            updateExisting(
                    id = existing.id,
                    payload = record.payload,
                    operation = record.operation,  // allow CREATE→UPDATE promotion
                    timestamp = record.timestamp
            )
        }
    }

}