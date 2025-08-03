package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.textButtonStyle

@Composable
fun AppTopBar(
    screenTitle: String,
    onChevronIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = Dp.Unspecified,
) {

    Row(
            modifier = modifier
                    .statusBarsPadding()
                    .height(height = height)
                    .padding(MaterialTheme.spacing.spaceSmall),
            verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
                modifier = Modifier
                        .padding(end = MaterialTheme.spacing.spaceDoubleDp)
                        .clickable { onChevronIconClick() },
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = stringResource(id = R.string.cds_text_back),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
                text = screenTitle,
                style = MaterialTheme.typography.textButtonStyle.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        )
    }
}


@PreviewLightDark
@Composable
private fun AppTopBar_Preview() {

    NoteMarkTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "SETTINGS",

                    onChevronIconClick = {}
            )
            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "HOME",
                    height = 56.dp,
                    onChevronIconClick = {}
            )
            AppTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    screenTitle = "EDITOR",
                    onChevronIconClick = {}
            )
        }
    }
}
