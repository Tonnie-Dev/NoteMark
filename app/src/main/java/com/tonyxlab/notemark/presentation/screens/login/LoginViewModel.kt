package com.tonyxlab.notemark.presentation.screens.login

import android.text.TextUtils.replace
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel() : LoginBaseViewModel() {

    init {
        launch {

            run()
        }
    }

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState

    override val initialState: LoginUiState
        get() = LoginUiState()


    override fun onEvent(event: LoginUiEvent) {

        Timber.i("OnEvent() called")

        when (event) {
            is LoginUiEvent.EnterEmail -> editEmail(event.email)
            is LoginUiEvent.EnterPassword -> TODO()
            LoginUiEvent.Login -> onLogin()
            LoginUiEvent.RegisterAccount ->onRegister()
            LoginUiEvent.TogglePasswordVisibility -> onTogglePasswordVisibility()
        }
    }


    private fun editEmail(emailText: String) {
        Timber.i("editEmail event")
        currentState.emailTextFieldState.edit { replace(0, length, emailText) }
        //updateState { it.copy(emailTextFieldState = it.emailTextFieldState.apply { text = emailText }) }
    }

    private fun editPassword(passwordText: String) {
        Timber.i("editPassword event")
        currentState.passwordTextFieldState.edit { replace(0, length, passwordText) }

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


    suspend fun run() {
        snapshotFlow { currentState.emailTextFieldState.text }
                // Let fast typers get multiple keystrokes in before kicking off a search.
                .debounce(500)
                // collectLatest cancels the previous search if it's still running when there's a
                // new change.
                .collectLatest { queryText -> editEmail(queryText.toString()) }
    }

}
