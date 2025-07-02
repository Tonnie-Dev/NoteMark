package com.tonyxlab.notemark.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun EmptyBoard(modifier: Modifier = Modifier) {
    Column(
            modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTen * 8))
        Text(
                text = stringResource(id = R.string.cap_text_empty_board),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
        )


    }
}

@PreviewLightDark
@Composable
private fun EmptyBoardPreview() {

    NoteMarkTheme {

        EmptyBoard()
    }
}
