package com.tonyxlab.notemark.presentation.screens.signup

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupActionEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiState

typealias SignupBaseViewModel = BaseViewModel<SignupUiState, SignupUiEvent, SignupActionEvent>

class SignupViewModel : SignupBaseViewModel() {

    override val initialState: SignupUiState
        get() = SignupUiState()

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
}