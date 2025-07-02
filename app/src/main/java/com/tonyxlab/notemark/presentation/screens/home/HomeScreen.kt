package com.tonyxlab.notemark.presentation.screens.home

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.notemark.presentation.core.base.BaseContentLayout
import com.tonyxlab.notemark.presentation.core.components.AppFloatingActionButton
import com.tonyxlab.notemark.presentation.core.components.AppTopBar
import com.tonyxlab.notemark.presentation.screens.home.components.EmptyBoard
import com.tonyxlab.notemark.util.SetStatusBarIconsColor
import com.tonyxlab.notemark.util.formatUserInitials
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    SetStatusBarIconsColor(darkIcons = true)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = { AppTopBar(profileInitials = formatUserInitials(state.username)) },
            floatingActionButton = { AppFloatingActionButton(modifier = Modifier.navigationBarsPadding()) { } }) {

        EmptyBoard()
    }
}