package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel() : LoginBaseViewModel() {

    init {

        launch {

            observeEmailAndPasswordInputs()
        }
    }

    override val initialState: LoginUiState
        get() = LoginUiState()


    override fun onEvent(event: LoginUiEvent) {

        when (event) {
            is LoginUiEvent.EnterEmail -> TODO()
            is LoginUiEvent.EnterPassword -> TODO()
            LoginUiEvent.Login -> onLogin()
            LoginUiEvent.RegisterAccount -> onRegister()
            LoginUiEvent.TogglePasswordVisibility -> onTogglePasswordVisibility()
        }
    }

    private fun onTogglePasswordVisibility() {

        updateState { it.copy(isSecureText = !currentState.isSecureText) }
    }

    private fun onLogin() {
        sendActionEvent(LoginActionEvent.NavigateToMainScreen)
    }

    private fun onRegister() {
        sendActionEvent(LoginActionEvent.NavigateToRegistrationScreen)
    }


    private suspend fun observeEmailAndPasswordInputs() {

        val emailTextFlow = snapshotFlow { currentState.emailTextFieldState.text }
        val passwordTextFlow = snapshotFlow { currentState.passwordTextFieldState.text }

        combine(emailTextFlow, passwordTextFlow) { emailText, passwordText ->

            validateEmailAndPassword(emailText, passwordText)
        }.collect()

    }


    fun validateEmailAndPassword(emailText: CharSequence, passwordText: CharSequence) {

        val error = when {
            !isValidEmail(emailText) -> LoginUiState.FieldError.InvalidEmail
            passwordText.isBlank() -> LoginUiState.FieldError.InvalidPassword
            else -> null
        }

        updateState {
            it.copy(fieldError = error)
        }
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return !email.isNullOrBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
    }
}
