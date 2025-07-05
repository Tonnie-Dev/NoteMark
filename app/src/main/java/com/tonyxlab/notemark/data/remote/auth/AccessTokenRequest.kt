package com.tonyxlab.notemark.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    val refreshToken: String
)

