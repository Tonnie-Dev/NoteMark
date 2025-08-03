package com.tonyxlab.notemark.presentation.screens.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.core.utils.EditorTextFieldStyle.EditorFocusedNoteStyle
import com.tonyxlab.notemark.presentation.core.utils.EditorTextFieldStyle.EditorTitleStyle
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun EditableText(
    textFieldState: TextFieldState,
    placeHolderText: String,
    isEditing: Boolean,
    onEvent: (EditorUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    isTitle: Boolean

) {
    val focusRequester = remember { FocusRequester() }

    val currentStyle = remember(isTitle) {
        when {
            isTitle -> Pair(EditorTitleStyle, EditorFocusedNoteStyle)
            else -> Pair(EditorFocusedNoteStyle, EditorTitleStyle)
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
                        .focusRequester(focusRequester),
                state = textFieldState,
                textStyle = currentStyle.first.textStyle(),
                cursorBrush = SolidColor(currentStyle.first.cursorColor()),
                decorator = { innerTextField ->
                    TextDecorator(innerDefaultText = innerTextField)
                }
        )
    } else {

        Text(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                        ) {
                            if (isTitle) {
                                onEvent(EditorUiEvent.EditNoteTitle)
                            } else {
                                onEvent(EditorUiEvent.EditNoteContent)
                            }
                        }
                        .padding(vertical = MaterialTheme.spacing.spaceTen * 2),
                text = textFieldState.text.toString()
                        .ifBlank { placeHolderText },
                style = currentStyle.second.textStateStyle()
        )
    }
}

@Composable
private fun TextDecorator(
    innerDefaultText: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.spaceTen * 2)

    ) {
        innerDefaultText()
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
            EditableText(
                    textFieldState = textFieldState1,
                    placeHolderText = "Tonnie XIII",
                    isEditing = true,
                    isTitle = true,
                    onEvent = {}
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            EditableText(
                    textFieldState = textFieldState2,
                    placeHolderText = "Tonnie XIII",
                    isEditing = true,
                    isTitle = false,
                    onEvent = {}
            )
        }
    }
}
