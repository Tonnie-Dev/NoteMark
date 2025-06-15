package com.tonyxlab.notemark.presentation.screens.login.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

@Stable
data class LoginUiState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val fieldError: FieldError? = null,
    val isPasswordVisible: Boolean = false,
    val isLoginButtonEnabled: Boolean = false
) : UiState {

    @Stable
    sealed interface FieldError {
        data object InvalidEmail : FieldError
        data object InvalidPassword : FieldError
    }
}
class TextFieldState {
    var text by mutableStateOf("")
}