package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository

class LogoutUseCase(
    private val noteRepository: NoteRepository,
    private val authRepository: AuthRepository,
    private val dataStore: DataStore
) {
    suspend operator fun invoke(preserveSyncQueue: Boolean): Resource<Boolean> {

        return runCatching {

            dataStore.getRefreshToken()
                    ?.let { refreshToken -> authRepository.logout(refreshToken) }

            when (val result = noteRepository.clearAllNotes()) {
                is Resource.Success -> Unit
                is Resource.Error -> return Resource.Error(result.exception)
                else -> Unit
            }

            if (!preserveSyncQueue) {
                when (val result = noteRepository.clearSyncQueue()) {
                    is Resource.Success -> Unit
                    is Resource.Error -> return Resource.Error(exception = result.exception)
                    else -> Unit
                }
            }

            dataStore.clearTokens()

            Resource.Success(true)

        }.getOrElse { exception -> Resource.Error(exception = Exception(exception.message)) }
    }

}