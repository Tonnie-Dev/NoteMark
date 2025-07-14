package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    positiveButtonText: String,
    negativeButtonText: String? = null

) {

    AlertDialog(
            title = { Text(modifier = Modifier.fillMaxWidth(), text = dialogTitle) },
            text = { Text(text = dialogText) },
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = positiveButtonText)
                }
            },
            dismissButton = {

                if (negativeButtonText?.isNotBlank() == true) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = negativeButtonText)
                    }
                }
            }
    )

}


@PreviewLightDark
@Composable
private fun AppDialog_Preview() {

    NoteMarkTheme {

        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),

                contentAlignment = Alignment.Center
        ) {


            AppDialog(
                    onDismissRequest = {},
                    onConfirm = {},
                    dialogTitle = "Delete",
                    dialogText = "Delete Note?",
                    positiveButtonText = "OK",
                    negativeButtonText = "Cancel"
            )

        }
    }

}