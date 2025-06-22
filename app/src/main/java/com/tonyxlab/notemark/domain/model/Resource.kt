package com.tonyxlab.notemark.domain.model

sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

interface AuthRepository{

    suspend fun register(registerRequest:RegisterRequest): Resource<Int>
    suspend fun register(loginRequest:LoginRequest): Resource<Int>
    suspend fun register(refreshRequest:RefreshRequest): Resource<Int>
}