package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(
        id: Long, queueDelete: Boolean = true,

        ): Boolean =
        when (val result =
            repository.deleteNote(id = id, queueDelete = queueDelete)) {

            is Resource.Success -> {
                result.data
            }

            is Resource.Error -> {

                throw result.exception
            }

            else -> false

        }
}
