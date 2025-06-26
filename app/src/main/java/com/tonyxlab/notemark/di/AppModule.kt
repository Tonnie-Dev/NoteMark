package com.tonyxlab.notemark.di

import com.tonyxlab.notemark.data.auth.AuthRepositoryImpl
import com.tonyxlab.notemark.data.auth.provideHttpClient
import com.tonyxlab.notemark.data.datastore.TokenStorage
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.presentation.screens.login.LoginViewModel
import com.tonyxlab.notemark.presentation.screens.signup.SignupViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)

}

val networkModule = module {

    // Provide HttpClient
    single {
        TokenStorage.init(androidContext()) // Only once at startup
        provideHttpClient()
//        HttpClient(CIO) {
//            install(ContentNegotiation) {
//                json( json = Json{ ignoreUnknownKeys = true})
//            }
//            // Add Auth plugin or interceptors here later
//        }
    }

}

val repositoryModule = module {

  single<AuthRepository> { AuthRepositoryImpl(get()) }

}