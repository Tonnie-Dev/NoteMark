package com.tonyxlab.notemark.presentation.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState
import com.tonyxlab.notemark.presentation.screens.settings.handling.SyncInterval
import com.tonyxlab.notemark.util.toDpSize

@Composable
fun SyncIntervalRow(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }
    val localDensity = LocalDensity.current

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(SettingsUiEvent.ShowSyncIntervalSettings)
                    }
                    .padding(vertical = MaterialTheme.spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
                modifier = Modifier
                        .weight(.5f),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = stringResource(id = R.string.cds_sync_data),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            Text(
                    text = stringResource(id = R.string.settings_sync_interval),
                    style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                    )
            )
        }

        Row(
                modifier = Modifier
                        .weight(.5f)
                        .onGloballyPositioned {
                            anchorSize = with(localDensity) {
                                it.size
                            }
                        },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
        ) {

            Row {
                Text(
                        text = uiState.syncMenuState.activeInterval.toStringInterval(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

                Icon(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceTen * 2),
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = stringResource(id = R.string.cds_sync_data),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(modifier = modifier.offset(y = MaterialTheme.spacing.spaceLarge)) {
                DropdownMenu(
                        modifier = Modifier.width(anchorSize.toDpSize().width),
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        expanded = uiState.syncMenuState.isMenuOpen,
                        onDismissRequest = { onEvent(SettingsUiEvent.DismissSyncMenu) }
                ) {

                    uiState.syncMenuState.intervals.fastForEach { interval ->

                        DropdownMenuItem(
                                modifier = modifier
                                        .semantics {
                                            role = Role.RadioButton
                                            selected =
                                                (uiState.syncMenuState.activeInterval == interval)
                                        },
                                text = {
                                    Text(
                                            text = interval.toStringInterval(),
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                    color = MaterialTheme.colorScheme.onSurface
                                            )
                                    )
                                },
                                trailingIcon = {
                                    if (uiState.syncMenuState.activeInterval == interval) {
                                        Icon(
                                                modifier = Modifier.size(
                                                        MaterialTheme.spacing.spaceTen * 2
                                                ),
                                                imageVector = Icons.Default.Check,
                                                contentDescription = stringResource(
                                                        id = R.string.cds_text_check_mark
                                                ),
                                                tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                onClick = {
                                    onEvent(SettingsUiEvent.SelectSyncInterval(interval))
                                    onEvent(SettingsUiEvent.DismissSyncMenu)
                                }
                        )
                    }
                }
            }

        }
    }
}


@Composable
private fun SyncInterval.toStringInterval(): String {

    return when (this) {
        SyncInterval.ManualOnlyInterval ->
            stringResource(R.string.settings_manual_only)

        SyncInterval.FifteenMinutesInterval ->
            stringResource(R.string.settings_fifteen_mins)

        SyncInterval.ThirtyMinutesInterval ->

            stringResource(R.string.settings_thirty_mins)

        SyncInterval.HourlyInterval ->
            stringResource(R.string.settings_one_hour)
    }
}



