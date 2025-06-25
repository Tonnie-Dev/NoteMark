package com.tonyxlab.notemark.presentation.screens.signup.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

@Stable
data class SignupUiState(
    val fieldTextState: FieldTextState = FieldTextState(),
    val fieldError: FieldError = FieldError(),
    val passwordVisibility: PasswordVisibilityState = PasswordVisibilityState(),
    val loginStatus: Resource<Int> = Resource.Empty
) : UiState {

    @Stable
    data class FieldTextState(
        val username: TextFieldState = TextFieldState(),
        val email: TextFieldState = TextFieldState(),
        val passwordOne: TextFieldState = TextFieldState(),
        val passwordTwo: TextFieldState = TextFieldState(),

        )

    @Stable
    data class FieldError(
        val usernameError: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false,
        val confirmPasswordError: Boolean = false,
        val allFieldsFilled: Boolean = true
    )

    @Stable
    data class PasswordVisibilityState(
        val isPasswordOneVisible: Boolean = true,
        val isPasswordTwoVisible: Boolean = true
    )

    val areAllFieldsFilled = fieldTextState.run {
        listOf(
                username.text,
                email.text,
                passwordOne.text,
                passwordTwo.text
        ).all { it.isNotBlank() }
    }


    @Stable
    val isCreateAccountButtonEnabled: Boolean
        get() = (fieldError == FieldError()) && areAllFieldsFilled

}


