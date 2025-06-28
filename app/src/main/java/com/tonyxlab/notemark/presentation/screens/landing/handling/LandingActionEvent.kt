package com.tonyxlab.notemark.presentation.screens.landing.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface LandingActionEvent : ActionEvent{

    data object NavigateToDemo: LandingActionEvent
    data object NavigateToLogin: LandingActionEvent

}