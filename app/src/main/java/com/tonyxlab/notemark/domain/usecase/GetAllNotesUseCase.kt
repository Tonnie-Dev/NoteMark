package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(private val repository: NoteRepository) {

    operator fun invoke(): Flow<List<NoteItem>> {

        return repository.getAllNotes()
    }
}