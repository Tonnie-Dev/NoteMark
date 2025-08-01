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
import com.tonyxlab.notemark.presentation.screens.editor.modes.EditModeScreenContent
import com.tonyxlab.notemark.presentation.screens.editor.modes.ViewModeScreenContent
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


