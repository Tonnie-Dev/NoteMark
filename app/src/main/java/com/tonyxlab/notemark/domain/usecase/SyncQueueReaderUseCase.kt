package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository

class SyncQueueReaderUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(): Resource<Boolean> = runCatching {
        repository.isSyncQueueEmpty()
        Resource.Success(true)
    }.getOrElse { exception -> Resource.Error(exception = Exception(exception.message)) }
}