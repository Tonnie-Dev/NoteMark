@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.notemark.presentation.core.components.SnackbarController
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {

    SetStatusBarIconsColor(darkIcons = true)

    val snackbarController = remember {
        SnackbarController<SettingsUiEvent>()
    }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ShowAppSnackbar(
            triggerId = snackbarController.triggerId,
            snackbarHostState = snackbarHostState,
            message = snackbarController.message,
            actionLabel = snackbarController.actionLabel,
            onActionClick = { snackbarController.actionEvent?.let { viewModel.onEvent(it) } },
            onDismiss = { snackbarController.dismissSnackbar() }
    )

    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_settings),
                        onChevronIconClick = { viewModel.onEvent(SettingsUiEvent.ExitSettings) })
            },
            actionEventHandler = { _, action ->
                when (action) {
                    SettingsActionEvent.ExitSettings -> {
                        navOperations.popBackStack()
                    }

                    is SettingsActionEvent.ShowSnackbar -> {
                        snackbarController.showSnackbar(
                                message = context.getString(action.messageRes),
                                actionLabel = context.getString(action.labelRes),
                                actionEvent = action.settingsActionEvent
                        )
                    }

                    SettingsActionEvent.Logout -> {
                        navOperations.navigateToLoginScreenAndClearBackStack()
                    }

                    SettingsActionEvent.ShowSyncIntervalContextMenu -> {
                        viewModel.onEvent(SettingsUiEvent.ShowSyncIntervalSettings)
                    }
                }
            },
            onBackPressed = { viewModel.onEvent(SettingsUiEvent.ExitSettings) }
    ) {
        SettingsScreenContent(
                modifier = modifier,
                uiState = it,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsSection(
            modifier = modifier,
            uiState = uiState,
            onEvent = onEvent,

    )
}


@Composable
fun SettingsSection(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
    ) {

        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(SettingsUiEvent.ShowSyncIntervalSettings)
                        }
                        .padding(vertical = MaterialTheme.spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceTen * 2),
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = stringResource(id = R.string.cds_text_log_out),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

                Text(
                        text = stringResource(id = R.string.settings_sync_interval),
                        style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
            }

            Row {

                Text(
                        text = stringResource(id = R.string.settings_sync_interval),
                        style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))
                Icon(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceTen * 2),
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = stringResource(id = R.string.cds_text_log_out),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(SettingsUiEvent.ShowSyncIntervalSettings)
                        }
                        .padding(vertical = MaterialTheme.spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.Default.Sync,
                    contentDescription = stringResource(id = R.string.cds_text_log_out),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            Column {

                Text(
                        text = stringResource(id = R.string.settings_sync_data),
                        style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
                Text(
                        text = stringResource(id = R.string.settings_last_sync),
                        style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                )

            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(SettingsUiEvent.LogOut)
                        }
                        .padding(vertical = MaterialTheme.spacing.spaceMedium)
        ) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(id = R.string.cds_text_log_out),
                    tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            Text(
                    text = stringResource(id = R.string.settings_log_out),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }

    DropdownMenu(
            expanded = uiState.syncMenuState.isMenuOpen,
            onDismissRequest = { onEvent(SettingsUiEvent.DismissSyncMenu)}
    ) {


        SyncIntervalDropDown(uiState = uiState, onEvent = onEvent)
    }
}


@Composable
fun SyncIntervalDropDown(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {


    Column(modifier = modifier) {

        uiState.syncMenuState.intervals.fastForEach { interval ->

            IntervalItem(
                    textRes = when (interval) {
                        SettingsUiState.SyncInterval.ManualOnlyInterval -> R.string.settings_manual_only
                        SettingsUiState.SyncInterval.FifteenMinutesInterval -> R.string.settings_fifteen_mins
                        SettingsUiState.SyncInterval.ThirtyMinutesInterval -> R.string.settings_thirty_mins
                        SettingsUiState.SyncInterval.HourlyInterval -> R.string.settings_one_hour
                    },
                    onSelectInterval = { onEvent(SettingsUiEvent.SelectSyncInterval(interval)) },
                    isSelected = uiState.syncMenuState.activeInterval == interval

            )

        }


    }
}


@Composable
fun IntervalItem(
    @StringRes
    textRes: Int,
    onSelectInterval: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                    ) {
                        onSelectInterval
                    }
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceTwelve),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        )

        AnimatedVisibility(isSelected) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.cds_text_check_mark),
                    tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenContent_Preview() {

    NoteMarkTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            SettingsScreenContent(uiState = SettingsUiState(), onEvent = {})
        }
    }
}