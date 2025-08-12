@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.AppSnackbarHost
import com.tonyxlab.notemark.presentation.core.components.AppTextButton
import com.tonyxlab.notemark.presentation.core.components.AppTextField
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.notemark.presentation.core.components.rememberSnackbarController
import com.tonyxlab.notemark.presentation.core.utils.SupportText
import com.tonyxlab.notemark.presentation.core.utils.eyeIcon
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navOperations: NavOperations,
    viewModel: LoginViewModel = koinViewModel()
) {

    SetStatusBarIconsColor(darkIcons = false)

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackbarController = rememberSnackbarController<LoginUiEvent>()
    val snackbarHostState = remember { SnackbarHostState() }

    ShowAppSnackbar(
            triggerId = snackbarController.triggerId,
            snackbarHostState = snackbarHostState,
            message = snackbarController.message,
            actionLabel = snackbarController.actionLabel,
            onActionClick = { snackbarController.actionEvent?.let { viewModel.onEvent(it) } },
            onDismiss = {
                snackbarController.dismissSnackbar()
            }
    )

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            snackbarHost = {
                AppSnackbarHost(
                        snackbarHostState = snackbarHostState,
                        isError = state.loginStatus is Resource.Error
                )
            },
            actionEventHandler = { _, action ->
                when (action) {
                    LoginActionEvent.NavigateToHomeScreen -> {
                        navOperations.navigateToHomeScreenDestination()
                    }

                    LoginActionEvent.NavigateToSignupScreen -> {
                        navOperations.navigateToSignupScreenDestination()
                    }

                    is LoginActionEvent.ShowSnackbar -> {
                        snackbarController.showSnackbar(
                                message = context.getString(action.messageRes),
                                actionLabel = context.getString(action.actionLabelRes),
                                actionEvent = action.loginUiEvent
                        )
                    }
                }
            }

    ) { state ->
        LoginScreenContent(
                modifier = modifier,
                uiState = state,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    val rootModifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()

    when (deviceType) {

        DeviceType.MOBILE_PORTRAIT -> {
            Box(modifier = rootModifier
                    .padding(top = MaterialTheme.spacing.spaceTen * 4)) {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        shape = getClippingShape()
                ) {
                    Column(
                            modifier = Modifier
                                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                                    .padding(vertical = MaterialTheme.spacing.spaceTwelve * 2),

                            ) {

                        Header(
                                modifier = Modifier.padding(
                                        bottom = MaterialTheme.spacing.spaceTen * 4
                                ),
                                title = R.string.header_text_login,
                                subTitle = R.string.cap_text_capture_thoughts,
                        )
                        LoginInputFields(
                                uiState = uiState,
                                onEvent = onEvent
                        )
                    }
                }
            }
        }

        DeviceType.MOBILE_LANDSCAPE -> {
            Column(
                    modifier = rootModifier
                            .consumeWindowInsets(WindowInsets.navigationBars)
                            .windowInsetsPadding(WindowInsets.displayCutout)
                            .statusBarsPadding()
            ) {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        shape = getClippingShape()
                ) {
                    Row(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = MaterialTheme.spacing.spaceTen * 6)
                                    .padding(end = MaterialTheme.spacing.spaceTen * 4)
                                    .padding(top = MaterialTheme.spacing.spaceLarge)
                                    .padding(bottom = MaterialTheme.spacing.spaceTen * 4),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve * 2)

                    ) {

                        Header(
                                modifier = Modifier
                                        .weight(.5f)
                                        .padding(bottom = MaterialTheme.spacing.spaceTen * 4),
                                title = R.string.header_text_login,
                                subTitle = R.string.cap_text_capture_thoughts,
                        )

                        LoginInputFields(
                                modifier = Modifier
                                        .weight(.5f)
                                        .verticalScroll(rememberScrollState()),
                                uiState = uiState,
                                onEvent = onEvent
                        )
                    }
                }
            }
        }

        DeviceType.TABLET_PORTRAIT,
        DeviceType.TABLET_LANDSCAPE,
        DeviceType.DESKTOP -> {
            Box(modifier = rootModifier.padding(top = MaterialTheme.spacing.spaceTen * 4)) {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        shape = getClippingShape()
                ) {
                    Column(
                            modifier = Modifier
                                    .padding(horizontal = MaterialTheme.spacing.spaceOneTwenty)
                                    .padding(vertical = MaterialTheme.spacing.spaceOneHundred),

                            ) {

                        Header(
                                modifier = Modifier.padding(
                                        bottom = MaterialTheme.spacing.spaceTen * 4
                                ),
                                textAlignment = TextAlign.Center,
                                title = R.string.header_text_login,
                                subTitle = R.string.cap_text_capture_thoughts,
                        )

                        LoginInputFields(
                                uiState = uiState,
                                onEvent = onEvent
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun LoginInputFields(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Column(
                modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.spaceTwelve * 2),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            AppTextField(
                    label = stringResource(id = R.string.lab_text_email_label),
                    placeholderString = stringResource(id = R.string.placeholder_text_email),
                    textFieldState = uiState.fieldTextState.emailTextFieldState,
                    supportText = SupportText.EmailSupportText,
                    isError = uiState.fieldError.emailError
            )

            AppTextField(
                    label = stringResource(id = R.string.lab_text_password_label),
                    supportText = SupportText.BlankSupportText,
                    placeholderString = stringResource(id = R.string.placeholder_text_password),
                    textFieldState = uiState.fieldTextState.passwordTextFieldState,
                    isError = uiState.fieldError.passwordError,
                    isSecureText = uiState.isSecureText,
                    onIconClick = { onEvent(LoginUiEvent.TogglePasswordVisibility) },
                    icon = uiState.isSecureText.eyeIcon
            )
        }

        Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)
        ) {
            AppButton(
                    buttonText = stringResource(id = R.string.btn_text_login),
                    onClick = { onEvent(LoginUiEvent.Login) },
                    isEnabled = uiState.fieldError == LoginUiState.FieldError(),
                    isLoading = uiState.loginStatus is Resource.Loading
            )
            AppTextButton(
                    text = stringResource(id = R.string.txt_btn_no_account),
                    onClick = { onEvent(LoginUiEvent.RegisterAccount) }
            )
        }
    }
}

@PreviewScreenSizes

@Composable
private fun LoginScreenContentPreview() {

    NoteMarkTheme {

        LoginScreenContent(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                uiState = LoginUiState(),
                onEvent = {}
        )
    }
}