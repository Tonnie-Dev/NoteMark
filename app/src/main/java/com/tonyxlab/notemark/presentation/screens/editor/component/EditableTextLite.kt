package com.tonyxlab.notemark.presentation.screens.editor.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction


@Composable
fun EditableTextLite(
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    onStartEditing: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if (isEditing) {
        BasicTextField(
               state = textFieldState,
                modifier = modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (!it.isFocused) onStartEditing()
                        },
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),

        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

    } else {
        Text(
                text = textFieldState.text.toString(),
                modifier = modifier.clickable { onStartEditing() }, // edit on tap
                style = LocalTextStyle.current
        )
    }
}
