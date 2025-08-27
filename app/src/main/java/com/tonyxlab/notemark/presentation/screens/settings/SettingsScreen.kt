@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppDialog
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.notemark.presentation.core.components.SnackbarController
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.settings.components.SyncIntervalRow
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

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

        Box(modifier = Modifier.fillMaxSize()) {
            if (it.syncInProgress || it.isLoggingOut) {
                Box(
                        modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Black.copy(alpha = 0.04f)),
                        contentAlignment = Alignment.Center
                ) {
                    Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                                modifier = Modifier.size(MaterialTheme.spacing.spaceLarge),
                                color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
                        Text(
                                text = if (it.isLoggingOut)
                                    stringResource(id = R.string.cap_text_logging_out)
                                else
                                    stringResource(id = R.string.cap_text_syncing),
                                style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        )
                    }
                }

            }
            SettingsScreenContent(
                    modifier = modifier,
                    uiState = it,
                    onEvent = viewModel::onEvent
            )
            if (it.dialogState.showDialog) {

                AppDialog(
                        dialogTitle = stringResource(id = it.dialogState.dialogTitle),
                        dialogText = stringResource(id = it.dialogState.dialogText),
                        positiveButtonText = stringResource(id = it.dialogState.positiveButtonText),
                        negativeButtonText = stringResource(id = it.dialogState.negativeButtonText),
                        onDismissRequest = { viewModel.onEvent(SettingsUiEvent.NegativeButtonClick) },
                        onConfirm = { viewModel.onEvent(SettingsUiEvent.PositiveButtonClick) }
                )
            }
        }

    }
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {


    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    when (deviceType) {
        DeviceType.MOBILE_PORTRAIT -> {

            SettingsSection(
                    modifier = modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                    uiState = uiState,
                    onEvent = onEvent
            )

        }

        DeviceType.MOBILE_LANDSCAPE -> {

            SettingsSection(
                    modifier = modifier
                            .padding(start = MaterialTheme.spacing.spaceTen * 6)
                            .padding(end = MaterialTheme.spacing.spaceMedium),
                    uiState = uiState,
                    onEvent = onEvent
            )

        }

        DeviceType.TABLET_PORTRAIT,
        DeviceType.TABLET_LANDSCAPE,
        DeviceType.DESKTOP -> {

            SettingsSection(
                    modifier = modifier.padding(horizontal = MaterialTheme.spacing.spaceTwelve * 2),
                    uiState = uiState,
                    onEvent = onEvent
            )
        }
    }


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

    ) {

        SyncIntervalRow(uiState = uiState, onEvent = onEvent)

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(SettingsUiEvent.SyncData)
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
                        style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        )
                )
                Text(
                        text = stringResource(
                                id = R.string.settings_last_sync,
                                uiState.lastSyncTime
                        ),
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
                    style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.error
                    )
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
                verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.spacing.spaceMedium
                )
        ) {
            SettingsScreenContent(uiState = SettingsUiState(), onEvent = {})
        }
    }
}