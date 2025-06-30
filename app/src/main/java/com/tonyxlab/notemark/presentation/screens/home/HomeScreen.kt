package com.tonyxlab.notemark.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = koinViewModel ()) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {

        Text("Welcome Home:- ${state.username}")
    }
}