package com.tonyxlab.notemark.domain.auth

import com.tonyxlab.notemark.domain.model.Credentials
import com.tonyxlab.notemark.domain.model.Resource

interface AuthRepository {


    suspend fun register(credentials: Credentials): Resource<Int>
    suspend fun login(credentials: Credentials): Resource<Int>
    suspend fun isSignedIn(): Boolean
    suspend fun getUserName(): String?
    suspend fun logout(refreshToken: String): Resource<Unit>

}