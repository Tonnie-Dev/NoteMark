@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.editor.modes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.component.ExtendedFabButton
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.InvisibleSpacer
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

            Box(modifier = detectGesturesModifier) {

                Column {

                    AnimatedTopAppBar(uiState = uiState, onEvent = onEvent)

                    MetaDataSection(
                            modifier = Modifier.fillMaxHeight(),
                            uiState = uiState
                    )
                }

                AnimatedExtendedFab(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
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

                AnimatedTopAppBar(
                        uiState = uiState,
                        onEvent = onEvent
                )

                Box {

                    MetaDataSection(
                            modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .fillMaxHeight(),
                            uiState = uiState
                    )

                    AnimatedExtendedFab(
                            uiState = uiState,
                            onEvent = onEvent,
                            modifier = Modifier
                    )
                }
            }
        }
    }
}


@Composable
private fun AnimatedTopAppBar(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val showUiElements = uiState.remainingSecs > 0
    val density = LocalDensity.current
    var componentSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier) {

        if (!showUiElements && componentSize != IntSize.Zero) {
            InvisibleSpacer(componentSize = componentSize)
        }
        AnimatedVisibility(
                visible = showUiElements,
                enter = fadeIn(animationSpec = tween(durationMillis = 2_000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 2_000))
        ) {
            AppTopBar(
                    modifier = Modifier.onGloballyPositioned {
                        componentSize = with(density) {
                            it.size
                        }
                    }.clickable{

                        onEvent(EditorUiEvent.ExitEditor)
                    },
                    screenTitle = stringResource(id = R.string.topbar_text_all_notes),
                    onChevronIconClick = {

                        onEvent(EditorUiEvent.ExitEditor)

                    }
            )
        }
    }
}



@Composable
private fun BoxScope.AnimatedExtendedFab(
    uiState: EditorUiState,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val showUiElements = uiState.remainingSecs > 0

    val density = LocalDensity.current
    var componentSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
            modifier = modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(MaterialTheme.spacing.spaceTwelve)
    ) {

        if (!showUiElements && componentSize != IntSize.Zero) {
            InvisibleSpacer(componentSize = componentSize)
        }

        AnimatedVisibility(
                visible = showUiElements,
                enter = fadeIn(animationSpec = tween(durationMillis = 2_000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 2_000))
        ) {
            ExtendedFabButton(
                    modifier = Modifier.onGloballyPositioned {
                        componentSize = with(density) { it.size }
                    },
                    uiState = uiState,
                    onEvent = onEvent
            )
        }
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
