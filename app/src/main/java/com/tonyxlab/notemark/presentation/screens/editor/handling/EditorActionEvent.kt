package com.tonyxlab.notemark.presentation.screens.editor.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface EditorActionEvent : ActionEvent {

    data object NavigateToHome : EditorActionEvent
    data object ShowDialog : EditorActionEvent
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val actionLabelRes: Int,
        val onActionClick: EditorUiEvent? = null,
        val isError: Boolean = false
    ) : EditorActionEvent
}
