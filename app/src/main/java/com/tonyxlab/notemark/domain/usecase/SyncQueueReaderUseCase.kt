package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.domain.repository.NoteRepository

class SyncQueueReaderUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isSyncQueueEmpty()
    }
}


