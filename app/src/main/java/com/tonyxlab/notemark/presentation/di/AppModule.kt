package com.tonyxlab.notemark.presentation.di

import com.tonyxlab.notemark.presentation.screens.login.LoginViewModel
import com.tonyxlab.notemark.presentation.screens.signup.SignupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
}