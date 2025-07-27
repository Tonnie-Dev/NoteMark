@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.usecase.DeleteNoteUseCase
import com.tonyxlab.notemark.domain.usecase.GetAllNotesUseCase
import com.tonyxlab.notemark.domain.usecase.GetNoteByIdUseCase
import com.tonyxlab.notemark.domain.usecase.UpsertNoteUseCase
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDateTime

typealias HomeViewModelBaseClass = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val upsertNoteUseCase: UpsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : HomeViewModelBaseClass() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        updateUsername()
        retrieveSavedNotes()
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.CreateNewNote -> createNote()
            is HomeUiEvent.ClickNote -> editNote(event.noteId)
            is HomeUiEvent.LongPressNote -> showDialog(noteId = event.noteId)
            is HomeUiEvent.ConfirmDeleteNote -> confirmDelete(event.notedId)
            HomeUiEvent.DismissDialog -> dismissDialog()
            HomeUiEvent.ClickSettingsIcon -> navigateToSettings()
        }
    }

    private fun navigateToSettings() {
        sendActionEvent(HomeActionEvent.NavigateToSettingsScreen)
    }

    private fun showDialog(noteId: Long) {
        updateState { it.copy(showDialog = true) }
        sendActionEvent(HomeActionEvent.ShowDialog(noteId = noteId))
    }

    private fun dismissDialog() {
        updateState { it.copy(showDialog = false) }
    }

    private fun confirmDelete(noteId: Long) {
        deleteNote(noteId = noteId)
        updateState { it.copy(showDialog = false) }
    }

    private fun retrieveSavedNotes() {

        getAllNotesUseCase()
                .onEach { notes ->

                    updateState { it.copy(notes = notes) }
                }
                .launchIn(viewModelScope)
    }

    private fun updateUsername() {

        launch {
            if (authRepository.isSignedIn()) {
                val username = authRepository.getUserName() ?: return@launch
                updateState { it.copy(username = username) }
                Timber.tag("HomeViewModel")
                        .i("User is-signed-in value is: ${authRepository.isSignedIn()}")
            } else {
                sendActionEvent(HomeActionEvent.NavigateToLoginScreen)
            }
        }
    }

    private fun editNote(noteId: Long) {
        sendActionEvent(HomeActionEvent.NavigateToEditorScreen(noteId))
    }

    private fun createNote() {

        launchCatching(
                onError = {
                    sendActionEvent(
                            HomeActionEvent.ShowToast(
                                    messageRes = R.string.snack_text_note_not_saved
                            )
                    )
                }
        ) {

            val now = LocalDateTime.now()
            val newNote = NoteItem(
                    title = "New Note",
                    content = "",
                    createdOn = now,
                    lastEditedOn = now
            )
            val result = upsertNoteUseCase(noteItem = newNote)
            val noteId = result
            sendActionEvent(HomeActionEvent.NavigateToEditorScreen(noteId))
        }
    }

    private fun deleteNote(noteId: Long) {
        launchCatching(
                onError = {
                    sendActionEvent(
                            HomeActionEvent.ShowToast(
                                    messageRes = R.string.snack_text_note_not_found
                            )
                    )

                }
        ) {
            val noteItem = getNoteByIdUseCase(id = noteId)
            deleteNoteUseCase(noteItem = noteItem)
        }
    }
}