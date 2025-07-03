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
import io.ktor.client.plugins.expectSuccess
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
import timber.log.Timber

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

    override suspend fun login(loginRequest: LoginRequest): Resource<Int> =

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
                }.body<LoginResponse>()

                when(result.username){

                    200 -> {

                        Timber.tag("AuthRepo").i("Success")
                        val loginResponse = result.body<LoginResponse>()

                        TokenStorage.saveTokens(
                                accessToken = loginResponse.accessToken,
                                refreshToken = loginResponse.refreshToken,
                                username = loginResponse.username
                        )

                        Resource.Success(0)
                    }

                    in 400 ..499 -> {

                        val errorBody = result.bodyAsText()

                        Timber.tag("AuthRepo").i("400 .. 499")
                        Resource.Error(Exception("Login failed: "))
                    }

                    else ->{

                        Timber.tag("AuthRepo").i("Some other error")
                        Resource.Error(Exception("Login failed: "))
                    }
                }



            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

/*

    override suspend fun login(loginRequest: LoginRequest): Resource<Int> =

        withContext(Dispatchers.IO) {

            try {

                Timber.tag("AuthRepo").i("Inside Try-Block")
                val email = loginRequest.email.trim()
                val password = loginRequest.password.trim()
                Timber.tag("AuthRepo").i("About to send login request")

                val result = client.post {
                    url(ApiEndpoints.LOGIN_ENDPOINT)
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Accept, ContentType.Application.Json)
                    header("X-User-Email", email)
                    setBody(LoginRequest(email = email, password = password))
                   // expectSuccess = false

                  //  Timber.tag("AuthRepo").i("Status Leaving result")
                }

              //  Timber.tag("AuthRepo").i("Status: ${result.status}")
                //Timber.tag("AuthRepo").i("isSuccess: ${result.status.isSuccess()}")

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
                    Resource.Error(Exception("Login failed: ${result.status.value}"))

                }
             */
/*   when(result.status.value){

                    200 -> {

                        Timber.tag("AuthRepo").i("Success")
                        val loginResponse = result.body<LoginResponse>()

                        TokenStorage.saveTokens(
                                accessToken = loginResponse.accessToken,
                                refreshToken = loginResponse.refreshToken,
                                username = loginResponse.username
                        )

                        Resource.Success(loginResponse)
                    }

                    in 400 ..499 -> {

                        val errorBody = result.bodyAsText()

                        Timber.tag("AuthRepo").i("Error on Else Block")
                        Resource.Error(Exception("Login failed: ${result.status.value}, $errorBody"))
                    }
                }


                Resource.Error(Exception("Login failed: ${result.status.value}"))*//*


            } catch (e: Exception) {
                Timber.tag("AuthRepo").e(e, "Login request failed")
                Timber.tag("AuthRepo").i("Other Error")
                Resource.Error(e as Exception)
            }
        }

*/

    override suspend fun isSignedIn(): Boolean {
        val accessToken = TokenStorage.getAccessToken()
        val username = TokenStorage.getUsername()
        return !accessToken.isNullOrBlank() && !username.isNullOrBlank()

    }

    override suspend fun getUserName(): String? {
        return TokenStorage.getUsername()
    }
}


