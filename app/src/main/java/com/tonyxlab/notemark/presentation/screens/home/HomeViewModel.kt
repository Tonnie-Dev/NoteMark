package com.tonyxlab.notemark.presentation.screens.home

import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiState

typealias HomeViewModelBaseClass = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val authRepository: AuthRepository, private val noteRepository: NoteRepository
) : HomeViewModelBaseClass() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        updateUsername()

    }

    override fun onEvent(event: HomeUiEvent) {

        when (event) {
            is HomeUiEvent.ClickNote -> editNote()
            HomeUiEvent.CreateNewNote -> createNote()
        }
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

    private fun editNote(noteId: Long) {
        sendActionEvent(HomeActionEvent.NavigateToEditorScreen(noteId))
    }


    private fun createNote() {

        sendActionEvent(HomeActionEvent.NavigateToEditorScreen)
    }
}