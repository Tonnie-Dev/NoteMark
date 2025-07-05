package com.tonyxlab.notemark.data.remote.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val username: String,
    val email: String,
    val password: String
)