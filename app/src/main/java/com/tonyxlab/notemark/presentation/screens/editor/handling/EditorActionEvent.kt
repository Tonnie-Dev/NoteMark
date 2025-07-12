package com.tonyxlab.notemark.presentation.screens.editor.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface EditorActionEvent: ActionEvent{

    data object NavigateToHome: EditorActionEvent
    data object ShowDialogue: EditorActionEvent
}
