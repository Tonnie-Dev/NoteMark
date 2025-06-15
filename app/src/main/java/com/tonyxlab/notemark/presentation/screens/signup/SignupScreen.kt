package com.tonyxlab.notemark.presentation.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.components.AppButton
import com.tonyxlab.notemark.presentation.core.components.AppTextButton
import com.tonyxlab.notemark.presentation.core.components.AppTextField
import com.tonyxlab.notemark.presentation.core.components.Header
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.getClippingShape

@Composable
fun SignupScreenContent(modifier: Modifier = Modifier) {

    val textFieldState = rememberTextFieldState()

    /* val eyeIcon = when {
         uiState.isSecureText -> Icons.Default.Visibility
         else -> Icons.Default.VisibilityOff
     }*/
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
                            textFieldState = textFieldState
                    )

                    AppTextField(
                            label = stringResource(id = R.string.lab_text_email_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_email),
                            textFieldState = textFieldState,
                    )

                    AppTextField(
                            label = stringResource(id = R.string.lab_text_password_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_password),
                            textFieldState = textFieldState,
                    )
                    AppTextField(
                            label = stringResource(id = R.string.lab_text_repeat_password_label),
                            placeholderString = stringResource(id = R.string.placeholder_text_password),
                            textFieldState = textFieldState,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)) {

                    AppButton(
                            buttonText = stringResource(id = R.string.btn_text_login),
                            onClick = { },
                            isEnabled = false

                    )
                    AppTextButton(
                            text = stringResource(id = R.string.txt_btn_already_have_account),
                            onClick = { }

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

                )
    }

}
