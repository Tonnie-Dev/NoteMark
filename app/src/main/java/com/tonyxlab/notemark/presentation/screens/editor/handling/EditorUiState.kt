@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor.handling

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.presentation.core.base.handling.UiState
import java.time.LocalDateTime

@Stable
data class EditorUiState(
    val activeNote: ActiveNote = ActiveNote(),
    val titleNoteState: TitleNoteState = TitleNoteState(),
    val contentNoteState: ContentNoteState = ContentNoteState(),
    val showDialog: Boolean = false,
    val editorMode: EditorMode = EditorMode.ViewMode,
    val remainingSecs: Int = 0
) : UiState {

    @Stable
    data class TitleNoteState(
        val titlePlaceholderText: String = "Note Title",
        val isEditingTitle: Boolean = true,
        val titleTextFieldState: TextFieldState = TextFieldState(initialText = "New Note")
    )

    @Stable
    data class ContentNoteState(
        val contentPlaceholderText: String = "Tap to enter note content",
        val isEditingContent: Boolean = false,
        val contentTextFieldState: TextFieldState = TextFieldState()
    )

    @Stable
    data class ActiveNote(
        val id: Long = -1L,
        val createdOn: LocalDateTime = LocalDateTime.now(),
        val lastEditedOn: LocalDateTime = LocalDateTime.now()
    )

    @Stable
    enum class EditorMode { ViewMode, ReadMode, EditMode }

}

fun NoteItem.toActiveNote(): EditorUiState.ActiveNote = EditorUiState.ActiveNote(
        id = id,
        createdOn = createdOn,
        lastEditedOn = lastEditedOn
)