package com.tonyxlab.notemark.di

import com.tonyxlab.notemark.data.local.datastore.TokenStorage
import com.tonyxlab.notemark.data.network.HttpClientFactory
import com.tonyxlab.notemark.data.remote.auth.AuthRepositoryImpl
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
        TokenStorage.init(androidContext())
        HttpClientFactory()
    }

}

val repositoryModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get()) }
}