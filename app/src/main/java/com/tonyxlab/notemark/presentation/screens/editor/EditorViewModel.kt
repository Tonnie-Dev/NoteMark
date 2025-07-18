@file:OptIn(FlowPreview::class)
@file:  RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.isBlankNote
import com.tonyxlab.notemark.domain.usecase.GetNoteByIdUseCase
import com.tonyxlab.notemark.domain.usecase.UpsertNoteUseCase
import com.tonyxlab.notemark.navigation.Destinations.EditorScreenDestination
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDateTime

typealias EditorBaseViewModel = BaseViewModel<EditorUiState, EditorUiEvent, EditorActionEvent>

class EditorViewModel(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val upsertNoteUseCase: UpsertNoteUseCase,
    savedStateHandle: SavedStateHandle
) : EditorBaseViewModel() {

    init {

        val navigationId = savedStateHandle.toRoute<EditorScreenDestination>().id
        updateNoteId(noteId = navigationId)

        observeTextFieldsInput()
        loadNote(navigationId)
    }

    override val initialState: EditorUiState
        get() = EditorUiState()

    override fun onEvent(event: EditorUiEvent) {

        when (event) {
            EditorUiEvent.EditNoteTitle -> editNoteTitle()
            EditorUiEvent.EditNoteContent -> editNoteContent()
            EditorUiEvent.SaveNote -> saveNote()
            EditorUiEvent.ExitEditor -> handleEditorExit()
        }
    }

    private fun updateNoteId(noteId: Long) {
        updateState { it.copy(noteId = noteId) }
    }

    private fun loadNote(noteId: Long) {

        launchCatching(onError = {

            sendActionEvent(
                    EditorActionEvent.ShowSnackbar(
                            messageRes = R.string.snack_text_note_not_found,
                            actionLabelRes = R.string.snack_text_exit
                    )
            )
        }
        ) {

            val currentNoteItem = getNoteByIdUseCase(id = noteId)

            updateState {
                it.copy(
                        titleNoteState = currentState.titleNoteState.copy(
                                titleTextFieldState = editTextField(
                                        currentNoteItem.title
                                )
                        ),
                        contentNoteState = currentState.contentNoteState.copy(
                                contentTextFieldState = editTextField(
                                        currentNoteItem.content
                                )
                        )
                )
            }
        }
    }


    private fun observeTextFieldsInput() {

        launch {

            val titleTextSnapshotFlow =
                snapshotFlow { currentState.titleNoteState.titleTextFieldState.text }
                        .debounce(timeoutMillis = 300)
                        .distinctUntilChanged()

            val contentTextSnapshotFlow =
                snapshotFlow { currentState.contentNoteState.contentTextFieldState.text }
                        .debounce(timeoutMillis = 300)
                        .distinctUntilChanged()

            combine(titleTextSnapshotFlow, contentTextSnapshotFlow) { title, content ->

                if (!title.isBlank() && !content.isBlank()) {
                    updatePlaceholderTexts(title = title.toString(), content = content.toString())
                }

            }.collect()

        }
    }

    private fun updatePlaceholderTexts(title: String, content: String) {

        updateState {
            it.copy(
                    titleNoteState = currentState.titleNoteState.copy(titlePlaceholderText = title),
                    contentNoteState = currentState.contentNoteState.copy(contentPlaceholderText = content)
            )
        }
    }

    private fun editNoteTitle() {

        updateState {
            it.copy(
                    titleNoteState = currentState.titleNoteState.copy(isEditingTitle = true),
                    contentNoteState = currentState.contentNoteState.copy(isEditingContent = false)
            )
        }
    }

    private fun editNoteContent() {

        updateState {
            it.copy(
                    titleNoteState = currentState.titleNoteState.copy(isEditingTitle = false),
                    contentNoteState = currentState.contentNoteState.copy(isEditingContent = true)
            )
        }
    }

    private fun saveNote() {

        val noteItem = NoteItem(
                title = currentState.titleNoteState.titleTextFieldState.text.toString(),
                content = currentState.contentNoteState.contentTextFieldState.text.toString(),
                createdOn = LocalDateTime.now()
        )

        upsertNote(noteItem = noteItem)
        sendActionEvent(EditorActionEvent.NavigateToHome)

    }


    private fun upsertNote(noteItem: NoteItem) {

        launchCatching(
                onError = {
                    sendActionEvent(
                            EditorActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_note_not_saved,
                                    actionLabelRes = R.string.snack_text_exit
                            )
                    )
                }
        ) {
            upsertNoteUseCase(noteItem = noteItem)
        }
    }

    private fun deleteNote(noteItem: NoteItem) {

        launchCatching(
                onError = {
                    sendActionEvent(
                            EditorActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_note_not_found,
                                    actionLabelRes = R.string.snack_text_exit
                            )
                    )
                }
        ) {
            deleteNote(noteItem = noteItem)
        }
    }




    private fun handleEditorExit() {

        launchCatching(
                onError = {
                    sendActionEvent(
                            EditorActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_note_not_found,
                                    actionLabelRes = R.string.snack_text_exit
                            )
                    )
                }
        ) {
            val currentNote = getNoteByIdUseCase(id = currentState.noteId)

            if (currentNote.isBlankNote()) {
                deleteNote(noteItem = currentNote)
            }
            sendActionEvent(EditorActionEvent.NavigateToHome)

        }
    }

    private fun editTextField(value: String): TextFieldState {
        return TextFieldState(initialText = value)
    }
}
