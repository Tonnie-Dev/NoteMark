package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tonyxlab.notemark.presentation.core.utils.spacing
import timber.log.Timber

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    isError: Boolean = true,
) {
    val containerColor = if (isError) MaterialTheme.colorScheme.error else Color.Green

    SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier.padding(MaterialTheme.spacing.spaceMedium)
    ) { snackbarData ->
        Snackbar(
                snackbarData = snackbarData,
                containerColor = containerColor,
                contentColor = contentColorFor(containerColor)
        )
    }
}

@Composable
fun ShowAppSnackbar(
    showSnackbar: Boolean,
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String = "",
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {

    Timber.i("Snackbar Value Change: $showSnackbar")
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {

            Timber.i("Snackbar Value Change Launched")
            val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed) {
                onActionClick()
            }
           onDismiss
        }
    }
}