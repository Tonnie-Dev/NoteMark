package com.tonyxlab.notemark.presentation.screens.settings.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface SettingsUiEvent: UiEvent{

    data object LogOut: SettingsUiEvent
    data object ExitSettings: SettingsUiEvent
}