package com.tonyxlab.notemark.presentation.screens.settings

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState


typealias SettingsBaseViewModel = BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>
class SettingsViewModel: SettingsBaseViewModel(){
    override val initialState: SettingsUiState
        get() = SettingsUiState()

    override fun onEvent(event: SettingsUiEvent) {

        when(event){
            SettingsUiEvent.LogOut -> {}
            SettingsUiEvent.ExitSettings -> {
               sendActionEvent(SettingsActionEvent.NavigateBack)
            }
        }

    }
}