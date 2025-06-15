package com.tonyxlab.notemark.presentation.screens.login

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel() : LoginBaseViewModel() {

    override val initialState: LoginUiState
        get() = LoginUiState()


    override fun onEvent(event: LoginUiEvent) {

        when (event) {
            is LoginUiEvent.EnterEmail -> TODO()
            is LoginUiEvent.EnterPassword -> TODO()
            LoginUiEvent.Login -> onLogin()
            LoginUiEvent.RegisterAccount ->onRegister()
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

}
