@file:OptIn(FlowPreview::class)

package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.core.utils.allFieldsFilled
import com.tonyxlab.notemark.presentation.core.utils.checkIfError
import com.tonyxlab.notemark.presentation.core.utils.isValidEmail
import com.tonyxlab.notemark.presentation.core.utils.isValidLoginPassword
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

typealias LoginBaseViewModel = BaseViewModel<LoginUiState, LoginUiEvent, LoginActionEvent>

class LoginViewModel() : LoginBaseViewModel() {

    init {



            observeFieldsInput()

    }

    override val initialState: LoginUiState
        get() = LoginUiState()


    override fun onEvent(event: LoginUiEvent) {

        when (event) {
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
        sendActionEvent(LoginActionEvent.NavigateToSignupScreen)
    }


    private  fun observeFieldsInput() {

        launch {


            val emailTextFlow =
                snapshotFlow { currentState.fieldTextState.emailTextFieldState.text }.debounce(300)
                        .distinctUntilChanged()
            val passwordTextFlow =
                snapshotFlow { currentState.fieldTextState.passwordTextFieldState.text }.debounce(300)
                        .distinctUntilChanged()

            combine(emailTextFlow, passwordTextFlow) { emailText, passwordText ->
                val fieldError = updateFieldError(emailText, passwordText)

                Timber.i("1st Prop: ${fieldError.emailError}, 2nd Prop: ${fieldError.passwordError}")
                Timber.i("Text:${passwordText}, Length: ${passwordText.length}, isBlank: ${passwordText.isBlank()}, isEmpty: ${passwordText.isEmpty()}, isValid: ${passwordText.isNotBlank()}, cond: ${!isValidLoginPassword(passwordText)}")
                updateState { it.copy(fieldError = fieldError) }
            }.collect()
        }

    }


    private fun updateFieldError(
        emailText: CharSequence,
        passwordText: CharSequence
    ): LoginUiState.FieldError {

        return LoginUiState.FieldError(
                emailError = checkIfError(emailText, ::isValidEmail),
                passwordError = checkIfError (passwordText, ::isValidLoginPassword),
                allFieldsFilled = allFieldsFilled(emailText, passwordText)
        )
    }


}
