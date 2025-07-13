package com.tonyxlab.notemark.presentation.screens.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.core.utils.EditorTextFieldStyle
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun EditorText(
    textFieldState: TextFieldState,
    placeHolderText: String,
    isEditing: Boolean,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    style: EditorTextFieldStyle = EditorTextFieldStyle.EditorPlaceHolderNoteStyle,
    isTitle: Boolean

) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    val currentStyle = remember(isFocused) {
        when {
            isFocused && isTitle -> EditorTextFieldStyle.EditorTitleStyle
            isFocused -> EditorTextFieldStyle.EditorFocusedNoteStyle
            else -> style
        }
    }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }

    if (isEditing) {
        BasicTextField(
                modifier = modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isFocused = it.isFocused
                        },
                state = textFieldState,
                textStyle = currentStyle.textStyle(),
                decorator = { innerTextField ->
                    EditorTextDecorator(
                            innerDefaultText = innerTextField,
                            placeHolderText = placeHolderText,
                            isFocused = isFocused,
                            editorTextFieldStyle = currentStyle
                    )
                }
        )
    } else {

        val textStyle =
            if (isTitle)
                MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                )
            else
                MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )

        Text(

                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            if (isTitle) {
                                onEvent(EditorUiEvent.EditNoteTitle)
                            } else {
                                onEvent(EditorUiEvent.EditNoteContent)
                            }
                        },
                text = "Edit",
                style = textStyle
        )
    }
}


@Composable
private fun EditorTextDecorator(
    innerDefaultText: @Composable () -> Unit,
    placeHolderText: String,

    isFocused: Boolean,
    modifier: Modifier = Modifier,
    editorTextFieldStyle: EditorTextFieldStyle = EditorTextFieldStyle.EditorPlaceHolderNoteStyle,

    ) {


    Row(
            modifier = modifier
                    .background(editorTextFieldStyle.backgroundColor())
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.spaceMedium)
    ) {


        if (isFocused) {

            innerDefaultText()

        } else {

            Text(text = placeHolderText)
        }
    }

}


@PreviewLightDark
@Composable
private fun EditorText_Preview() {


    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            val textFieldState1 = remember { TextFieldState() }
            val textFieldState2 = remember { TextFieldState(initialText = "Tonnie XIII") }
            EditorText(
                    textFieldState = textFieldState1,
                    placeHolderText = "Tonnie XIII",
                    isEditing = true,
                    isTitle = true,
                    onEvent = {}
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            EditorText(
                    textFieldState = textFieldState2,
                    placeHolderText = "Tonnie XIII",
                    isEditing = true,
                    isTitle = false,
                    onEvent = {}
            )

        }
    }
}
