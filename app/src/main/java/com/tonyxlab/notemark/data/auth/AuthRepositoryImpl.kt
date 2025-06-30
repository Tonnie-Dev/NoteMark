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
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {


    override suspend fun register(registerRequest: RegisterRequest): Resource<Int> =
        withContext(Dispatchers.IO) {

            try {
                val email = registerRequest.email.trim()

                val result = client.post(ApiEndpoints.REGISTRATION_ENDPOINT) {
                    contentType(ContentType.Application.Json)
                    header("X-User-Email", email)
                    setBody(registerRequest)
                }

                if (result.status.isSuccess()) {

                    Resource.Success(result.status.value)

                } else {
                    val errorBody = result.bodyAsText()

                    Resource.Error(Exception("Login failed: ${result.status.value}"))
                }

            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun login(loginRequest: LoginRequest): Resource<LoginResponse> =

        withContext(Dispatchers.IO) {
            try {

                val email = loginRequest.email.trim()
                val password = loginRequest.password.trim()

                val result = client.post {
                    url(ApiEndpoints.LOGIN_ENDPOINT)
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Accept, ContentType.Application.Json)
                    header("X-User-Email", email)
                    setBody(LoginRequest(email = email, password = password))
                }

                if (result.status.isSuccess()) {

                    val loginResponse = result.body<LoginResponse>()

                    TokenStorage.saveTokens(
                            accessToken = loginResponse.accessToken,
                            refreshToken = loginResponse.refreshToken,
                            username = loginResponse.username
                    )

                    Resource.Success(loginResponse)
                } else {
                    val errorBody = result.bodyAsText()
                    Resource.Error(Exception("Login failed: ${result.status.value}, $errorBody"))

                }

            } catch (e: Exception) {
                Resource.Error(e)
            }
        }


    override suspend fun isSignedIn(): Boolean {
        val accessToken = TokenStorage.getAccessToken()
        val username = TokenStorage.getUsername()
        return !accessToken.isNullOrBlank() && !username.isNullOrBlank()

    }

    override suspend fun getUserName(): String? {
        return TokenStorage.getUsername()
    }
}


