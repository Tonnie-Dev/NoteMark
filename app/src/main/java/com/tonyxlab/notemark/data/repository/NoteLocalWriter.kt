package com.tonyxlab.notemark.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.dao.SyncDao
import com.tonyxlab.notemark.data.local.database.entity.NoteEntity
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNoteDto
import com.tonyxlab.notemark.data.remote.sync.dto.toEntity
import com.tonyxlab.notemark.data.remote.sync.entity.SyncOperation
import com.tonyxlab.notemark.data.remote.sync.entity.SyncRecord
import com.tonyxlab.notemark.domain.json.JsonSerializer
import java.util.UUID

class NoteLocalWriter(
    private val noteDao: NoteDao,
    private val syncDao: SyncDao,
    private val jsonSerializer: JsonSerializer,
    private val dataStore: DataStore,
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

    suspend fun delete(id: Long, queueDelete: Boolean = true): Boolean {

        val latestLocalNoteSnapshot =
            noteDao.getNoteById(id = id) ?: return false

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
/*
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun applyRemoteNoteLww(dto: RemoteNoteDto) {
        // Map remote → local entity fields (including remote.id and lastEditedAtEpochMs)
        val remoteEntity = dto.toEntity()

        // Find by remoteId first
        val localId = dto.id.let { noteDao.findIdByRemoteId(it) }
        val local = localId?.let { noteDao.getNoteById(it) }

        // If remote is a tombstone (deleted), treat it as a note with isDeleted=true
        if (dto.isDeleted == true) {
            if (local == null) return // nothing to do
            val newerWins = (dto.lastEditedAtEpochMs ?: -1L) > (local.lastEditedAtEpochMs ?: 0L)
            if (newerWins) {
                noteDao.deleteById(local.id)
                syncDao.deleteByNoteId(local.id.toString()) // drop any pending local ops
            }
            return
        }

        if (local == null) {
            // New to us, insert as-is
            noteDao.upsert(remoteEntity)
            return
        }

        val serverNewer =
            (remoteEntity.lastEditedAtEpochMs ?: -1L) > (local.lastEditedAtEpochMs ?: 0L)

        if (serverNewer) {
            // Server wins: overwrite local and drop pending local ops for this note
            noteDao.upsert(remoteEntity.copy(id = local.id))
            syncDao.deleteByNoteId(local.id.toString())
        } else {
            // Local wins: ensure there’s an UPDATE enqueued (if not already)
            // (Optional if you always enqueue on user edit)
        }
    }*/

}
