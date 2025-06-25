@file:OptIn(FlowPreview::class)

package com.tonyxlab.notemark.presentation.screens.signup

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.RegisterRequest
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.core.utils.allFieldsFilled
import com.tonyxlab.notemark.presentation.core.utils.checkIfError
import com.tonyxlab.notemark.presentation.core.utils.isSameAs
import com.tonyxlab.notemark.presentation.core.utils.isValidEmail
import com.tonyxlab.notemark.presentation.core.utils.isValidPassword
import com.tonyxlab.notemark.presentation.core.utils.isValidUsername
import com.tonyxlab.notemark.presentation.screens.login.handling.toText
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupActionEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiState
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiState.FieldError
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

typealias SignupBaseViewModel = BaseViewModel<SignupUiState, SignupUiEvent, SignupActionEvent>

class SignupViewModel(private val authRepository: AuthRepository) : SignupBaseViewModel() {

    override val initialState: SignupUiState
        get() = SignupUiState()

    init {
        observeFieldsInputs()
    }

    override fun onEvent(event: SignupUiEvent) {
        when (event) {
            SignupUiEvent.TogglePasswordOneVisibility -> togglePasswordOne()
            SignupUiEvent.TogglePasswordTwoVisibility -> togglePasswordTwo()
            SignupUiEvent.LoginToExistingAccount -> login()
            SignupUiEvent.CreateAccount -> createAccount()
            SignupUiEvent.RetrySnackbarAction -> createAccount()
            SignupUiEvent.LoginSnackbarAction -> login()
        }
    }

    private fun login() {
        sendActionEvent(SignupActionEvent.NavigateToLoginScreen)
    }

    private fun createAccount() {

        launch {

            updateState { it.copy(loginStatus = Resource.Loading) }

            when (
                val response = authRepository.register(
                        registerRequest = RegisterRequest(
                                currentState.fieldTextState.username.toText,
                                currentState.fieldTextState.email.toText,
                                currentState.fieldTextState.passwordOne.toText
                        )
                )) {

                is Resource.Success -> {
                    sendActionEvent(
                            SignupActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_signup_success,
                                    actionLabelRes = R.string.snack_text_login,
                                    isError = false,
                                    onActionClick = SignupUiEvent.LoginSnackbarAction
                            )
                    )

                    updateState { it.copy(loginStatus = Resource.Success(response.data)) }
                }

                else -> {
Timber.i("Error is: ${response.toString()}")
                    updateState { it.copy(loginStatus = Resource.Error(Exception("Signup failed"))) }

                    sendActionEvent(
                            SignupActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_signup_failed,
                                    actionLabelRes = R.string.snack_text_try_again,
                                    isError = true,
                                    onActionClick = SignupUiEvent.RetrySnackbarAction
                            )
                    )

                }
            }

        }
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
            }.debounce(300)
                    .distinctUntilChanged()
            val emailFlow = snapshotFlow {
                currentState.fieldTextState.email.text
            }.debounce(300)
                    .distinctUntilChanged()
            val passwordOneFlow = snapshotFlow {
                currentState.fieldTextState.passwordOne.text
            }.debounce(300)
                    .distinctUntilChanged()
            val passwordTwoFlow = snapshotFlow {
                currentState.fieldTextState.passwordTwo.text
            }.debounce(300)
                    .distinctUntilChanged()
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
                usernameError = checkIfError(username, ::isValidUsername),
                emailError = checkIfError(email, ::isValidEmail),
                passwordError = checkIfError(passwordOne, ::isValidPassword),
                confirmPasswordError = checkIfError(passwordTwo) { passwordOne isSameAs passwordTwo },
                allFieldsFilled = allFieldsFilled(username, email, passwordOne, passwordTwo)
        )
    }

}