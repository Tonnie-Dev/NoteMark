package com.tonyxlab.notemark.presentation.screens.landing.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface LandingActionEvent : ActionEvent{

    data object NavigateToGetStarted: LandingActionEvent
    data object NavigateToHome: LandingActionEvent
    data object NavigateToLogin: LandingActionEvent
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val actionLabelRes: Int,
        val landingUiEvent: LandingUiEvent? = null,
        val isError: Boolean = false
    ) : LandingActionEvent
}