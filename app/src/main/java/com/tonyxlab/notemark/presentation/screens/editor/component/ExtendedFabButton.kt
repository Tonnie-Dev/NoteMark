@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.Primary10

@Composable
fun ExtendedFabButton(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(MaterialTheme.spacing.spaceSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
    ) {
        val mode = uiState.editorMode

        IconFabButton(
                painter = painterResource(R.drawable.ic_edit_mode),
                semanticLabel = stringResource(id = R.string.cds_text_edit_mode),
                isActive = mode == EditorUiState.EditorMode.EditMode,
                onClickIcon = { onEvent(EditorUiEvent.EnterEditMode) }
        )

        IconFabButton(
                painter = painterResource(R.drawable.ic_read_mode),
                semanticLabel = stringResource(id = R.string.cds_text_read_mode),
                isActive = mode == EditorUiState.EditorMode.ReadMode,
                onClickIcon = { onEvent(EditorUiEvent.EnterReadMode) }
        )

    }
}

@Composable
private fun IconFabButton(
    painter: Painter,
    semanticLabel: String,
    isActive: Boolean,
    onClickIcon: () -> Unit,

    ) {

    val containerColor = if (isActive) Primary10 else Color.Unspecified
    val tintColor =
        if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    Box(
            modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(color = containerColor)
                    .padding(MaterialTheme.spacing.spaceExtraSmall)
                    .clickable { onClickIcon() },

            ) {
        Icon(
                modifier = Modifier
                        .size(MaterialTheme.spacing.spaceTen * 3)
                        .padding(MaterialTheme.spacing.spaceExtraSmall),
                contentDescription = semanticLabel,
                painter = painter,
                tint = tintColor
        )

    }
}


@PreviewLightDark
@Composable
private fun ExtendedFabButton_Preview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.spacing.spaceMedium
                )
        ) {

            ExtendedFabButton(
                    uiState = EditorUiState(),
                    onEvent = {}
            )
            ExtendedFabButton(
                    uiState = EditorUiState(editorMode = EditorUiState.EditorMode.EditMode),
                    onEvent = {}
            )
            ExtendedFabButton(
                    uiState = EditorUiState(editorMode = EditorUiState.EditorMode.ReadMode),
                    onEvent = {}
            )
        }
    }
}