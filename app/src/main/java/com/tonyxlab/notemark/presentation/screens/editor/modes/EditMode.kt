package com.tonyxlab.notemark.presentation.screens.editor.modes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.components.AppDialog
import com.tonyxlab.notemark.presentation.screens.editor.component.EditableText
import com.tonyxlab.notemark.presentation.screens.editor.component.EditorAppBar
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState


@Composable
 fun EditModeScreenContent(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {

        EditorAppBar(onEvent = onEvent)

        Column(modifier = modifier.animateContentSize()) {

            EditableText(
                    textFieldState = uiState.titleNoteState.titleTextFieldState,
                    placeHolderText = uiState.titleNoteState.titlePlaceholderText,
                    isEditing = uiState.titleNoteState.isEditingTitle,
                    isTitle = true,
                    onEvent = onEvent


            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            EditableText(
                    textFieldState = uiState.contentNoteState.contentTextFieldState,
                    placeHolderText = uiState.contentNoteState.contentPlaceholderText,
                    isEditing = uiState.contentNoteState.isEditingContent,
                    isTitle = false,
                    onEvent = onEvent
            )
        }
    }

    if (uiState.showDialog) {

        AppDialog(
                onDismissRequest = { onEvent(EditorUiEvent.KeepEditing) },
                onConfirm = { onEvent(EditorUiEvent.DiscardChanges) },
                dialogTitle = stringResource(id = R.string.dialog_text_discard_changes),
                dialogText = stringResource(id = R.string.dialog_text_unsaved_changes),
                positiveButtonText = stringResource(id = R.string.dialog_text_discard),
                negativeButtonText = stringResource(id = R.string.dialog_text_keep_editing)
        )
    }
}

