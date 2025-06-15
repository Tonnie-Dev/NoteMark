package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.AppTextButton
import com.tonyxlab.notemark.presentation.core.components.AppTextField
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiEvent
import com.tonyxlab.notemark.presentation.screens.login.handling.LoginUiState
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
            viewModel = viewModel
    ) { state ->
        LoginScreenContent(
                uiState = state,
                modifier = modifier,
                onEvent = viewModel::onEvent
        )
    }

}


@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {

    val emailTextFieldState = uiState.emailTextFieldState
    val passwordTextFieldState = rememberTextFieldState(initialText = "Tonnie Xiii")

    Surface(
            modifier = modifier
                    .padding(WindowInsets.statusBars.asPaddingValues()) // 👈 Add this here if you want spacing
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
                    title = R.string.header_text_login,
                    subTitle = R.string.cap_text_capture_thoughts
            )

            Column {
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)) {
                    AppTextField(
                            label = stringResource(id = R.string.lab_text_email_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_email),
                            textFieldState = emailTextFieldState
                    )

                    AppTextField(
                            label = stringResource(id = R.string.lab_text_email_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_email),
                            textFieldState = passwordTextFieldState
                    )

                    AdvancedTextField(textFieldState = uiState.emailTextFieldState)

                }

                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)) {

                    AppButton(
                            buttonText = stringResource(id = R.string.btn_text_login),
                            onClick = { onEvent(LoginUiEvent.Login) })
                    AppTextButton(
                            text = stringResource(id = R.string.txt_btn_no_account),
                            onClick = { onEvent(LoginUiEvent.RegisterAccount) }

                    )
                }
            }
        }
    }
}
@Composable
fun AdvancedTextField(textFieldState: TextFieldState) {
    // Create and remember the TextFieldState
    //val textFieldState = rememberTextFieldState("ff")

    BasicTextField(
            state = textFieldState,
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
    )

    // You can access the text value anywhere using:
    // textFieldState.text.toString()
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