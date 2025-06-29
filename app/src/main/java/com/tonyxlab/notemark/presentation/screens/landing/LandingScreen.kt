@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

package com.tonyxlab.notemark.presentation.screens.landing

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingActionEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiEvent
import com.tonyxlab.notemark.presentation.screens.landing.handling.LandingUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel


@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    navOperations: NavOperations,
    viewModel: LandingViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(true)
    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            actionEventHandler = { _, actionEvent ->
                when (actionEvent) {
                    LandingActionEvent.NavigateToGetStarted -> navOperations.navigateToSignupScreenDestination()
                    LandingActionEvent.NavigateToLogin -> navOperations.navigateToLoginScreenDestination()
                }
            }) {

        LandingScreenContent(
                modifier = modifier.fillMaxSize(),
                uiState = it,
                onEvent = viewModel::onEvent
        )

    }
}

@Composable
fun LandingScreenContent(
    uiState: LandingUiState,
    onEvent: (LandingUiEvent) -> Unit,
    modifier: Modifier = Modifier

) {
    Box(modifier = modifier) {

        Image(
                modifier = Modifier
                        .fillMaxWidth(), painter = painterResource(R.drawable.landing_image),
                contentDescription = null,
                contentScale = ContentScale.Crop
        )

        Surface(
                modifier = Modifier
                        .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.background,
                shape = getClippingShape()
        ) {

            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                            .padding(top = MaterialTheme.spacing.spaceLarge)
                            .padding(bottom = MaterialTheme.spacing.spaceMedium)
            ) {

                Header(
                        title = R.string.header_text_your_notes,
                        subTitle = R.string.cap_text_capture_thoughts
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTen * 4))

                AppButton(
                        buttonText = stringResource(id = R.string.btn_text_get_started),
                        onClick = { onEvent(LandingUiEvent.GetStarted) }
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
}

@PreviewLightDark
@Composable
private fun LandingScreenContentPreview() {

    NoteMarkTheme {
        LandingScreenContent(
                modifier = Modifier,
                uiState = LandingUiState(),
                onEvent = {}
        )
    }
}

