package com.tonyxlab.notemark.data.remote.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(val refreshToken: String)
