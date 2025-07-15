package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.exception.NoteNotFoundException
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(noteItem: NoteItem) {
        when (val result = repository.deleteNote(noteItem)) {
            is Resource.Success -> {
                val deleted = result.data
                if (!deleted) throw Exception("Note deletion failed or note not found.")
           true
            }

            is Resource.Error -> throw result.exception

            else -> throw NoteNotFoundException(noteItem.id)
        }
    }
}
