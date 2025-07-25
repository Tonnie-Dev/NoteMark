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

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<NoteItem>> {
        return dao.getAllNotes()
                .filterNotNull()
                .map { notes -> notes.map { it.toModel() } }
    }

    override suspend fun upsertNote(noteItem: NoteItem): Resource<Long> =
        safeIoCall { dao.insertAndReturnId(noteItem.toEntity()) }

    override suspend fun getNoteById(id: Long): Resource<NoteItem> =

        safeIoCall {
            val note = dao.getNoteById(id)
            note?.toModel() ?: throw NoteNotFoundException(id)
        }

    override suspend fun deleteNote(noteItem: NoteItem): Resource<Boolean> =

        safeIoCall {
            val result = dao.delete(noteItem.toEntity())
            result > 0
        }

    override suspend fun clearAllNotes(): Resource<Boolean> = safeIoCall {
        dao.clearAllNotes()
        true
    }

}
