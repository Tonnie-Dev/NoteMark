package com.tonyxlab.notemark.presentation.screens.editor.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface EditorUiEvent : UiEvent {

    data object SaveNote: EditorUiEvent
    data object EditNoteTitle: EditorUiEvent
    data object EditNoteContent: EditorUiEvent
    data object ExitEditor: EditorUiEvent
    data object ExitWithSnackbar : EditorUiEvent

    data object KeepEditing : EditorUiEvent
    data object DiscardChanges : EditorUiEvent
    data object ShowDialog : EditorUiEvent

}