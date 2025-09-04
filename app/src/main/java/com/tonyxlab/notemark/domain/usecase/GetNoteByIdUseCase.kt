package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.exception.NoteNotFoundException
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import timber.log.Timber

class GetNoteByIdUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(id: Long): NoteItem {

        return repository.getNoteById(id)
                .let { result ->
                    when (result) {
                        is Resource.Success -> result.data
                        is Resource.Error -> throw result.exception
                        else -> throw NoteNotFoundException(id = id)
                    }
                }
    }
}
