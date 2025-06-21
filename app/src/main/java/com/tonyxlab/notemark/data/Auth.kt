package com.tonyxlab.notemark.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer


fun installAuth(){


    HttpClient(CIO){


        install(Auth ){

            bearer{

                loadTokens {

                    val tokenPair = sess

                    BearerTokens(accessToken = tokenPair)



                }

            }
        }
    }
}
