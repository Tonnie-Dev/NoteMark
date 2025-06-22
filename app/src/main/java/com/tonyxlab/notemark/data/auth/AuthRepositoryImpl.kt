package com.tonyxlab.notemark.data.auth

import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.LoginRequest
import com.tonyxlab.notemark.domain.model.RegisterRequest
import com.tonyxlab.notemark.domain.model.Resource
import io.ktor.client.HttpClient


class AuthRepositoryImpl (client: HttpClient): AuthRepository{
    override suspend fun register(registerRequest: RegisterRequest): Resource<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun login(loginRequest: LoginRequest): Resource<Int> {
        TODO("Not yet implemented")
    }


}
/*
fun installAuth(){


    HttpClient(CIO){


        install(Auth ){

            bearer{

                loadTokens {
                    TODO()

                }

                refreshTokens {
                    TODO()
                }
            }
        }
    }
}

val tokenResponse: TokenResponse= client.post("https://api.example.com/login") {
    setBody(LoginRequest(username, password))
}

fun sessionStorage() : String = ""
interface AuthRepository {
    suspend fun  register(registerRequest: RegisterRequest): ApiResult<Int>
}*/
