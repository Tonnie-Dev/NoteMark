package com.tonyxlab.notemark.presentation.screens.landing.handling

import com.tonyxlab.notemark.presentation.core.base.handling.UiEvent

sealed interface LandingUiEvent: UiEvent {

    data object GuestLogin: LandingUiEvent
    data object Login: LandingUiEvent


}