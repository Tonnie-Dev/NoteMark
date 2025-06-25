package com.tonyxlab.notemark.presentation.screens.login.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.data.dto.LoginResponse
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

@Stable
data class LoginUiState(
    val fieldTextState:FieldTextState = FieldTextState(),
    val fieldError: FieldError = FieldError(),
    val loginStatus: Resource<LoginResponse> = Resource.Empty,
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


val TextFieldState.toText:String
    get() = text.toString()
