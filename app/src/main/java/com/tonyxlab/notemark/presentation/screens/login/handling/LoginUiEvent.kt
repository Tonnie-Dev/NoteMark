package com.tonyxlab.notemark.presentation.screens.login.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface LoginUiEvent : UiEvent {
    data object Login : LoginUiEvent
    data object RegisterAccount : LoginUiEvent
    data object TogglePasswordVisibility: LoginUiEvent
}