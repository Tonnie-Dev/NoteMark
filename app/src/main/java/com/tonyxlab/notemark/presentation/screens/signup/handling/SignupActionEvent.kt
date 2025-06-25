package com.tonyxlab.notemark.presentation.screens.signup.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface SignupActionEvent : ActionEvent {
    data object NavigateToLoginScreen : SignupActionEvent
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val actionLabelRes:Int,
        val onActionClick: SignupUiEvent? = null,
        val isError: Boolean = false,
    ) : SignupActionEvent

}
