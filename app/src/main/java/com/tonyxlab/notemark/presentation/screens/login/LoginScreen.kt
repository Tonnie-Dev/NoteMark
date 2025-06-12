package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.AppTextButton
import com.tonyxlab.notemark.presentation.core.components.AppTextField
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navOperations: NavOperations
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.primary) { innerPadding ->
        LoginScreenContent(
                modifier = modifier
                        .padding(innerPadding),
                onClickButton = { navOperations.navigateToLoginScreenDestination() },
                onClickTextButton = { navOperations.navigateToLoginScreenDestination() }

        )

    }

}


@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit,
    onClickTextButton: () -> Unit
) {

    val emailTextFieldState = TextFieldState()
    val passwordTextFieldState = TextFieldState()

    Surface(
            modifier = Modifier
                    .padding(top = MaterialTheme.spacing.spaceLarge),
            color = MaterialTheme.colorScheme.background,
            shape = getClippingShape()
    ) {
        Column(
                modifier = modifier
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
                }

                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)) {

                    AppButton(
                            buttonText = stringResource(id = R.string.btn_text_login),
                            onClick = onClickButton
                    )
                    AppTextButton(
                            text = stringResource(id = R.string.txt_btn_no_account),
                            onClick = onClickTextButton
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

        LoginScreenContent(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                onClickButton = {},
                onClickTextButton = {}
        )
    }

}