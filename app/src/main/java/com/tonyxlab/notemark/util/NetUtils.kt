package com.tonyxlab.notemark.util

object ApiEndpoints {

    // Base URL
    const val BASE_URL = "https://notemark.pl-coding.com"

    // Endpoints
    const val REGISTRATION_ENDPOINT = "$BASE_URL/api/auth/register"
    const val LOGIN_ENDPOINT = "$BASE_URL/api/auth/login"
    const val REFRESH_ENDPOINT = "$BASE_URL/api/auth/refresh"

    const val CREATE_ENDPOINT = "$BASE_URL/api/notes"

    const val LOGOUT_ENDPOINT = "$BASE_URL/api/auth/logout"

    //Header
    const val EMAIL_HEADER = "X-User-Email"
}



