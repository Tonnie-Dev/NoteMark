package com.tonyxlab.notemark.presentation.screens.editor.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.presentation.core.base.handling.UiState


@Stable
data class EditorUiState(
    val noteId: Long = -1L,
    val titleNoteState: TitleNoteState = TitleNoteState(),
    val contentNoteState: ContentNoteState = ContentNoteState()
) : UiState {

    @Stable
    data class TitleNoteState(
        val titlePlaceholderText: String = "Note Title",
        val isEditingTitle: Boolean = true,
        val titleTextFieldState: TextFieldState = TextFieldState("New Note")
    )

    @Stable
    data class ContentNoteState(
        val contentPlaceholderText: String = "Tap to enter note content",
        val isEditingContent: Boolean = false,
        val contentTextFieldState: TextFieldState = TextFieldState()
    )
}