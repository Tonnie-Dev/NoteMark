package com.tonyxlab.notemark.presentation.screens.settings.handling

import com.tonyxlab.notemark.presentation.core.base.handling.ActionEvent

sealed interface SettingsActionEvent: ActionEvent{


    data object NavigateBack: SettingsActionEvent

}
