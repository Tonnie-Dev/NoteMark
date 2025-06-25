package com.tonyxlab.notemark.presentation.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.tonyxlab.notemark.presentation.core.utils.SupportText
import com.tonyxlab.notemark.presentation.core.utils.eyeIcon
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupActionEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiEvent
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignupScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var snackbarTriggerId by remember { mutableIntStateOf(0) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarActionLabel by remember { mutableStateOf("") }
    var snackbarActionEvent by remember {
        mutableStateOf<SignupUiEvent?>(null)

    }

    val context = LocalContext.current

    ShowAppSnackbar(
            triggerId = snackbarTriggerId,
            snackbarHostState = snackbarHostState,
            message = snackbarMessage,
            actionLabel = snackbarActionLabel,
            onActionClick = { snackbarActionEvent?.let { viewModel.onEvent(it) } },
            onDismiss = {
                snackbarTriggerId = 0
                snackbarActionEvent = null
            }
    )
    BaseContentLayout(
            viewModel = viewModel,
            snackbarHost = {
                AppSnackbarHost(
                        snackbarHostState = snackbarHostState,
                        isError = state.loginStatus is Resource.Error
                )
            },

            actionEventHandler = { _, signupActionEvent ->

                when (signupActionEvent) {

                    SignupActionEvent.NavigateToLoginScreen -> {
                        navOperations.navigateToLoginScreenDestination()
                    }

                    is SignupActionEvent.ShowSnackbar -> {

                        snackbarMessage = context.getString(signupActionEvent.messageRes)
                        snackbarActionLabel = context.getString(signupActionEvent.actionLabelRes)
                        snackbarActionEvent = signupActionEvent.onActionClick
                        snackbarTriggerId++
                    }
                }
            }
    ) { uiState ->

        SignupScreenContent(
                modifier = modifier,
                uiState = uiState,
                onEvent = viewModel::onEvent
        )

    }

}

@Composable
fun SignupScreenContent(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val passwordOneVisibility = uiState.passwordVisibility.isPasswordOneVisible
    val passwordTwoVisibility = uiState.passwordVisibility.isPasswordTwoVisible

    Box(
            modifier = modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxSize()
                    .padding(top = MaterialTheme.spacing.spaceLarge)
    ) {
        Surface(
                modifier = Modifier
                        .clip(getClippingShape())
                        .padding(top = MaterialTheme.spacing.spaceLarge),
                color = MaterialTheme.colorScheme.background,
                shape = getClippingShape()
        ) {
            Column(
                    modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                            .padding(top = MaterialTheme.spacing.spaceLarge)
            ) {
                Header(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceTen * 4),
                        title = R.string.header_text_create_account,
                        subTitle = R.string.cap_text_capture_thoughts
                )

                Column(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceTwelve * 2),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
                ) {
                    AppTextField(
                            label = stringResource(R.string.lab_text_username_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_username),
                            supportText = SupportText.UsernameSupportText,
                            textFieldState = uiState.fieldTextState.username,
                            isError = uiState.fieldError.usernameError
                    )

                    AppTextField(
                            label = stringResource(id = R.string.lab_text_email_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_email),
                            textFieldState = uiState.fieldTextState.email,
                            isError = uiState.fieldError.emailError,
                            supportText = SupportText.EmailSupportText
                    )

                    AppTextField(
                            label = stringResource(id = R.string.lab_text_password_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_password),
                            supportText = SupportText.PasswordOneSupportText,
                            textFieldState = uiState.fieldTextState.passwordOne,
                            isSecureText = passwordOneVisibility,
                            icon = passwordOneVisibility.eyeIcon,
                            isError = uiState.fieldError.passwordError,
                            onIconClick = { onEvent(SignupUiEvent.TogglePasswordOneVisibility) }
                    )
                    AppTextField(
                            label = stringResource(id = R.string.lab_text_repeat_password_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_password),
                            textFieldState = uiState.fieldTextState.passwordTwo,
                            isSecureText = passwordTwoVisibility,
                            supportText = SupportText.PasswordTwoSupportText,
                            icon = passwordTwoVisibility.eyeIcon,
                            isError = uiState.fieldError.confirmPasswordError,
                            onIconClick = { onEvent(SignupUiEvent.TogglePasswordTwoVisibility) }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)) {

                    AppButton(
                            buttonText = stringResource(id = R.string.btn_text_login),
                            onClick = { onEvent(SignupUiEvent.CreateAccount) },
                            isEnabled = uiState.fieldError == SignupUiState.FieldError(),
                            isLoading = uiState.loginStatus == Resource.Loading

                    )
                    AppTextButton(
                            text = stringResource(id = R.string.txt_btn_already_have_account),
                            onClick = { onEvent(SignupUiEvent.CreateAccount) }
                    )
                }
            }
        }
    }

}

@PreviewLightDark
@Composable
private fun LoginScreenContentPreview() {
    NoteMarkTheme {
        SignupScreenContent(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                uiState = SignupUiState(),
                onEvent = {}
        )
    }
}


