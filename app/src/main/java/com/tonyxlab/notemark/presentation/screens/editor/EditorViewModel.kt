@file:OptIn(FlowPreview::class)

package com.tonyxlab.notemark.presentation.screens.editor

import android.R.attr.text
import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

typealias EditorBaseViewModel = BaseViewModel<EditorUiState, EditorUiEvent, EditorActionEvent>

class EditorViewModel : EditorBaseViewModel() {
    init {
        observeTextFieldsInput()
    }


    override val initialState: EditorUiState
        get() = EditorUiState()

       override fun onEvent(event: EditorUiEvent) {

        when (event) {
            EditorUiEvent.EditNoteTitle -> editNoteTitle()
            EditorUiEvent.EditNoteContent -> editNoteContent()
            EditorUiEvent.SaveNote -> saveNote()
            EditorUiEvent.CancelEditor -> cancelNoteEdit()
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

                if (!title.isBlank() && !content.isBlank()){
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

    }

    private fun cancelNoteEdit() {

    }
}
