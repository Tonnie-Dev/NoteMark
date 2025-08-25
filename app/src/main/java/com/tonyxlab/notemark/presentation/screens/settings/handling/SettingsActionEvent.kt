package com.tonyxlab.notemark.presentation.screens.settings.handling

import androidx.annotation.StringRes
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface SettingsActionEvent : ActionEvent {

    data object ExitSettings : SettingsActionEvent
    data object Logout : SettingsActionEvent
    data object ShowSyncIntervalContextMenu : SettingsActionEvent
    //data object ShowDialog: SettingsActionEvent
 /*   data class ShowDialog(
        @StringRes val title: Int,
        @StringRes val message: Int,
        @StringRes val positiveText: Int,
        @StringRes val negativeText: Int = R.string.dialog_text_cancel,
        val showNegativeButton: Boolean = false
    ) : SettingsActionEvent*/

    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        @StringRes val labelRes: Int,
        val isError: Boolean = false,
        val settingsActionEvent: SettingsUiEvent? = null
    ) : SettingsActionEvent
}
