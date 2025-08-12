@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppDialog
import com.tonyxlab.notemark.presentation.core.components.AppFloatingActionButton
import com.tonyxlab.notemark.presentation.screens.home.components.HomeTopBar
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.screens.home.components.EmptyBoard
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.notemark.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.notemark.presentation.screens.login.components.NotePreview
import com.tonyxlab.notemark.presentation.screens.login.components.getNotes
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.util.DeviceType
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import com.tonyxlab.notemark.util.formatUserInitials
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    navOperations: NavOperations
) {

    SetStatusBarIconsColor(darkIcons = true)

    val homeState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var toastMessage by remember { mutableStateOf("") }
    var longPressedNoteId by remember { mutableLongStateOf(-1L) }

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                HomeTopBar(
                        profileInitials = formatUserInitials(homeState.username),
                        isOffline = homeState.isOffline,
                        onClickSettingsIcon = { viewModel.onEvent(HomeUiEvent.ClickSettingsIcon)}
                )
            },
            floatingActionButton = {
                AppFloatingActionButton(modifier = Modifier.navigationBarsPadding()) { event ->
                    viewModel.onEvent(event)
                }
            },
            actionEventHandler = { _, action ->
                when (action) {

                    is HomeActionEvent.NavigateToEditorScreen -> {

                        navOperations.navigateToEditorScreenDestination(action.noteId)
                    }

                    HomeActionEvent.NavigateToLoginScreen -> {
                        navOperations.navigateToLoginScreenDestination()
                    }

                    is HomeActionEvent.ShowToast -> {

                        toastMessage = context.getString(action.messageRes)
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT)
                                .show()
                    }

                    is HomeActionEvent.ShowDialog -> {
                        longPressedNoteId = action.noteId
                    }

                    HomeActionEvent.NavigateToSettingsScreen -> {

                        navOperations.navigateToSettingsScreenDestination()
                    }
                }
            }
    ) { state ->

        state.notes.ifEmpty { EmptyBoard() }

        HomeScreenContent(
                modifier = modifier,
                state = state,
                onEvent = viewModel::onEvent
        )

        if (state.showDialog) {

            AppDialog(
                    dialogTitle = stringResource(id = R.string.dialog_text_delete_title),
                    dialogText = stringResource(id = R.string.dialog_text_delete_message),
                    positiveButtonText = stringResource(id = R.string.dialog_text_delete),
                    negativeButtonText = stringResource(id = R.string.dialog_text_cancel),
                    onDismissRequest = {
                        viewModel.onEvent(HomeUiEvent.DismissDialog)
                    },
                    onConfirm = {
                        viewModel.onEvent(HomeUiEvent.ConfirmDeleteNote(notedId = longPressedNoteId))
                    }
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)

    val columns = when (deviceType) {
        DeviceType.MOBILE_PORTRAIT, DeviceType.TABLET_PORTRAIT -> 2
        DeviceType.MOBILE_LANDSCAPE, DeviceType.TABLET_LANDSCAPE -> 3
        else -> 3
    }

    LazyVerticalStaggeredGrid(
            modifier = modifier.fillMaxSize(),
            columns = StaggeredGridCells.Fixed(count = columns),
            contentPadding = PaddingValues(MaterialTheme.spacing.spaceMedium),
            verticalItemSpacing = MaterialTheme.spacing.spaceMedium,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium),
    ) {
        items(state.notes) { note ->
            NotePreview(
                    noteItem = note,
                    onEvent = onEvent
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenContent_Preview() {

    NoteMarkTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
        ) {
            HomeScreenContent(
                    state = HomeUiState(notes = getNotes(20)),
                    onEvent = {}
            )
        }
    }
}