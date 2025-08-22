package com.tonyxlab.notemark.data.remote.auth


import com.tonyxlab.notemark.BuildConfig
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.network.HttpClientFactory
import com.tonyxlab.notemark.data.remote.auth.dto.AccessTokenResponse
import com.tonyxlab.notemark.data.remote.auth.dto.LogoutRequest
import com.tonyxlab.notemark.data.remote.auth.mappers.toAccessTokenRequest
import com.tonyxlab.notemark.data.remote.auth.mappers.toRegistrationRequest
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.Credentials
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.util.ApiEndpoints
import com.tonyxlab.notemark.util.Constants.EMAIL_HEADER_KEY
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
import timber.log.Timber

class AuthRepositoryImpl(
    private val httpClientFactory: HttpClientFactory,
    private val dataStore: DataStore,
) : AuthRepository {

    override suspend fun register(credentials: Credentials): Resource<Int> =

        withContext(Dispatchers.IO) {

            try {

                val client = httpClientFactory.provideMainHttpClient()

                val result = client.post(ApiEndpoints.REGISTRATION_ENDPOINT) {
                    contentType(ContentType.Application.Json)
                    header("X-User-Email", BuildConfig.USER_EMAIL)
                    setBody(credentials.toRegistrationRequest())
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

    override suspend fun login(credentials: Credentials): Resource<Int> =

        withContext(Dispatchers.IO) {
            try {

                val client = httpClientFactory.provideMainHttpClient()

                val result = client.post {
                    url(ApiEndpoints.LOGIN_ENDPOINT)
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Accept, ContentType.Application.Json)
                    header(EMAIL_HEADER_KEY, BuildConfig.USER_EMAIL)
                    setBody(credentials.toAccessTokenRequest())
                }

                when (result.status.value) {
                    200 -> {
                        val accessTokenResponse = result.body<AccessTokenResponse>()
                        Timber.tag("AuthRepositoryImpl")
                                .i("AccessToken ${accessTokenResponse.accessToken}")
                        dataStore.saveTokens(
                                accessToken = accessTokenResponse.accessToken,
                                refreshToken = accessTokenResponse.refreshToken,
                                username = accessTokenResponse.username
                        )

                        dataStore.getOrCreateInternalUserId()
                        Resource.Success(0)
                    }

                    in 400..499 -> {
                        Resource.Error(Exception("Login failed: "))
                    }

                    else -> {
                        Resource.Error(Exception("Login failed: "))
                    }
                }


            } catch (e: Exception) {
                Resource.Error(e)
            }
        }


    override suspend fun isSignedIn(): Boolean {
        val accessToken = dataStore.getAccessToken()
        val username = dataStore.getUsername()
        return !accessToken.isNullOrBlank() && !username.isNullOrBlank()

    }

    override suspend fun getUserName(): String? {
        return dataStore.getUsername()
    }

    override suspend fun logout(refreshToken: String): Resource<Unit> =

        safeIoCall {

            val client = httpClientFactory.provideMainHttpClient()

            client.post(ApiEndpoints.LOGIN_ENDPOINT) {
                contentType(ContentType.Application.Json)
                header("X-User-Email", BuildConfig.USER_EMAIL)
                setBody(LogoutRequest(refreshToken = refreshToken))
            }

            Unit

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


