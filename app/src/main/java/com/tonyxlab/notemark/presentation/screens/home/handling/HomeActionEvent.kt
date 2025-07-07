package com.tonyxlab.notemark.presentation.screens.home.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface HomeActionEvent : ActionEvent {

    object NavigateToEditorScreen: HomeActionEvent
    object  NavigateToLoginScreen: HomeActionEvent
}