package com.tonyxlab.notemark.data.auth


import com.tonyxlab.notemark.data.datastore.TokenStorage
import com.tonyxlab.notemark.data.dto.LoginResponse
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.LoginRequest
import com.tonyxlab.notemark.domain.model.RegisterRequest
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.util.ApiEndpoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {
    override suspend fun register(registerRequest: RegisterRequest): Resource<Int> =
        withContext(Dispatchers.IO) {
            try {
                val response = client.post(ApiEndpoints.REGISTRATION_ENDPOINT) {
                    contentType(ContentType.Application.Json)
                    setBody(registerRequest)
                }

                Resource.Success(response.status.value)

            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun login(loginRequest: LoginRequest): Resource<LoginResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response: LoginResponse = client.post {
                    url(ApiEndpoints.LOGIN_ENDPOINT)
                    contentType(ContentType.Application.Json)
                    setBody(loginRequest)
                }
                        .body()

                // Here you'd store the tokens locally (DataStore, EncryptedPrefs, etc)
                TokenStorage.saveTokens(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken
                )

                Resource.Success(response)

            } catch (e: Exception) {
                Resource.Error(e)
            }
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
