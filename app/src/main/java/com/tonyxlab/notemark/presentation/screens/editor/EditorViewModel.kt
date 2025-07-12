package com.tonyxlab.notemark.presentation.screens.editor

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState

typealias EditorBaseViewModel = BaseViewModel<EditorUiState, EditorUiEvent, EditorActionEvent>

class EditorViewModel : EditorBaseViewModel() {

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

    private fun editNoteTitle() {

        updateState {
            it.copy(
                    titleNoteState = EditorUiState.TitleNoteState(isEditingTitle = true),
                    contentNoteState = EditorUiState.ContentNoteState(isEditingContent = false)
            )
        }
    }

    private fun editNoteContent() {

        updateState {
            it.copy(
                    titleNoteState = EditorUiState.TitleNoteState(isEditingTitle = false),
                    contentNoteState = EditorUiState.ContentNoteState(isEditingContent = true)
            )
        }
    }

    private fun saveNote() {

    }

    private fun cancelNoteEdit() {

    }
}
