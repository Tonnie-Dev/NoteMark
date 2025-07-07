package com.tonyxlab.notemark.presentation.screens.editor.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface EditorUiEvent : UiEvent {

    data object CancelNote : EditorUiEvent
    data object SaveNote : EditorUiEvent
}