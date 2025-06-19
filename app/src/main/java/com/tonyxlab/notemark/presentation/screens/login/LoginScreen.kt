package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.AppTextButton
import com.tonyxlab.notemark.presentation.core.components.AppTextField
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.utils.SupportText
import com.tonyxlab.notemark.presentation.core.utils.eyeIcon
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginActionEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
import com.tonyxlab.notemark.presentation.screens.signup.handling.SignupUiEvent
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navOperations: NavOperations,
    viewModel: LoginViewModel = koinViewModel()
) {
    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            actionEventHandler = { _, actionEvent ->
                when (actionEvent) {
                    LoginActionEvent.NavigateToMainScreen -> {}
                    LoginActionEvent.NavigateToSignupScreen -> {
                        navOperations.navigateToSignupScreenDestination()
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
                        title = R.string.header_text_login,
                        subTitle = R.string.cap_text_capture_thoughts,
                )

                Column {
                    Column(
                            modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceTwelve * 2),
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

                    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)) {

                        AppButton(
                                buttonText = stringResource(id = R.string.btn_text_login),
                                onClick = { onEvent(LoginUiEvent.Login) },
                                isEnabled = uiState.fieldError == LoginUiState.FieldError() //uiState.isLoginButtonEnabled

                        )
                        AppTextButton(
                                text = stringResource(id = R.string.txt_btn_no_account),
                                onClick = { onEvent(LoginUiEvent.RegisterAccount) }

                        )
                    }
                }
            }
        }
    }
}


@PreviewLightDark
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