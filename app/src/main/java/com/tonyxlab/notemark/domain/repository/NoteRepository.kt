package com.tonyxlab.notemark.domain.repository

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<NoteItem>>

    suspend fun upsertNote(noteItem: NoteItem): Resource<Boolean>

    suspend fun getNoteById(id: Long) : Resource<NoteItem>

    suspend fun deleteNote(noteItem: NoteItem): Resource<Boolean>
}