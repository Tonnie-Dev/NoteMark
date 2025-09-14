@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.landing

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.notemark.presentation.core.components.SnackbarController
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingActionEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiState
import com.tonyxlab.notemark.presentation.theme.LandingPageBackground
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    navOperations: NavOperations,
    viewModel: LandingViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(true)
    val snackbarController = SnackbarController<LandingUiEvent>()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
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
            actionEventHandler = { _, action ->
                when (action) {
                    LandingActionEvent.NavigateToGetStarted -> navOperations.navigateToSignupScreenDestination()
                    LandingActionEvent.NavigateToLogin -> navOperations.navigateToLoginScreenDestination()
                    LandingActionEvent.NavigateToHome -> navOperations.navigateToHomeScreenDestination()
                    is LandingActionEvent.ShowSnackbar -> {

                        snackbarController.showSnackbar(
                                message = context.getString(action.messageRes),
                                actionLabel = context.getString(action.actionLabelRes),
                                actionEvent = action.landingUiEvent
                        )
                    }
                }
            }
    ) {
        LandingScreenContent(
                modifier = modifier,
                uiState = it,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun LandingScreenContent(
    uiState: LandingUiState,
    onEvent: (LandingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)
    val rootModifier = modifier.fillMaxSize()

    when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> {
            Box(modifier = rootModifier) {
                Image(
                        modifier = Modifier
                                .fillMaxWidth(),
                        painter = painterResource(R.drawable.landing_image_1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                )

                LoginPanel(
                        modifier = Modifier.clip(getClippingShape()),
                        uiState = uiState,
                        onEvent = onEvent,
                        alignment = Alignment.BottomCenter,
                        startPadding = MaterialTheme.spacing.spaceMedium,
                        endPadding = MaterialTheme.spacing.spaceMedium,
                        topPadding = MaterialTheme.spacing.spaceLarge,
                        bottomPadding = MaterialTheme.spacing.spaceTen * 4
                )
            }
        }

        DeviceType.MOBILE_LANDSCAPE -> {
            Box(
                    modifier = rootModifier.background(color = LandingPageBackground)
            ) {
                Image(
                        modifier = Modifier.fillMaxHeight(),
                        painter = painterResource(R.drawable.landing_image_2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                )

                LoginPanel(
                        modifier = Modifier.clip(
                                RoundedCornerShape(
                                        topStart = MaterialTheme.spacing.spaceTen,
                                        bottomStart = MaterialTheme.spacing.spaceTen
                                )
                        ),
                        uiState = uiState,
                        onEvent = onEvent,
                        alignment = Alignment.CenterEnd,
                        widthFraction = .5f,
                        startPadding = MaterialTheme.spacing.spaceTen * 6,
                        endPadding = MaterialTheme.spacing.spaceTen * 4,
                        topPadding = MaterialTheme.spacing.spaceTen * 4,
                        bottomPadding = MaterialTheme.spacing.spaceTen * 4
                )
            }
        }

        DeviceType.TABLET_PORTRAIT,
        DeviceType.TABLET_LANDSCAPE,
        DeviceType.DESKTOP -> {

            Box(
                    modifier = rootModifier.background(color = LandingPageBackground)
            ) {

                Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.landing_image_1),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                )

                LoginPanel(
                        modifier = Modifier.clip(getClippingShape()),
                        uiState = uiState,
                        onEvent = onEvent,
                        alignment = Alignment.BottomCenter,
                        widthFraction = .8f,
                        startPadding = MaterialTheme.spacing.spaceTen * 6,
                        endPadding = MaterialTheme.spacing.spaceTen * 4,
                        topPadding = MaterialTheme.spacing.spaceTen * 4,
                        bottomPadding = MaterialTheme.spacing.spaceTen * 4
                )
            }
        }
    }
}

@Composable
private fun BoxScope.LoginPanel(
    uiState: LandingUiState,
    onEvent: (LandingUiEvent) -> Unit,
    alignment: Alignment,
    widthFraction: Float = 1f,
    startPadding: Dp = MaterialTheme.spacing.spaceMedium,
    endPadding: Dp = MaterialTheme.spacing.spaceMedium,
    topPadding: Dp = MaterialTheme.spacing.spaceMedium,
    bottomPadding: Dp = MaterialTheme.spacing.spaceMedium,
    modifier: Modifier = Modifier
) {
    Surface(
            modifier = modifier
                    .align(alignment),
            color = MaterialTheme.colorScheme.surfaceContainerLowest

    ) {
        Column(
                modifier = Modifier
                        .fillMaxWidth(widthFraction)
                        .padding(start = startPadding)
                        .padding(end = endPadding)
                        .padding(top = topPadding)
                        .padding(bottom = bottomPadding)

        ) {
            Header(
                    title = R.string.header_text_your_notes,
                    subTitle = R.string.cap_text_capture_thoughts
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTen * 4))

            AppButton(
                    buttonText = stringResource(id = R.string.btn_text_guest_login),
                    onClick = { onEvent(LandingUiEvent.GuestLogin) },
                    isLoading = uiState.loginStatus is Resource.Loading
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve))

            AppButton(
                    buttonText = stringResource(id = R.string.btn_text_login),
                    isOutlined = true,
                    onClick = { onEvent(LandingUiEvent.Login) }
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun LandingScreenContentPreview() {

    NoteMarkTheme {
        LandingScreenContent(
                uiState = LandingUiState(),
                onEvent = {}
        )
    }
}



