package com.tonyxlab.notemark.data.remote.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    val email: String,
    val password: String
)