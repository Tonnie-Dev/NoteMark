@file:OptIn(FlowPreview::class)
@file:  RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.domain.model.isBlankNote
import com.tonyxlab.notemark.domain.timer.CountdownTimer
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
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
    private var timer: CountdownTimer? = null

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

            EditorUiEvent.EnterEditMode -> enterEditMode()
            EditorUiEvent.EnterReadMode -> enterReadMode()
            EditorUiEvent.ExitReadMode -> exitReadMode()
            EditorUiEvent.ToggledReadModeComponentVisibility -> toggleUiComponentsVisibility()
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

    private fun startTimer() {

        timer?.stop() //Clean up the old timer
        timer = CountdownTimer().also { timer ->
            timer.start()
            timer.remainingSecs.onEach { secs ->


                updateState { it.copy(remainingSecs = secs) }
            }
                    .launchIn(viewModelScope)
        }

    }

    private fun stopTimer() {

        timer?.stop()
        updateState { it.copy(remainingSecs = 0) }
    }

    private fun populateOldNote(oldNote: NoteItem) {
        updateState {
            it.copy(
                    titleNoteState = updateTitle(oldNote.title),
                    contentNoteState = updateContent(oldNote.content),
                    activeNote = oldNote.toActiveNote()
            )
        }
    }

    private fun toggleUiComponentsVisibility() {

        if (currentState.remainingSecs > 0) {

            stopTimer()
        } else {

            startTimer()
        }

    }

    private fun enterReadMode() {

        if (currentState.editorMode == EditorUiState.EditorMode.ReadMode) {
            exitReadMode()

        } else {
            startTimer()
            updateState { it.copy(editorMode = EditorUiState.EditorMode.ReadMode) }
            sendActionEvent(EditorActionEvent.EnterReadMode)
        }

    }

    private fun exitReadMode() {

        updateState { it.copy(editorMode = EditorUiState.EditorMode.ViewMode) }
        sendActionEvent(EditorActionEvent.ExitReadMode)

    }

    private fun enterViewMode() {
        updateState { it.copy(editorMode = EditorUiState.EditorMode.ViewMode) }

    }

    private fun enterEditMode() {
        exitReadMode()
        updateState { it.copy(editorMode = EditorUiState.EditorMode.EditMode) }

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

        if (this::oldNotePair.isInitialized) {
            updateState {
                it.copy(
                        titleNoteState = updateTitle(oldNotePair.first),
                        contentNoteState = updateContent(oldNotePair.second)
                )
            }
        }
        updateState { it.copy(showDialog = false) }
        enterViewMode()
    }

    private fun saveNote() {

        if (!isNoteEdited()) {
            enterViewMode()
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
        enterViewMode()
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

    private fun deleteNote(noteItem: NoteItem, queueDelete: Boolean = true) {

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
            deleteNoteUseCase(noteItem = noteItem, queueDelete =  queueDelete)
        }
    }


    private fun handleEditorExit() {
        launchCatching(
                onError = {
                    Timber.tag("EditorViewModel")
                            .i("HEE - Step 6 ${it.message}")
                    sendActionEvent(
                            EditorActionEvent.ShowSnackbar(
                                    messageRes = R.string.snack_text_note_not_found,
                                    actionLabelRes = R.string.snack_text_exit,
                                    editorUiEvent = EditorUiEvent.ExitWithSnackbar
                            )
                    )
                }
        ) {
            val currentNoteItem = getNoteByIdUseCase(id = currentState.activeNote.id)


            when (currentState.editorMode) {

                EditorUiState.EditorMode.ViewMode -> {

                    if (currentNoteItem.isBlankNote()) {
                        deleteNote(noteItem = currentNoteItem, queueDelete = false)
                    }
                    sendActionEvent(EditorActionEvent.NavigateToHome)
                    return@launchCatching
                }

                EditorUiState.EditorMode.ReadMode -> {
                    sendActionEvent(EditorActionEvent.ExitReadMode)
                    sendActionEvent(EditorActionEvent.NavigateToHome)
                    return@launchCatching
                }

                EditorUiState.EditorMode.EditMode -> {
                    if (isNoteEdited()) {
                        sendActionEvent(EditorActionEvent.ShowDialog)
                    } else {
                        enterViewMode()
                    }
                    return@launchCatching
                }

            }
        }

    }

    private fun buildTextFieldState(value: String): TextFieldState {

        return TextFieldState(initialText = value)

    }

    private fun updateTitle(value: String): EditorUiState.TitleNoteState {

        return currentState.titleNoteState.copy(
                titleTextFieldState = buildTextFieldState(value)
        )

    }

    private fun updateContent(value: String): EditorUiState.ContentNoteState {
        return currentState.contentNoteState.copy(
                contentTextFieldState = buildTextFieldState(value)
        )
    }

    private fun isNoteEdited(): Boolean {
        return this::oldNotePair.isInitialized &&
                this::newNotePair.isInitialized &&
                oldNotePair != newNotePair
    }
}
