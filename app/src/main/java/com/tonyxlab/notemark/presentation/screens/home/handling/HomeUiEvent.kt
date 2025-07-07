package com.tonyxlab.notemark.presentation.screens.home.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface HomeUiEvent: UiEvent{

    data class ClickNote(val noteId:Long): HomeUiEvent
    data object CreateNewNote: HomeUiEvent
}