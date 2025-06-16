package com.tonyxlab.notemark.presentation.screens.login.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface LoginActionEvent: ActionEvent {
    data object NavigateToMainScreen: LoginActionEvent
    data object NavigateToSignupScreen: LoginActionEvent
}
