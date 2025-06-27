package com.tonyxlab.notemark.data.auth

import android.R.attr.level
import android.system.Os.access
import com.tonyxlab.notemark.data.datastore.TokenStorage
import com.tonyxlab.notemark.data.dto.LoginResponse
import com.tonyxlab.notemark.util.ApiEndpoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

fun provideHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger{
                override fun log(message: String) {
                    Timber.tag("ApiRequest").d(message)
                }

            }
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val access = TokenStorage.getAccessToken()
                    val refresh = TokenStorage.getRefreshToken()
                    Timber.i("Trying to Load tokens - access:$access, refresh: $refresh")
                    if (access != null && refresh != null) {
                        BearerTokens(access, refresh)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val refreshToken = TokenStorage.getRefreshToken() ?: return@refreshTokens null
                    Timber.i("Refresh block called - token:$refreshToken")
                    try {

                        Timber.i("Trying to Refresh ...")
                        val refreshResponse = client.post(ApiEndpoints.REFRESH_ENDPOINT) {
                            header("Authorization", "Bearer $refreshToken")
                        }

                        Timber.i("Refresh response code is: ${refreshResponse.status}")
                        if (refreshResponse.status == HttpStatusCode.OK) {
                            val newTokens = refreshResponse.body<LoginResponse>()
                            TokenStorage.saveTokens(
                                    accessToken = newTokens.accessToken,
                                    refreshToken = newTokens.refreshToken
                            )
                            BearerTokens(newTokens.accessToken, newTokens.refreshToken)
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
