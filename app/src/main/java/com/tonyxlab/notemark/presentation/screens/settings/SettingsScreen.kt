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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsScreen(
    navOperations: NavOperations,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        screenTitle = stringResource(id = R.string.topbar_text_settings),
                        onPressBack = { viewModel.onEvent(SettingsUiEvent.ExitSettings) })
            },
            actionEventHandler = { _,action ->

                when(action){
                    SettingsActionEvent.NavigateBack -> navOperations.popBackStack()
                    is SettingsActionEvent.ShowSnackbar -> TODO()
                }
            }
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
                        .padding(MaterialTheme.spacing.spaceMedium)
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