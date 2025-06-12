package com.tonyxlab.notemark.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.components.AppTextField
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navOperations: NavOperations
) {
    Scaffold { innerPadding ->
        LoginScreenContent(
                modifier = modifier
                        .padding(innerPadding)
                        .background(color = Color.Red)
        )

    }

}


@Composable
fun LoginScreenContent(modifier: Modifier = Modifier) {

    val emailTextFieldState = TextFieldState()
    val passwordTextFieldState = TextFieldState()

    Column(modifier = modifier.fillMaxSize()) {

        Header(
                title = R.string.header_text_login,
                subTitle = R.string.cap_text_capture_thoughts
        )

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
}


@PreviewLightDark
@Composable
private fun LoginScreenContentPreview() {

    NoteMarkTheme {

        LoginScreenContent(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        )
    }

}