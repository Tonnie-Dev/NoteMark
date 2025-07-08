package com.tonyxlab.notemark.presentation.screens.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme


@Composable
fun EditorText(modifier: Modifier = Modifier) {

}

@Composable
private fun EditorTextDecorator(
    innerDefaultText: @Composable () -> Unit,
    placeHolderText: String,
    isTextEmpty: Boolean,
    isFocused: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {


    val backgroundColor = if (isFocused)
        MaterialTheme.colorScheme.surface
    else
        MaterialTheme.colorScheme.surfaceVariant

    Row(
            modifier = modifier
                    .background(backgroundColor)
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.spaceMedium)
    ) {


        if (isTextEmpty && !isFocused) {
            Text(text = placeHolderText)
        } else {

            innerDefaultText()
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


            EditorTextDecorator(
                    innerDefaultText = {},
                    placeHolderText = "Placeholder",
                    isTextEmpty = true,
                    isFocused = true
            )
            EditorTextDecorator(
                    innerDefaultText = {},
                    placeHolderText = "Placeholder",
                    isTextEmpty = false,
                    isFocused = false
            )
        }
    }
}
