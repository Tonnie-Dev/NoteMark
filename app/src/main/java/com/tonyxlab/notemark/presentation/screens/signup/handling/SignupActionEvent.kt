package com.tonyxlab.notemark.presentation.screens.signup.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface SignupActionEvent: ActionEvent {
    data object NavigateToMainScreen: SignupActionEvent
    data object NavigateToLoginScreen: SignupActionEvent
}
