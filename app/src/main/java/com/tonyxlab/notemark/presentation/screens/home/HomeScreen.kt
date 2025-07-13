@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.home

import android.os.Build
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.notemark.navigation.NavOperations
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppFloatingActionButton
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
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

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                AppTopBar(profileInitials = formatUserInitials(homeState.username))
            },
            floatingActionButton = {
                AppFloatingActionButton(modifier = Modifier.navigationBarsPadding()) {

                    navOperations.navigateToEditorScreenDestination()
                }
            },
            actionEventHandler = { _, action ->
                when (action) {
                    is HomeActionEvent.NavigateToEditorScreen -> navOperations.navigateToEditorScreenDestination()
                    HomeActionEvent.NavigateToLoginScreen -> navOperations.navigateToLoginScreenDestination()
                }
            }
    ) { state ->
        state.notes.ifEmpty { EmptyBoard() }
        HomeScreenContent(
                modifier = modifier,
                state = state,
               onEvent = viewModel::onEvent,
                deviceType = DeviceType.MOBILE_PORTRAIT
        )

    }
}


@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    deviceType: DeviceType = DeviceType.MOBILE_PORTRAIT
) {

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

            NotePreview(noteItem = note, onClickNoteItem = onEvent)
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