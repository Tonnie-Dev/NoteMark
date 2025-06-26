package com.tonyxlab.notemark.data.auth

import com.tonyxlab.notemark.data.datastore.TokenStorage
import com.tonyxlab.notemark.data.dto.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val access = TokenStorage.getAccessToken()
                    val refresh = TokenStorage.getRefreshToken()

                    if (access != null && refresh != null) {
                        BearerTokens(access, refresh)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val refreshToken = TokenStorage.getRefreshToken() ?: return@refreshTokens null

                    try {
                        val refreshResponse = client.post("https://your.base.url/api/auth/refresh") {
                            header("Authorization", "Bearer $refreshToken")
                        }

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
