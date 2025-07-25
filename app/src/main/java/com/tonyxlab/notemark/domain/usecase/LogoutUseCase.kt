package com.tonyxlab.notemark.domain.usecase

import com.tonyxlab.notemark.data.local.datastore.TokenStorage
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogoutUseCase(
    private val noteRepository: NoteRepository,
    private val authRepository: AuthRepository
) {
/*    suspend operator fun invoke(): Boolean{
        return try {


            TokenStorage.getRefreshToken()?.let {

                val logoutResult =    authRepository.logout(refreshToken = it)

                if (logoutResult !is Resource.Success){
                    return false
                }
            }

            val dbClearResult = noteRepository.clearAllNotes()

            if (dbClearResult !is Resource.Success){
                return false
            }
            TokenStorage.clearTokens()
        true
        }catch (e: Exception){
           throw e
        }

    }*/
suspend operator fun invoke(): Resource<Boolean> = safeIoCall {
    val refreshToken = TokenStorage.getRefreshToken()

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

    TokenStorage.clearTokens()
    true
}

    suspend fun <T> safeIoCall(block: suspend () -> T): Resource<T> {
        return try {
            withContext(Dispatchers.IO) {
                Resource.Success(block())
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }

    }

}