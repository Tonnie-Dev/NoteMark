package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import kotlinx.coroutines.flow.collectLatest

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel() : LoginBaseViewModel() {

    init {

        launch {

            run()
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

        updateState { it.copy(isPasswordVisible = !currentState.isPasswordVisible) }
    }

    private fun onLogin() {
        sendActionEvent(LoginActionEvent.NavigateToMainScreen)
    }

    private fun onRegister() {
        sendActionEvent(LoginActionEvent.NavigateToRegistrationScreen)
    }

    fun isValidEmail(email: CharSequence?): Boolean {
        return !email.isNullOrBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
    }

    suspend fun run() {

        snapshotFlow { currentState.emailTextFieldState.text }.collectLatest {

            textFieldState ->
           updateFieldError(textFieldState)
        }
    }





    fun updateFieldError(emailText: CharSequence) {
      //  val email = currentState.emailTextFieldState.text
        val password = currentState.passwordTextFieldState.text

        val error = when {
            !isValidEmail(emailText) -> LoginUiState.FieldError.InvalidEmail
            password.length < 6 -> LoginUiState.FieldError.InvalidPassword
            else -> null
        }

        updateState {


            it.copy(fieldError = error)
        }
    }



}
