package com.tonyxlab.notemark.data.remote.auth.mappers

import com.tonyxlab.notemark.data.remote.auth.dto.AccessTokenRequest
import com.tonyxlab.notemark.data.remote.auth.dto.RegistrationRequest
import com.tonyxlab.notemark.domain.model.Credentials

fun Credentials.toRegistrationRequest() = RegistrationRequest(
        username = username,
        email = email,
        password = password
)

fun Credentials.toAccessTokenRequest() = AccessTokenRequest(
        email = email,
        password = password
)
