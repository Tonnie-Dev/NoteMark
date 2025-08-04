@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor.modes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
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

@Composable
fun ReadModeScreenContent(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    val detectGesturesModifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    onEvent(EditorUiEvent.ToggledReadModeComponentVisibility)

                }

            }
            .fillMaxSize()

    when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> {

            Box(modifier = Modifier.then(detectGesturesModifier)) {
                Column {
                    AnimatedTopAppBar(uiState = uiState, onEvent = onEvent)

                    MetaDataSection(
                            modifier = Modifier.fillMaxHeight(),
                            uiState = uiState
                    )
                }

                AnimatedExtendedFab(
                        uiState = uiState,
                        onEvent = onEvent, modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(MaterialTheme.spacing.spaceTwelve)
                )
            }
        }

        DeviceType.MOBILE_LANDSCAPE,
        DeviceType.TABLET_PORTRAIT,
        DeviceType.TABLET_LANDSCAPE,
        DeviceType.DESKTOP
            -> {

            Row(
                    modifier = Modifier
                            .then(detectGesturesModifier)
                            .windowInsetsPadding(WindowInsets.displayCutout)
                            .padding(start = 60.dp)
                            .padding(end = 176.dp),
                    verticalAlignment = Alignment.Top
            ) {

                AnimatedTopAppBar(uiState = uiState, onEvent = onEvent)


                Box {

                    MetaDataSection(
                            modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .fillMaxHeight(),
                            uiState = uiState
                    )
                    AnimatedExtendedFab(
                            uiState = uiState,
                            onEvent = onEvent, modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .padding(MaterialTheme.spacing.spaceMedium)

                    )
                }

            }
        }
    }
}


@Composable
private fun AnimatedExtendedFab(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val showUiElements = uiState.remainingSecs > 0
    AnimatedVisibility(
            modifier = modifier,
            visible = showUiElements,

            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        ExtendedFabButton(

                uiState = uiState,
                onEvent = onEvent
        )


    }
}

@Composable
private fun AnimatedTopAppBar(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit
) {
    val showUiElements = uiState.remainingSecs > 0
    AnimatedVisibility(
            visible = showUiElements,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        AppTopBar(
                screenTitle = stringResource(id = R.string.topbar_text_all_notes),
                onChevronIconClick = {
                    onEvent(EditorUiEvent.ExitEditor)
                }
        )

    }
}


@PreviewScreenSizes
@Composable
private fun ReadModeScreenContent_Preview() {
    val content = generateLoremIpsum(11)

    NoteMarkTheme {
        ReadModeScreenContent(
                uiState = EditorUiState(
                        remainingSecs = 5,
                        contentNoteState = EditorUiState.ContentNoteState(
                                contentTextFieldState = TextFieldState(initialText = content)
                        )
                ),
                onEvent = {}
        )

    }

}
