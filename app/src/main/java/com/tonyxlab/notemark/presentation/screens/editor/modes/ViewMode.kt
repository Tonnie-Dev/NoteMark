@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor.modes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.component.ExtendedFabButton
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.generateLoremIpsum
import com.tonyxlab.notemark.util.toCreatedOnMetaData
import com.tonyxlab.notemark.util.toLastEditedMetaData

@Composable
fun ViewModeScreenContent(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    when (deviceType) {
        DeviceType.MOBILE_PORTRAIT,
        DeviceType.TABLET_PORTRAIT,
        DeviceType.TABLET_LANDSCAPE,
        DeviceType.DESKTOP -> {

            Box {
                Column {
                    AppTopBar(
                            screenTitle = stringResource(id = R.string.topbar_text_all_notes),
                            onChevronIconClick = {
                                onEvent(EditorUiEvent.ExitEditor)
                            }
                    )

                    MetaDataSection(
                            modifier = Modifier.fillMaxHeight(),
                            uiState = uiState
                    )
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

        DeviceType.MOBILE_LANDSCAPE -> {

            Row(
                    modifier = Modifier
                            .windowInsetsPadding(WindowInsets.displayCutout)
                            .padding(start = 60.dp)
                            .padding(end = 176.dp),
                    verticalAlignment = Alignment.Top
            ) {

                AppTopBar(height = 40.dp,
                        screenTitle = stringResource(id = R.string.topbar_text_all_notes),
                        onChevronIconClick = {
                            onEvent(EditorUiEvent.ExitEditor)
                        }
                )

                Box {
                    MetaDataSection(
                            modifier = modifier
                                    .align(alignment = Alignment.TopStart)
                                    .fillMaxHeight(),
                            uiState = uiState
                    )

                    ExtendedFabButton(
                            modifier = Modifier
                                    .align(alignment = Alignment.BottomCenter)
                                    .padding(MaterialTheme.spacing.spaceTwelve),
                            uiState = uiState,
                            onEvent = onEvent
                    )
                }

            }
        }


    }
}


@Composable
fun MetaDataSection(
    uiState: EditorUiState,
    modifier: Modifier = Modifier
) {

    val title = uiState.titleNoteState.titleTextFieldState.text.toString()
    val content = uiState.contentNoteState.contentTextFieldState.text.toString()
    val createdOn = uiState.activeNote.createdOn.toCreatedOnMetaData()
    val lastEditedOn = uiState.activeNote.lastEditedOn.toLastEditedMetaData()

    val contentPaddingModifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
            .padding(vertical = MaterialTheme.spacing.spaceTen * 2)

    Column(modifier = modifier.fillMaxWidth()) {

        Row(
                modifier = Modifier
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
}


@PreviewScreenSizes
@Composable
private fun ViewModeScreenContent_Preview() {
    val content = generateLoremIpsum(11)

    NoteMarkTheme {
        ViewModeScreenContent(
                uiState = EditorUiState(
                        contentNoteState = EditorUiState.ContentNoteState(
                                contentTextFieldState = TextFieldState(initialText = content)
                        )
                ),
                onEvent = {}
        )

    }

}

@PreviewScreenSizes
@Composable
private fun MetaDataSection_Preview() {
    val content = generateLoremIpsum(56)

    NoteMarkTheme {
        MetaDataSection(
                uiState = EditorUiState(
                        contentNoteState = EditorUiState.ContentNoteState(
                                contentTextFieldState = TextFieldState(initialText = content)
                        )
                )
        )

    }

}
