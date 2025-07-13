@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.component.EditableText
import com.tonyxlab.notemark.presentation.screens.editor.component.EditorAppBar
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditorScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = koinViewModel()
) {

    BaseContentLayout(
            viewModel = viewModel,
            actionEventHandler = { _, action ->

                when (action) {
                    EditorActionEvent.NavigateToHome -> {navOperations.navigateToHomeScreenDestination()}
                    EditorActionEvent.ShowDialogue -> {navOperations.navigateToHomeScreenDestination()}
                }
            }) {

        EditorScreenContent(
                modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
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

    Column {

        EditorAppBar(onEvent =onEvent)

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
}


@PreviewLightDark
@Composable
private fun EditorScreenContent_Preview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()

        ) {
            EditorScreenContent(uiState = EditorUiState(), onEvent = {})

        }
    }


}


