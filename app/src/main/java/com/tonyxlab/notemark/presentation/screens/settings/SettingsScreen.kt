@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.components.ShowAppSnackbar
import com.tonyxlab.notemark.presentation.core.components.SnackbarController
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
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
                }
            },
            onBackPressed = { viewModel.onEvent(SettingsUiEvent.ExitSettings) }
    ) {
        SettingsScreenContent(
                modifier = modifier,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun SettingsScreenContent(
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
SettingsSection(onEvent = onEvent)
}


@Composable
fun SettingsSection(
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
                            onEvent(SettingsUiEvent.LogOut)
                        }
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = stringResource(id = R.string.cds_text_log_out),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            Text(
                    text = stringResource(id = R.string.txt_btn_log_out),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }


        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(SettingsUiEvent.LogOut)
                        }
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(id = R.string.cds_text_log_out),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            Text(
                    text = stringResource(id = R.string.txt_btn_log_out),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(SettingsUiEvent.LogOut)
                        }
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {
            Icon(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 2),
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(id = R.string.cds_text_log_out),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            Text(
                    text = stringResource(id = R.string.txt_btn_log_out),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.error)
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
            SettingsScreenContent(onEvent = {})
        }
    }
}