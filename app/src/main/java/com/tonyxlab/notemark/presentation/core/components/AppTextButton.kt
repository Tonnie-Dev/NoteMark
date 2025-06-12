package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun AppTextButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {

    TextButton(
            modifier = modifier
                    .fillMaxWidth()
            //.padding(horizontal = MaterialTheme.spacing.spaceTen * 2)
            //.padding(vertical = MaterialTheme.spacing.spaceTen),
            , onClick = onClick
    ) {

        Text(
                text = text,
                style = MaterialTheme.typography.titleSmall
        )
    }
}

@PreviewLightDark
@Composable
private fun AppTextPreview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            AppTextButton(text = "Register") { }
            AppTextButton(text = "Already have an account") { }
        }

    }
}