package com.tonyxlab.notemark.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)