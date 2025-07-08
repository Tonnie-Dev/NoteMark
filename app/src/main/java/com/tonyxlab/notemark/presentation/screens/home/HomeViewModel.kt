@file:RequiresApi(Build.VERSION_CODES.O)
package com.tonyxlab.notemark.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiState
import java.time.LocalDateTime

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
            is HomeUiEvent.ClickNote -> editNote(event.noteId)
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

        launch {
            val newNote = NoteItem(
                    title = "New Note",
                    content = "",
                    createdOn = LocalDateTime.now()
            )

            val result = noteRepository.upsertNote(newNote)

            if (result is Resource.Success) {
                val noteId = result.data
                sendActionEvent(HomeActionEvent.NavigateToEditorScreen(noteId))
            } else {
                // Optional: show error snackbar or log
            }
        }
    }
}