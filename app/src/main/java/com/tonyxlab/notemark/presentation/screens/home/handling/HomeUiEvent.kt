package com.tonyxlab.notemark.presentation.screens.home.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class ClickNote(val noteId: Long) : HomeUiEvent
    data object CreateNewNote : HomeUiEvent
    data class LongPressNote(val noteId: Long) : HomeUiEvent
    data class ConfirmDeleteNote(val notedId: Long) : HomeUiEvent
    data object DismissDialog : HomeUiEvent
    data object ClickSettingsIcon : HomeUiEvent
}