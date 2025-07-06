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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllNotes(): Flow<List<NoteItem>> {
        return dao.getAllNotes()
                .map { notes -> notes?.map { it.toModel() } ?: emptyList() }
    }

    override suspend fun upsertNote(noteItem: NoteItem): Resource<Boolean> =

        withContext(Dispatchers.IO) {

            try {
                dao.upsert(noteItem.toEntity())
                Resource.Success(true)
            } catch (e: Exception) {

                Resource.Error(e)
            }
        }

    override suspend fun getNoteById(id: Long): Resource<NoteItem> =
        withContext(Dispatchers.IO) {

            try {
                val note = dao.getNoteById(id)

                if (note != null) {
                    Resource.Success(note.toModel())
                } else {
                    Resource.Error(exception = NoteNotFoundException(id))
                }
            } catch (e: Exception) {

                Resource.Error(exception = e)
            }
        }


    override suspend fun deleteNote(noteItem: NoteItem): Resource<Boolean> =
        withContext(Dispatchers.IO) {

            try {
                val result = dao.delete(noteItem.toEntity())
                Resource.Success(result > 0)
            } catch (e: Exception) {

                Resource.Error(exception = e)
            }
        }
}
