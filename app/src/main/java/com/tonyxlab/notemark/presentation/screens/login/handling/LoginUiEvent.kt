package com.tonyxlab.notemark.presentation.screens.login.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface LoginUiEvent : UiEvent {

    data class EnterEmail(val email: String) : LoginUiEvent
    data class EnterPassword(val password: String) : LoginUiEvent
    data object Login : LoginUiEvent
    data object RegisterAccount : LoginUiEvent
}