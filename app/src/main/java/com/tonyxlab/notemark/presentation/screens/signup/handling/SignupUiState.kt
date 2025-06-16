package com.tonyxlab.notemark.presentation.screens.signup.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

@Stable
data class SignupUiState(
    val fieldTextState: FieldTextState = FieldTextState(),
    val fieldErrors: FieldErrors = FieldErrors(),
    val passwordVisibility: PasswordVisibilityState = PasswordVisibilityState()
): UiState {
    @Stable
    data class FieldTextState(
        val username: TextFieldState = TextFieldState(),
        val email: TextFieldState = TextFieldState(),
        val passwordOne: TextFieldState = TextFieldState(),
        val passwordTwo: TextFieldState = TextFieldState()
    ) {
        val allFieldsFilled: Boolean
            get() = username.text.isNotBlank()
                    && email.text.isNotBlank()
                    && passwordOne.text.isNotBlank()
                    && passwordTwo.text.isNotBlank()
    }
    @Stable
    sealed class FieldError {
        data object InvalidUserName : FieldError()
        data object InvalidEmail : FieldError()
        data object InvalidPassword : FieldError()
        data object PasswordsDoNotMatch : FieldError()
    }
    @Stable
    data class FieldErrors(
        val usernameError: FieldError? = null,
        val emailError: FieldError? = null,
        val passwordError: FieldError? = null,
        val confirmPasswordError: FieldError? = null
    )

    @Stable
    data class PasswordVisibilityState(
        val isPasswordOneVisible: Boolean = true,
        val isPasswordTwoVisible: Boolean = true
    )



    val doPasswordsMatch: Boolean
        get() = fieldTextState.passwordOne.text == fieldTextState.passwordTwo.text

    val hasNoErrors: Boolean
        get() = listOf(
                fieldErrors.usernameError,
                fieldErrors.emailError,
                fieldErrors.passwordError,
                fieldErrors.confirmPasswordError
        ).all { it == null }


    val isCreateAccountButtonEnabled: Boolean
        get() = fieldTextState.allFieldsFilled && doPasswordsMatch && hasNoErrors

}
