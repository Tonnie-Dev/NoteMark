package com.tonyxlab.notemark.presentation.screens.landing

import com.tonyxlab.notemark.BuildConfig
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.Credentials
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingActionEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiState
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent

typealias LandingBaseVieModel = BaseViewModel<LandingUiState, LandingUiEvent, LandingActionEvent>

class LandingViewModel(
    private val authRepository: AuthRepository
) : LandingBaseVieModel() {
    override val initialState: LandingUiState
        get() = LandingUiState()

    override fun onEvent(event: LandingUiEvent) {
        when (event) {
            LandingUiEvent.GuestLogin -> guestLogin()
            LandingUiEvent.Login -> login()
        }
    }

    private fun login() {
        sendActionEvent(LandingActionEvent.NavigateToLogin)
    }

    private fun guestLogin() {
        launchCatching(
                onStart = {

                    updateState { it.copy(loginStatus = Resource.Loading) }
                },
                onCompletion = {
                    updateState { it.copy(loginStatus = Resource.Empty) }
                },
                onError = { e ->

                    updateState { it.copy(loginStatus = Resource.Error(exception = e as Exception)) }
                    sendActionEvent(
                            actionEvent = LandingActionEvent.ShowSnackbar(

                                    messageRes = R.string.snack_text_login_failed,
                                    actionLabelRes = R.string.snack_text_retry,
                                    landingUiEvent = LandingUiEvent.GuestLogin
                            )
                    )

                }
        ) {

            val credentials = Credentials(
                    email = BuildConfig.DEFAULT_USER_EMAIL,
                    password = BuildConfig.DEFAULT_USER_PASSWORD
            )
            val result = authRepository.login(credentials)

            when(result){

                is Resource.Success -> {

                    updateState { it.copy(loginStatus = Resource.Success(result.data)) }
                    sendActionEvent(actionEvent = LandingActionEvent.NavigateToHome)
                }

                is Resource.Error -> throw  result.exception

                else -> Unit
            }

        }
    }

}