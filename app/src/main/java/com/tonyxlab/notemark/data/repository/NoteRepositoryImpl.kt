@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.mappers.toEntity
import com.tonyxlab.notemark.data.local.database.mappers.toModel
import com.tonyxlab.notemark.domain.exception.NoteNotFoundException
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.util.safeIoCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val localWriter: NoteLocalWriter
) : NoteRepository {
    override fun getAllNotes(): Flow<List<NoteItem>> {
        return dao.getAllNotes()
                .filterNotNull()
                .map { notes -> notes.map { it.toModel() } }
    }

    override suspend fun upsertNote(noteItem: NoteItem, queueCreate: Boolean): Resource<Long> =
        safeIoCall { localWriter.upsert(
                noteEntity = noteItem.toEntity(),
                queueCreate = queueCreate
        ) }

    override suspend fun getNoteById(id: Long): Resource<NoteItem> =

        safeIoCall {
            val note = dao.getNoteById(id)
            note?.toModel() ?: throw NoteNotFoundException(id)
        }

    override suspend fun deleteNote(noteItem: NoteItem, queueDelete: Boolean): Resource<Boolean> =

        safeIoCall {
          localWriter.delete(
                  noteEntity = noteItem.toEntity(),
                  queueDelete = queueDelete
          )
        }

    override suspend fun clearAllNotes(): Resource<Boolean> = safeIoCall {
        dao.clearAllNotes()
        true
    }

}
