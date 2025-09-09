package com.tonyxlab.notemark.data.repository

import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.dao.SyncDao
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.data.remote.sync.entity.SyncRecord
import com.tonyxlab.notemark.domain.json.JsonSerializer
import java.util.UUID

class LocalNoteWriter(
    private val noteDao: NoteDao,
    private val syncDao: SyncDao,
    private val jsonSerializer: JsonSerializer,
    private val dataStore: DataStore,
    private val clock: () -> Long = { System.currentTimeMillis() }
) {

    suspend fun upsert(
        noteEntity: NoteEntity,
        queueSync: Boolean = true
    ): Long {

        val id = noteDao.upsert(noteEntity)
        val noteWithId = noteEntity.copy(id = id)

        if (queueSync) {
            val syncOp = if (noteWithId.remoteId.isNullOrBlank())
                SyncOperation.CREATE
            else
                SyncOperation.UPDATE

            queue(
                    localNoteId = id,
                    noteEntity = noteWithId,
                    syncOp = syncOp
            )
        }
        return id
    }





    suspend fun softDelete(localId: Long, queueDelete: Boolean = true) {

        val note = noteDao.getNoteById(localId) ?: return
        val now = clock()

        // Persist tombstone locally - Dead or Deleted Note
        val tombstone = note.copy(isDeleted = true, lastEditedOn = now)
        noteDao.upsert(tombstone)

        if (queueDelete) {

            queueDelete(localId = localId, tombstone = tombstone)
        }
    }


    suspend fun hardDelete(localId: Long, queueDelete: Boolean = true): Boolean {

        val latestLocalNoteSnapshot =
            noteDao.getNoteById(id = localId) ?: return false

        val hasRemoteId = latestLocalNoteSnapshot.remoteId.isNullOrBlank()
                .not()

        if (queueDelete && hasRemoteId) {
            queue(
                    localNoteId = latestLocalNoteSnapshot.id,
                    noteEntity = latestLocalNoteSnapshot,
                    syncOp = SyncOperation.DELETE
            )
        }

        val rowsDeleted = noteDao.deleteById(latestLocalNoteSnapshot.id)
        return rowsDeleted > 0
    }

    private suspend fun queueDelete(localId: Long, tombstone: NoteEntity) {
        // If your API exposes a DELETE op, use SyncOperation.DELETE.
        // If it doesn’t, send UPDATE with isDeleted=true (server treats as delete).
        val op = if (tombstone.remoteId.isNullOrBlank())
            SyncOperation.UPDATE   // no remote id yet → just send full payload (server decides)
        else
            SyncOperation.DELETE   // or UPDATE if your API expects soft delete via update

        // Serialize the full tombstoned note; remote will see isDeleted=true + lastEditedOn
        val payloadJson = jsonSerializer.toJson(
                serializer = NoteEntity.serializer(),
                data = tombstone
        )



        val record = SyncRecord(
                id = UUID.randomUUID()
                        .toString(),
                userId = dataStore.getOrCreateInternalUserId(),
                noteId = localId.toString(),
                operation = SyncOperation.DELETE, // -> prefer UPDATED for tombstones
                payload = payloadJson,
                timestamp = tombstone.lastEditedOn
        )
        syncDao.upsertLatest(record)
    }

        private suspend fun queue(
            localNoteId: Long,
            noteEntity: NoteEntity,
            syncOp: SyncOperation
        ) {

            val payloadJsonSnapshot = jsonSerializer
                    .toJson(
                            serializer = NoteEntity.serializer(),
                            data = noteEntity
                    )

            val userId = dataStore.getOrCreateInternalUserId()

            val record = SyncRecord(
                    id = UUID.randomUUID()
                            .toString(),
                    userId = userId,
                    noteId = localNoteId.toString(),
                    operation = syncOp,
                    payload = payloadJsonSnapshot, // JSON snapshot at time of change
                    timestamp = noteEntity.lastEditedOn
            )
            syncDao.upsertLatest(record)
        }
}
