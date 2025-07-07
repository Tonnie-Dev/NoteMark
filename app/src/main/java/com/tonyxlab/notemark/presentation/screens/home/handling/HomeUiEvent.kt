package com.tonyxlab.notemark.presentation.screens.home.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface HomeUiEvent: UiEvent{

    data object ClickNote: HomeUiEvent
    data object CreateNewNote: HomeUiEvent
}