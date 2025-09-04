package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository

class UpsertNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(noteItem: NoteItem, queueSync: Boolean = true): Long {
        return when (val result = repository.upsertNote(noteItem, queueSync)
        ) {
            is Resource.Success -> result.data
            is Resource.Error -> throw result.exception
            else -> throw Exception("Unknow error while upserting note")
        }
    }
}