@file:OptIn(FlowPreview::class)

package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.data.workmanager.SyncRequest
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.Credentials


import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.core.utils.allFieldsFilled
import com.tonyxlab.notemark.presentation.core.utils.checkIfError
import com.tonyxlab.notemark.presentation.core.utils.isValidEmail
import com.tonyxlab.notemark.presentation.core.utils.isValidLoginPassword
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import com.tonyxlab.notemark.presentation.screens.login.handling.toText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val syncRequest: SyncRequest
) : LoginBaseViewModel() {

    init {
        observeFieldsInput()
    }

    override val initialState: LoginUiState
        get() = LoginUiState()

    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            LoginUiEvent.Login -> login()
            LoginUiEvent.RegisterAccount -> onRegister()
            LoginUiEvent.TogglePasswordVisibility -> toggledPasswordVisibility()
        }
    }

    private fun toggledPasswordVisibility() {
        updateState { it.copy(isSecureText = !currentState.isSecureText) }
    }

    private fun onRegister() {
        sendActionEvent(LoginActionEvent.NavigateToSignupScreen)
    }

    private fun observeFieldsInput() {

        launch {

            val emailTextFlow =
                snapshotFlow { currentState.fieldTextState.emailTextFieldState.text }.debounce(300)
                        .distinctUntilChanged()
            val passwordTextFlow =
                snapshotFlow { currentState.fieldTextState.passwordTextFieldState.text }.debounce(
                        300
                )
                        .distinctUntilChanged()

            combine(emailTextFlow, passwordTextFlow) { emailText, passwordText ->
                val fieldError = updateFieldError(emailText, passwordText)

                updateState { it.copy(fieldError = fieldError) }
            }.collect()
        }
    }

    private fun login() {

        launchCatching(
                onError = { throwable ->

                    val exception =
                        throwable as? Exception ?: Exception(throwable.message, throwable)

                    updateState { it.copy(loginStatus = Resource.Error(exception = exception)) }

                    sendActionEvent(
                            LoginActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_login_failed,
                                    actionLabelRes = R.string.snack_text_retry,
                                    loginUiEvent = LoginUiEvent.Login
                            )
                    )

                }
        ) {
            updateState { it.copy(loginStatus = Resource.Loading) }

            val creds = Credentials(
                    email = currentState.fieldTextState.emailTextFieldState.toText
                            .trim()
                            .lowercase(),
                    password = currentState.fieldTextState.passwordTextFieldState.toText
                            .trim()
            )

            val result = authRepository.login(credentials = creds)

            when (result) {
                is Resource.Success -> {
                    updateState { it.copy(loginStatus = Resource.Success(result.data)) }
                    syncRequest.enqueuePeriodicSync(SyncInterval.MANUAL)
                    sendActionEvent(LoginActionEvent.NavigateToHomeScreen)
                }

                is Resource.Error -> throw result.exception
                else -> Unit
            }
        }
    }

    private fun updateFieldError(
        emailText: CharSequence,
        passwordText: CharSequence
    ): LoginUiState.FieldError {

        return LoginUiState.FieldError(
                emailError = checkIfError(emailText, ::isValidEmail),
                passwordError = checkIfError(passwordText, ::isValidLoginPassword),
                allFieldsFilled = allFieldsFilled(emailText, passwordText)
        )
    }
}
