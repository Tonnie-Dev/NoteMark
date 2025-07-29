@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppDialog
import com.tonyxlab.notemark.presentation.core.components.AppSnackbarHost
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.notemark.presentation.core.components.SnackbarController
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.component.EditableText
import com.tonyxlab.notemark.presentation.screens.editor.component.EditorAppBar
import com.tonyxlab.notemark.presentation.screens.editor.component.ExtendedFabButton
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.toCreatedOnMetaData
import com.tonyxlab.notemark.util.toLastEditedMetaData
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditorScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val snackbarController = SnackbarController<EditorUiEvent>()
    val snackbarHostState = remember { SnackbarHostState() }
    ShowAppSnackbar(
            triggerId = snackbarController.triggerId,
            snackbarHostState = snackbarHostState,
            message = snackbarController.message,
            actionLabel = snackbarController.actionLabel,
            onActionClick = {
                snackbarController.actionEvent?.let { viewModel.onEvent(it) }
            },
            onDismiss = {
                snackbarController.dismissSnackbar()
            }

    )
    BaseContentLayout(
            viewModel = viewModel,
            snackbarHost = { AppSnackbarHost(snackbarHostState = snackbarHostState) },
            actionEventHandler = { _, action ->

                when (action) {
                    EditorActionEvent.NavigateToHome -> {
                        navOperations.popBackStack()
                    }

                    is EditorActionEvent.ShowSnackbar -> {
                        snackbarController.showSnackbar(
                                message = context.getString(action.messageRes),
                                actionLabel = context.getString(action.actionLabelRes),
                                actionEvent = action.editorUiEvent
                        )

                    }

                    EditorActionEvent.ShowDialog -> {
                        viewModel.onEvent(EditorUiEvent.ShowDialog)
                    }
                }
            },
            onBackPressed = {
                viewModel.onEvent(EditorUiEvent.ExitEditor)
            }
    ) {

        EditorScreenContent(
                modifier = modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                uiState = it,
                onEvent = viewModel::onEvent
        )

    }
}


@Composable
fun EditorScreenContent(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState.editorMode) {
        EditorUiState.EditorMode.ViewMode -> ViewModeScreenContent(
                modifier = modifier,
                uiState = uiState,
                onEvent = onEvent,
        )


        EditorUiState.EditorMode.EditMode -> EditModeScreenContent(
                modifier = modifier,
                uiState = uiState,
                onEvent = onEvent,
        )

        EditorUiState.EditorMode.ReadMode -> ViewModeScreenContent(
                modifier = modifier,
                uiState = uiState,
                onEvent = onEvent,
        )
    }

}

@Composable
private fun EditModeScreenContent(
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

@Composable
fun ViewModeScreenContent(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val title = uiState.titleNoteState.titleTextFieldState.text.toString()
    val content = uiState.contentNoteState.contentTextFieldState.text.toString()
    val createdOn = uiState.activeNote.createdOn.toCreatedOnMetaData()
    val lastEditedOn = uiState.activeNote.lastEditedOn.toLastEditedMetaData()

    val contentPaddingModifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
            .padding(vertical = MaterialTheme.spacing.spaceTen * 2)

    Box {


        Column(modifier = modifier.fillMaxSize()) {


            AppTopBar(
                    screenTitle = stringResource(id = R.string.topbar_text_all_notes),
                    onChevronIconClick = {
                        onEvent(EditorUiEvent.ExitEditor)
                    }
            )

            Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .then(contentPaddingModifier)
            ) {

                Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .then(contentPaddingModifier)
            ) {


                Column(modifier = Modifier.weight(1f)) {
                    Text(
                            stringResource(id = R.string.meta_text_created_on),
                            style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )
                    Text(
                            text = createdOn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                            )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                            stringResource(id = R.string.meta_text_edited_on),
                            style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )
                    Text(
                            text = lastEditedOn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                            )
                    )
                }

            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                            .padding(vertical = MaterialTheme.spacing.spaceTen * 2)
            ) {


                Text(
                        text = content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                )

            }
        }

        ExtendedFabButton(
                modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(MaterialTheme.spacing.spaceTwelve),
                uiState = uiState,
                onEvent = onEvent
        )
    }
}


@PreviewLightDark
@Composable
private fun EditorScreenContentView_Preview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .fillMaxSize()

        ) {
            EditorScreenContent(uiState = EditorUiState(), onEvent = {})

        }
    }


}


@PreviewLightDark
@Composable
private fun EditorScreenContentEdit_Preview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .fillMaxSize()

        ) {
            EditorScreenContent(
                    uiState = EditorUiState(editorMode = EditorUiState.EditorMode.EditMode),
                    onEvent = {})

        }
    }


}


