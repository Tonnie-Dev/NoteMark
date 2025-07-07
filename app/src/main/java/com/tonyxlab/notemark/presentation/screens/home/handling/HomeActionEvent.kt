package com.tonyxlab.notemark.presentation.screens.home.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface HomeActionEvent : ActionEvent {

    data  class NavigateToEditorScreen(val noteId: Long): HomeActionEvent
    object  NavigateToLoginScreen: HomeActionEvent
}