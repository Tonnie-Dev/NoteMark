package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun AppTopBar(
    title: String,
    profileInitials: String,
    modifier: Modifier = Modifier
) {
    val clipShape = MaterialTheme.shapes.medium

    Surface(color = MaterialTheme.colorScheme.background) {

        Row(
                modifier = modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.spacing.spaceExtraLarge)
                        .padding(vertical = MaterialTheme.spacing.spaceSmall)
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )

            Box(
                    modifier = Modifier
                            .clip(clipShape)
                            .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = clipShape
                            )
                            .size(MaterialTheme.spacing.spaceTen * 4)
                            .padding(MaterialTheme.spacing.spaceExtraSmall),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = profileInitials,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AppTopBarPreview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
        ) {
            AppTopBar(
                    title = "NoteMark",
                    profileInitials = "TX"
            )
        }
    }
}