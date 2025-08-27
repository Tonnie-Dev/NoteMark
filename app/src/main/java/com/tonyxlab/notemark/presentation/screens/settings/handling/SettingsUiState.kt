package com.tonyxlab.notemark.presentation.screens.settings.handling

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

data class SettingsUiState(
    val id: Long = -1L,
    val syncMenuState: SyncMenuState = SyncMenuState(),
    val lastSyncTime: String = "",
    val isSyncing: Boolean = false,
    val isOnline: Boolean = true,
    val isLoggingOut: Boolean = false,
    val dialogState: DialogState = DialogState()
) : UiState {

    @Stable
    data class SyncMenuState(
        val isMenuOpen: Boolean = false,
        val activeInterval: SyncInterval = SyncInterval.MANUAL,
        val intervals: List<SyncInterval> = SyncInterval.entries
    )

    @Stable
    data class DialogState(
        val showDialog: Boolean = false,
        @StringRes val dialogTitle: Int = R.string.dialog_text_ok,
        @StringRes val dialogText: Int = R.string.dialog_text_ok,
        @StringRes val positiveButtonText: Int = R.string.dialog_text_ok,
        @StringRes val negativeButtonText: Int = R.string.blank_text,
        val dialogType: SettingsDialogType? = null
    )

}

sealed interface SettingsDialogType {
    data object NoInternet : SettingsDialogType
    data object SyncError : SettingsDialogType
    data class UnSyncedChanges(val pendingCount: Int? = null) : SettingsDialogType

}
