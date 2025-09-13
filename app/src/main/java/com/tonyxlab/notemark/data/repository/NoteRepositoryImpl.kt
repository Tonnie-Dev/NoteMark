@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.dao.SyncDao
import com.tonyxlab.notemark.data.local.database.mappers.toEntity
import com.tonyxlab.notemark.data.local.database.mappers.toModel
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.domain.exception.NoteNotFoundException
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.util.safeIoCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val dataStore: DataStore,
    private val syncDao: SyncDao,
    private val localWriter: LocalNoteWriter
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteItem>> {
        return noteDao.getNotes()
                .filterNotNull()
                .map { notes -> notes.map { it.toModel() } }
    }

    override suspend fun upsertNote(noteItem: NoteItem, queueSync: Boolean): Resource<Long> =
        safeIoCall {
            localWriter.upsert(
                    noteEntity = noteItem.toEntity(),
                    queueSync = queueSync
            )
        }

    override suspend fun getNoteById(id: Long): Resource<NoteItem> =
        safeIoCall {
            val note = noteDao.getNoteById(id)
            note?.toModel() ?: throw NoteNotFoundException(id)
        }

    override suspend fun isSyncQueueEmpty(): Boolean {
        return dataStore.getInternalUserId()
                ?.let {
                    syncDao.isSyncQueueEmpty(it)
                } == true
    }

    override suspend fun clearAllNotes(): Resource<Boolean> = safeIoCall {
        noteDao.clearAllNotes()
        true
    }

    override suspend fun clearSyncQueue(): Resource<Boolean> = safeIoCall {
        syncDao.clearSyncQueue()
        true
    }

    override suspend fun deleteNote(
        id: Long,
        queueDelete: Boolean
    ): Resource<Boolean> = safeIoCall {

        localWriter.hardDelete(
                localId = id,
                queueDelete = queueDelete
        )

        true
/*
        if (hardDelete) {

        } else {

            localWriter.softDelete(
                    localId = id,
                    queueDelete = queueDelete
            )
            true

        }
*/
    }
}
