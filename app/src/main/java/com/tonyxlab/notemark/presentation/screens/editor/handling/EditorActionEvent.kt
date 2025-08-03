package com.tonyxlab.notemark.presentation.screens.editor.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface EditorActionEvent : ActionEvent {

    data object NavigateToHome : EditorActionEvent
    data object ShowDialog : EditorActionEvent
    data object EnterReadMode: EditorActionEvent
    data object ExitReadMode: EditorActionEvent
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val actionLabelRes: Int,
        val editorUiEvent: EditorUiEvent? = null,
        val isError: Boolean = false
    ) : EditorActionEvent
}
