package com.tonyxlab.notemark.di

import com.tonyxlab.notemark.data.auth.AuthRepositoryImpl
import com.tonyxlab.notemark.data.auth.provideHttpClient
import com.tonyxlab.notemark.data.datastore.TokenStorage
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.presentation.screens.home.HomeViewModel
import com.tonyxlab.notemark.presentation.screens.landing.LandingViewModel
import com.tonyxlab.notemark.presentation.screens.login.LoginViewModel
import com.tonyxlab.notemark.presentation.screens.signup.SignupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::LandingViewModel)
    viewModelOf(::HomeViewModel)
}

val networkModule = module {

    single {
        TokenStorage.init(androidContext()) // Only once at startup
        provideHttpClient()

    }

}

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get()) }

}