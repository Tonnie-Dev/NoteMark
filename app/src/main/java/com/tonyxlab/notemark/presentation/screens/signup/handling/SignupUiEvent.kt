package com.tonyxlab.notemark.presentation.screens.signup.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent

sealed interface SignupUiEvent : UiEvent {
    data object CreateAccount : SignupUiEvent
    data object TogglePasswordOneVisibility: SignupUiEvent
    data object TogglePasswordTwoVisibility: SignupUiEvent
    data object LoginToExistingAccount : SignupUiEvent
    data object LoginSnackbarAction: SignupUiEvent
    data object RetrySnackbarAction: SignupUiEvent

}
