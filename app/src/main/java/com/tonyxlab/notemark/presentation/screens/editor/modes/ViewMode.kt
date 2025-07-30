package com.tonyxlab.notemark.presentation.screens.editor.modes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.component.ExtendedFabButton
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.util.toCreatedOnMetaData
import com.tonyxlab.notemark.util.toLastEditedMetaData

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
