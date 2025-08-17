package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.util.safeIoCall

class LogOutUseCase(
    private val noteRepository: NoteRepository,
    private val authRepository: AuthRepository,
    private val dataStore: DataStore
) {
    suspend operator fun invoke(): Resource<Boolean> = safeIoCall {
        val refreshToken = dataStore.getRefreshToken()

        if (refreshToken != null) {
            val logoutResult = authRepository.logout(refreshToken)
            if (logoutResult !is Resource.Success) {
                return@safeIoCall false
            }
        }

        val dbClearResult = noteRepository.clearAllNotes()

        if (dbClearResult !is Resource.Success) {
            return@safeIoCall false
        }
        dataStore.clearTokens()
        true
    }
}