package com.tonyxlab.notemark.presentation.screens.home.components

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme


@Composable
fun HomeTopBar(

    profileInitials: String,
    onClickSettingsIcon: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int = R.string.app_name,
) {
    val clipShape = MaterialTheme.shapes.medium

    Surface(color = MaterialTheme.colorScheme.background) {

        Row(
                modifier = modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(MaterialTheme.spacing.spaceExtraLarge)
                        .padding(vertical = MaterialTheme.spacing.spaceSmall)
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                    text = stringResource(id = titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                    horizontalArrangement = Arrangement.spacedBy(
                            MaterialTheme.spacing.spaceExtraSmall
                    ),
                    verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onClickSettingsIcon) {

                    Icon(
                            modifier = Modifier.Companion
                                    .size(MaterialTheme.spacing.spaceTen * 4)
                                    .padding(MaterialTheme.spacing.spaceSmall),
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(id = R.string.cds_text_settings),
                            tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box(
                        modifier = Modifier.Companion
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
            HomeTopBar(
                    titleRes = R.string.app_name,
                    profileInitials = "TX",
                    onClickSettingsIcon = {}
            )
        }
    }
}
