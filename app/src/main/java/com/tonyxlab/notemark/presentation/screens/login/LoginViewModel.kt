package com.tonyxlab.notemark.presentation.screens.login

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel() : LoginBaseViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState

    override val initialState: LoginUiState
        get() = LoginUiState()


    override fun onEvent(event: LoginUiEvent) {

        when (event) {
            is LoginUiEvent.EnterEmail -> editEmail(event.email)
            is LoginUiEvent.EnterPassword -> TODO()
            LoginUiEvent.Login -> TODO()
            LoginUiEvent.RegisterAccount -> TODO()
            LoginUiEvent.TogglePasswordVisibility -> TODO()
        }
    }


    private fun editEmail(emailText: String) {

   currentState.emailTextFieldState.edit { replace(0, length, emailText) }

    }
}
