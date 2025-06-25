package com.tonyxlab.notemark.presentation.screens.login.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface LoginActionEvent : ActionEvent {
    data object NavigateToHomeScreen : LoginActionEvent
    data object NavigateToSignupScreen : LoginActionEvent
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val actionLabelRes: Int,
        val onActionClick: LoginUiEvent? = null,
        val isError: Boolean = false
    ) : LoginActionEvent
}
