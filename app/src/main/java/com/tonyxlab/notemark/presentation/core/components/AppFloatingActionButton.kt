package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.gradient
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme

@Composable
fun AppFloatingActionButton(modifier: Modifier = Modifier, onClickFab: () -> Unit) {
    Box(
            modifier = modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                            brush = MaterialTheme.gradient.fabGradient,
                            shape = MaterialTheme.shapes.extraLarge
                    )
                    .size(MaterialTheme.spacing.spaceExtraLarge)
                    .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClickFab
                    ),
            contentAlignment = Alignment.Center
    ) {
        Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.cds_text_add),
                tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@PreviewLightDark
@Composable
private fun AppFloatingActionButtonPreview() {

    NoteMarkTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppFloatingActionButton {}

        }
    }
}