package com.tonyxlab.notemark.presentation.screens.settings.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

data class SettingsUiState(
    val id: Long = -1L,
    val syncMenuState: SyncMenuState = SyncMenuState()
) : UiState {

    @Stable
    data class SyncMenuState(
        val isMenuOpen: Boolean = false,
        val activeInterval: SyncInterval = SyncInterval.MANUAL,
        val intervals: List<SyncInterval> = SyncInterval.entries
    )


}
