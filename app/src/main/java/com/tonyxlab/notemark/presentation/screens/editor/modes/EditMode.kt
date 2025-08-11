@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor.modes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.components.AppDialog
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.component.EditableText
import com.tonyxlab.notemark.presentation.screens.editor.component.EditorAppBar
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.textButtonStyle
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.generateLoremIpsum


@Composable
fun EditModeScreenContent(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> {

            Column {
                EditorAppBar(onEvent = onEvent)
                TitleAndContentEditBoxes(modifier, uiState, onEvent)
            }

        }

        DeviceType.MOBILE_LANDSCAPE -> {

            Row(
                    modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.displayCutout)
                            .statusBarsPadding(),
                    verticalAlignment = Alignment.Top
            ) {
                IconButton(
                        modifier = Modifier.weight(2f),
                        onClick = { onEvent(EditorUiEvent.ExitEditor) }) {

                    Icon(
                            modifier = Modifier.size(MaterialTheme.spacing.spaceTen * 2),
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.cds_text_cancel),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TitleAndContentEditBoxes(modifier.weight(6f), uiState, onEvent)
                TextButton(
                        modifier = Modifier.weight(2f),

                        onClick = { onEvent(EditorUiEvent.SaveNote) }) {

                    Text(
                            text = stringResource(id = R.string.txt_btn_save_note),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.textButtonStyle
                    )
                }
            }
        }

        DeviceType.TABLET_PORTRAIT, DeviceType.TABLET_LANDSCAPE, DeviceType.DESKTOP -> {

            Column {
                Row(
                        modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.displayCutout)
                                .statusBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                            onClick = { onEvent(EditorUiEvent.ExitEditor) }) {

                        Icon(
                                modifier = Modifier.size(MaterialTheme.spacing.spaceTen * 2),
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.cds_text_cancel),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(
                            onClick = { onEvent(EditorUiEvent.SaveNote) }) {

                        Text(
                                text = stringResource(id = R.string.txt_btn_save_note),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.textButtonStyle
                        )
                    }
                }

                TitleAndContentEditBoxes(
                        modifier = modifier.weight(6f),
                        uiState = uiState,
                        onEvent = onEvent
                )
            }
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

@Composable
private fun TitleAndContentEditBoxes(
    modifier: Modifier,
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit
) {
    Column(
            modifier = modifier
                    .animateContentSize()

    ) {

        Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium)) {
            EditableText(
                    textFieldState = uiState.titleNoteState.titleTextFieldState,
                    placeHolderText = uiState.titleNoteState.titlePlaceholderText,
                    isEditing = uiState.titleNoteState.isEditingTitle,
                    isTitle = true,
                    onEvent = onEvent
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium)) {
            EditableText(
                    textFieldState = uiState.contentNoteState.contentTextFieldState,
                    placeHolderText = uiState.contentNoteState.contentPlaceholderText,
                    isEditing = uiState.contentNoteState.isEditingContent,
                    isTitle = false,
                    onEvent = onEvent
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun ViewModeScreenContent_Preview() {
    val content = generateLoremIpsum(11)

    NoteMarkTheme {
        EditModeScreenContent(
                uiState = EditorUiState(
                        contentNoteState = EditorUiState.ContentNoteState(
                                contentTextFieldState = TextFieldState(initialText = content)
                        )
                ),
                onEvent = {}
        )

    }

}

