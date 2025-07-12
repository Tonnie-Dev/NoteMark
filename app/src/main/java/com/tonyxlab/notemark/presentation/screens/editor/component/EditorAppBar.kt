package com.tonyxlab.notemark.presentation.screens.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.textButtonStyle

@Composable
fun EditorAppBar(
    modifier: Modifier = Modifier,
    onEvent: (EditorUiEvent) -> Unit
) {

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.spaceSmall)
                    .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onEvent(EditorUiEvent.CancelEditor) }) {

            Icon(
                    modifier = Modifier.size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.cds_text_cancel),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TextButton(onClick = { onEvent(EditorUiEvent.SaveNote) }) {

            Text(
                    text = stringResource(id = R.string.txt_btn_save_note),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.textButtonStyle
            )
        }

    }
}

@PreviewLightDark
@Composable
private fun EditorAppBar_Preview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
        ) {

            EditorAppBar { }
        }
    }
}
