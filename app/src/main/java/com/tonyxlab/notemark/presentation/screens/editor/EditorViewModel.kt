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
import com.tonyxlab.notemark.domain.usecase.DeleteNoteUseCase
import com.tonyxlab.notemark.domain.usecase.GetNoteByIdUseCase
import com.tonyxlab.notemark.domain.usecase.UpsertNoteUseCase
import com.tonyxlab.notemark.navigation.Destinations.EditorScreenDestination
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.screens.editor.handling.toActiveNote
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
    private val deleteNoteUseCase: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle
) : EditorBaseViewModel() {

    private lateinit var oldNotePair: Pair<String, String>
    private lateinit var newNotePair: Pair<String, String>

    init {
        val navigationId = savedStateHandle.toRoute<EditorScreenDestination>().id
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
            EditorUiEvent.ShowDialog -> updateState { it.copy(showDialog = true) }
            EditorUiEvent.KeepEditing -> keepEditing()
            EditorUiEvent.DiscardChanges,
            EditorUiEvent.ExitWithSnackbar -> discardChanges()
        }
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
            populateOldNote(currentNoteItem)
            observeTitleAndContentFields(currentNoteItem)
        }
    }

    private fun populateOldNote(oldNote: NoteItem) {
        updateState {
            it.copy(
                    titleNoteState = currentState.titleNoteState.copy(
                            titleTextFieldState = buildTextFieldState(
                                    oldNote.title
                            )
                    ),
                    contentNoteState = currentState.contentNoteState.copy(
                            contentTextFieldState = buildTextFieldState(
                                    oldNote.content
                            )
                    ),
                    activeNote = oldNote.toActiveNote()
            )
        }
    }

    private fun observeTitleAndContentFields(oldNote: NoteItem) {

        launch {

            val titleSnapshotFlow =
                snapshotFlow { currentState.titleNoteState.titleTextFieldState.text }
                        .debounce(300)
                        .distinctUntilChanged()

            val contentSnapshotFlow =
                snapshotFlow { currentState.contentNoteState.contentTextFieldState.text }
                        .debounce(300)
                        .distinctUntilChanged()

            combine(titleSnapshotFlow, contentSnapshotFlow) { title, content ->
                initializeOldAndNewNotePairs(
                        oldNote = oldNote,
                        newNote = Pair(title, content)
                )
            }.collect()
        }
    }

    private fun initializeOldAndNewNotePairs(
        oldNote: NoteItem,
        newNote: Pair<CharSequence, CharSequence>
    ) {
        oldNotePair = Pair(
                first = oldNote.title,
                second = oldNote.content
        )

        newNotePair = Pair(
                first = newNote.first.toString(),
                second = newNote.second.toString()
        )

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

    private fun keepEditing() {
        updateState { it.copy(showDialog = false) }
    }

    private fun discardChanges() {
        updateState { it.copy(showDialog = false) }
        sendActionEvent(EditorActionEvent.NavigateToHome)
    }

    private fun saveNote() {

        if (!isNoteEdited()) {
            sendActionEvent(EditorActionEvent.NavigateToHome)
            return
        }

        val now = LocalDateTime.now()
        val noteItem = NoteItem(
                id = currentState.activeNote.id,
                title = currentState.titleNoteState.titleTextFieldState.text.toString(),
                content = currentState.contentNoteState.contentTextFieldState.text.toString(),
                createdOn = currentState.activeNote.createdOn,
                lastEditedOn = now
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
            deleteNoteUseCase(noteItem = noteItem)
        }
    }

    private fun handleEditorExit() {
        launchCatching(
                onError = {
                    sendActionEvent(
                            EditorActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_note_not_found,
                                    actionLabelRes = R.string.snack_text_exit,
                                    onActionClick = EditorUiEvent.ExitWithSnackbar
                            )
                    )
                }
        ) {
            val currentNoteItem = getNoteByIdUseCase(id = currentState.activeNote.id)

            if (currentNoteItem.isBlankNote()) {
                deleteNote(noteItem = currentNoteItem)
            } else if (isNoteEdited()) {

                sendActionEvent(EditorActionEvent.ShowDialog)
                return@launchCatching
            }

            sendActionEvent(EditorActionEvent.NavigateToHome)
        }
    }

    private fun buildTextFieldState(value: String): TextFieldState {
        return TextFieldState(initialText = value)
    }

    private fun isNoteEdited(): Boolean {
        return this::oldNotePair.isInitialized &&
                this::newNotePair.isInitialized &&
                oldNotePair != newNotePair
    }
}
