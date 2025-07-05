package com.tonyxlab.notemark.data.network

import com.tonyxlab.notemark.BuildConfig
import com.tonyxlab.notemark.data.local.datastore.TokenStorage
import com.tonyxlab.notemark.data.remote.auth.AccessTokenRequest
import com.tonyxlab.notemark.data.remote.auth.AccessTokenResponse
import com.tonyxlab.notemark.util.ApiEndpoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

class HttpClientFactoryTwo {

    private val defaultClient = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            header("X-User-Email", BuildConfig.USER_EMAIL)
        }
    }

    fun build(): HttpClient {

        return HttpClient(CIO) {

            install(ContentNegotiation) {

                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) {

                logger = object : Logger {

                    override fun log(message: String) {
                        Timber.tag("ApiRequest")
                                .d(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header("X-User-Email", BuildConfig.USER_EMAIL)
            }

            install(Auth) {

                bearer {

                    loadTokens {

                        BearerTokens(

                                accessToken = TokenStorage.getAccessToken() ?: "",
                                refreshToken = TokenStorage.getRefreshToken() ?: ""
                        )
                    }

                    refreshTokens {

                        val refreshToken =
                            TokenStorage.getRefreshToken() ?: return@refreshTokens null
                        try {
                            defaultClient.post(urlString = ApiEndpoints.REFRESH_ENDPOINT) {
                                setBody(
                                        AccessTokenRequest(
                                                refreshToken = TokenStorage.getRefreshToken() ?: ""
                                        )
                                )
                            }

                            if (response.status.isSuccess()) {
                                val newTokens = response.body<AccessTokenResponse>()
                                TokenStorage.saveTokens(
                                        accessToken = newTokens.accessToken,
                                        refreshToken = newTokens.refreshToken,
                                        username = newTokens.username
                                )
                                BearerTokens(
                                        newTokens.accessToken,
                                        newTokens.refreshToken
                                )
                            } else {
                                TokenStorage.clearTokens()
                                null
                            }

                        } catch (e: Exception) {
                            TokenStorage.clearTokens()
                            null
                        }
                    }
                }
            }
        }
    }
}
