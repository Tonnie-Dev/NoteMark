package com.tonyxlab.notemark.presentation.screens.landing

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingActionEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiState

typealias LandingBaseVieModel = BaseViewModel<LandingUiState, LandingUiEvent, LandingActionEvent>

class LandingViewModel : LandingBaseVieModel(){
    override val initialState: LandingUiState
        get() = LandingUiState()

    override fun onEvent(event: LandingUiEvent) {
     when(event){
         LandingUiEvent.GetStarted -> getStarted()
         LandingUiEvent.Login -> login()
     }
    }



    private fun login() {
       sendActionEvent(LandingActionEvent.NavigateToLogin)
    }

    private fun getStarted() {
        sendActionEvent(LandingActionEvent.NavigateToGetStarted)
    }

}