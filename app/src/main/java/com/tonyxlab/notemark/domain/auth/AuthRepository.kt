package com.tonyxlab.notemark.domain.auth

import com.tonyxlab.notemark.domain.model.LoginRequest
import com.tonyxlab.notemark.domain.model.RefreshRequest
import com.tonyxlab.notemark.domain.model.RegisterRequest
import com.tonyxlab.notemark.domain.model.Resource

interface AuthRepository {

    fun register(registerRequest: RegisterRequest): Resource<Int>
    fun login(loginRequest: LoginRequest): Resource<Int>
    fun refreshToken(refreshRequest: RefreshRequest): Resource<Int>
}