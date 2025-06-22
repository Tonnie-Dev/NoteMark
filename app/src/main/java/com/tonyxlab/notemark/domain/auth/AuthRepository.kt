package com.tonyxlab.notemark.domain.auth

import com.tonyxlab.notemark.data.dto.LoginResponse
import com.tonyxlab.notemark.domain.model.LoginRequest
import com.tonyxlab.notemark.domain.model.RegisterRequest
import com.tonyxlab.notemark.domain.model.Resource

interface AuthRepository {

    suspend fun register(registerRequest: RegisterRequest): Resource<Int>
    suspend fun login(loginRequest: LoginRequest): Resource<LoginResponse>

}