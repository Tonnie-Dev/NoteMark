package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import timber.log.Timber

class DeleteNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(id: Long, queueDelete: Boolean = true): Boolean =
        when (val result = repository.deleteNote(id = id, queueDelete)) {

            is Resource.Success -> {
                Timber.tag("DeleteNoteUseCase").i("DUC - Success")
                result.data}

            is Resource.Error -> {

                Timber.tag("DeleteNoteUseCase").i("DUC - Error")
                throw result.exception
            }

            else -> false

        }

}
