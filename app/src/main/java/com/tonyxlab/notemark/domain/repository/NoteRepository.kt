package com.tonyxlab.notemark.domain.repository

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<NoteItem>>

    suspend fun upsertNote(noteItem: NoteItem, queueCreate: Boolean): Resource<Long>

    suspend fun getNoteById(id: Long) : Resource<NoteItem>

    suspend fun isSyncQueueEmpty(): Resource<Boolean>

    suspend fun deleteNote(noteItem: NoteItem, queueDelete: Boolean): Resource<Boolean>

    suspend fun clearAllNotes(): Resource<Boolean>

    suspend fun clearSyncQueue(): Resource<Boolean>
}