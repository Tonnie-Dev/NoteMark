package com.tonyxlab.notemark.presentation.screens.settings.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface SettingsActionEvent : ActionEvent {


    data object ExitSettings : SettingsActionEvent
    data object Logout: SettingsActionEvent
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val labelRes: Int,
        val isError: Boolean = false,
        val settingsActionEvent: SettingsUiEvent? = null
    ) : SettingsActionEvent

}
