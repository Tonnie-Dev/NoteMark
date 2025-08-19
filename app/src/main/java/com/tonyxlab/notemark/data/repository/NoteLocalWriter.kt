package com.tonyxlab.notemark.data.repository

import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.remote.sync.dao.SyncDao
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.data.remote.sync.entity.SyncRecord
import com.tonyxlab.notemark.domain.json.JsonSerializer
import java.util.UUID

class NoteLocalWriter(
    private val noteDao: NoteDao,
    private val syncDao: SyncDao,
    private val jsonSerializer: JsonSerializer,
    private val dataStore: DataStore,
    private val clock: () -> Long = { System.currentTimeMillis() }
) {

    suspend fun upsert(noteEntity: NoteEntity): Long {
        val exists = noteEntity.id != 0L && noteDao.getNoteById(noteEntity.id) != null

        val syncOp = if (exists) SyncOperation.UPDATE else SyncOperation.CREATE

        val id = noteDao.upsert(noteEntity)

        queue(
                localNoteId = id,
                noteEntity = noteEntity.copy(id = id),
                syncOp = syncOp
        )

        return id
    }

    suspend fun delete(noteEntity: NoteEntity): Boolean {
        val rowsDeleted = noteDao.delete(noteEntity)

        if (rowsDeleted > 0) queue(
                localNoteId = noteEntity.id,
                noteEntity = noteEntity,
                syncOp = SyncOperation.DELETE
        )
        return rowsDeleted > 0
    }

    private suspend fun queue(localNoteId: Long, noteEntity: NoteEntity, syncOp: SyncOperation) {

        val payloadJsonSnapshot = jsonSerializer
                .toJson(serializer = NoteEntity.serializer(), data = noteEntity)

        val userId = dataStore.getOrCreateInternalUserId()

        val record = SyncRecord(
                id = UUID.randomUUID().toString(),
                userId = userId,
                noteId = localNoteId.toString(),
                operation = syncOp,
                payload = payloadJsonSnapshot, // JSON snapshot at time of change
                timestamp = clock()
        )
        syncDao.insert(record)
    }
}
