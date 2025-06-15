package com.tonyxlab.notemark.presentation.screens.login.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

@Stable
data class LoginUiState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val fieldError: FieldError? = null,
    val isSecureText: Boolean = true

) : UiState {

    @Stable
    sealed interface FieldError {
        data object InvalidEmail : FieldError
        data object InvalidPassword : FieldError
    }

    val isLoginButtonEnabled: Boolean
        get() = fieldError == null

}