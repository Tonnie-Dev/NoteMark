package com.tonyxlab.notemark.data.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)