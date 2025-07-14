package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(noteItem: NoteItem): Resource<Boolean> {
        return repository.deleteNote(noteItem)
    }
}

