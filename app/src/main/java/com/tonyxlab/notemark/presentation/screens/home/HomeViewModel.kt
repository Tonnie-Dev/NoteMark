package com.tonyxlab.notemark.presentation.screens.home

import com.tonyxlab.notemark.data.datastore.TokenStorage
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiState
import timber.log.Timber

typealias HomeViewModelBaseClass = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(private val authRepository: AuthRepository) : HomeViewModelBaseClass() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        updateUsername()

        launch {

            val token = TokenStorage.getAccessToken()
                    ?.take(5) ?: "Empty!!"
            Timber.tag("Ngui")
                    .i("Token is: $token")
            updateState { it.copy(username = token) }

        }
    }

    override fun onEvent(event: HomeUiEvent) {

        TODO("Not yet implemented")
    }

    private fun updateUsername() {

        launch {
            if (authRepository.isSignedIn()) {

                val username = authRepository.getUserName() ?: return@launch
                updateState { it.copy(username = username) }
            } else {
                sendActionEvent(HomeActionEvent.NavigateToLoginScreen)
            }

        }

    }
}