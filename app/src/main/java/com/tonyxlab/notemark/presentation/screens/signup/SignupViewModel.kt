@file:OptIn(FlowPreview::class)

package com.tonyxlab.notemark.presentation.screens.signup

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.core.utils.allFieldsFilled
import com.tonyxlab.notemark.presentation.core.utils.checkIfError
import com.tonyxlab.notemark.presentation.core.utils.isSameAs
import com.tonyxlab.notemark.presentation.core.utils.isValidEmail
import com.tonyxlab.notemark.presentation.core.utils.isValidPassword
import com.tonyxlab.notemark.presentation.core.utils.isValidUsername
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupActionEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiState
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiState.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import okio.AsyncTimeout.Companion.condition

typealias SignupBaseViewModel = BaseViewModel<SignupUiState, SignupUiEvent, SignupActionEvent>

class SignupViewModel : SignupBaseViewModel() {

    override val initialState: SignupUiState
        get() = SignupUiState()


    init {
        observeFieldsInputs()
    }

    override fun onEvent(event: SignupUiEvent) {
        when (event) {
            SignupUiEvent.CreateAccount -> createAccount()
            SignupUiEvent.LoginToExistingAccount -> login()
            SignupUiEvent.TogglePasswordOneVisibility -> togglePasswordOne()
            SignupUiEvent.TogglePasswordTwoVisibility -> togglePasswordTwo()
        }
    }

    private fun login() {
        sendActionEvent(SignupActionEvent.NavigateToLoginScreen)
    }

    private fun createAccount() {
        sendActionEvent(SignupActionEvent.NavigateToMainScreen)
    }

    private fun togglePasswordOne() {

        val currentVisibility = currentState.passwordVisibility

        updateState {
            it.copy(
                    passwordVisibility = currentVisibility.copy(
                            isPasswordOneVisible = !currentVisibility.isPasswordOneVisible
                    )
            )
        }
    }

    private fun togglePasswordTwo() {

        val currentVisibilityState = currentState.passwordVisibility

        updateState {
            it.copy(
                    passwordVisibility = currentVisibilityState.copy(
                            isPasswordTwoVisible = !currentVisibilityState.isPasswordTwoVisible
                    )
            )
        }
    }


    private fun observeFieldsInputs() {

        launch {

            val usernameFlow = snapshotFlow {
                currentState.fieldTextState.username.text
            }.debounce(300).distinctUntilChanged()
            val emailFlow = snapshotFlow {
                currentState.fieldTextState.email.text
            }.debounce(300).distinctUntilChanged()
            val passwordOneFlow = snapshotFlow {
                currentState.fieldTextState.passwordOne.text
            }.debounce(300).distinctUntilChanged()
            val passwordTwoFlow = snapshotFlow {
                currentState.fieldTextState.passwordTwo.text
            }.debounce(300).distinctUntilChanged()
            combine(
                    usernameFlow,
                    emailFlow,
                    passwordOneFlow,
                    passwordTwoFlow
            ) { username, email, passwordOne, passwordTwo ->

                val fieldError = updateFieldsError(
                        username = username,
                        email = email,
                        passwordOne = passwordOne,
                        passwordTwo = passwordTwo
                )

                updateState { it.copy(fieldError = fieldError) }


            }.collect()

        }

    }

    private fun updateFieldsError(
        username: CharSequence,
        email: CharSequence,
        passwordOne: CharSequence,
        passwordTwo: CharSequence
    ): FieldError {

        return FieldError(
                usernameError = checkIfError  (username, ::isValidUsername),
                emailError = checkIfError(email, ::isValidEmail),
                passwordError =checkIfError (passwordOne, ::isValidPassword),
                confirmPasswordError = checkIfError (passwordTwo){ passwordOne isSameAs passwordTwo},
                allFieldsFilled = allFieldsFilled(username,email, passwordOne, passwordTwo)
        )
    }


}