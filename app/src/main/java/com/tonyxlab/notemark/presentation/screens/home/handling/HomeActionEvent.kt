package com.tonyxlab.notemark.presentation.screens.home.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface HomeActionEvent : ActionEvent {
    data class NavigateToEditorScreen(val noteId: Long) : HomeActionEvent
    data object NavigateToLoginScreen : HomeActionEvent
    data class ShowDialog(val noteId: Long) : HomeActionEvent
    data class ShowToast(@StringRes val messageRes: Int) : HomeActionEvent
}