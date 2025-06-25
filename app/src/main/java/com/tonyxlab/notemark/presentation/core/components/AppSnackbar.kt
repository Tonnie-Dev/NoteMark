package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults.actionContentColor
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TextButton
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
    val contentColor = contentColorFor(containerColor)

    SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier.padding(MaterialTheme.spacing.spaceSmall)
    ) { snackbarData ->
        Snackbar(
                snackbarData = snackbarData,
                containerColor = containerColor,
                contentColor = contentColor,
                actionColor = contentColor

        )
    }
}

@Composable
fun ShowAppSnackbar(
    triggerId: Int,
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String = "",
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {


    LaunchedEffect(triggerId) {
        if (triggerId>0) {

            Timber.i("Snackbar Value Change Launched")
            val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                onActionClick()
            }
           onDismiss
        }
    }
}