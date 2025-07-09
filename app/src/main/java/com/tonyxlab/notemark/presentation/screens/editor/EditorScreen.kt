package com.tonyxlab.notemark.presentation.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.screens.editor.component.EditorAppBar
import com.tonyxlab.notemark.presentation.screens.editor.component.EditorText
import com.tonyxlab.notemark.presentation.screens.home.HomeScreenContent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.notemark.presentation.screens.login.components.getNotes
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun EditorScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Text(text = "Editor Screen")
    }
}


@Composable
fun EditorScreenContent(modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {

    val textFieldState = remember { TextFieldState( ) }
    Column(modifier = modifier) {

        EditorAppBar { }
        EditorText(textFieldState = textFieldState, placeHolderText = "Tonnie XII", isTitle = true, isEditing = true)
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        EditorText(textFieldState = textFieldState, placeHolderText = "Tonnie XII")
    }
}


@PreviewLightDark
@Composable
private fun EditorScreenContent_Preview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()

        ) {
           EditorScreenContent{}

        }
    }


}


