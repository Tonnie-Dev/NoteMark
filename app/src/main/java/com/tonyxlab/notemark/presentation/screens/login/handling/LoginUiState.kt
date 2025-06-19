package com.tonyxlab.notemark.presentation.screens.login.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

@Stable
data class LoginUiState(
    val fieldTextState:FieldTextState = FieldTextState(),
    val fieldError: FieldError = FieldError(),
    val isSecureText: Boolean = true

) : UiState {

   @Stable
   data class FieldTextState(
       val emailTextFieldState: TextFieldState = TextFieldState(),
       val passwordTextFieldState: TextFieldState = TextFieldState(),
   )

    @Stable
    data class FieldError(
        val emailError: Boolean = false,
        val passwordError: Boolean = false,
            val allFieldsFilled: Boolean = true
    )

    val areAllFieldsFilled = fieldTextState.run {

        listOf(emailTextFieldState.text, passwordTextFieldState.text).all { it.isNotBlank() }
    }
    val isLoginButtonEnabled: Boolean
        get() = (fieldError == FieldError()) && areAllFieldsFilled

}