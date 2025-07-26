package com.tonyxlab.notemark.presentation.core.components

import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tonyxlab.notemark.presentation.core.utils.spacing
import timber.log.Timber

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    isError: Boolean = true,
) {

    val containerColor =
        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val contentColor = contentColorFor(containerColor)

    SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
                    .padding(MaterialTheme.spacing.spaceSmall)
                    .navigationBarsPadding()
    ) { snackbarData ->
        Snackbar(
                snackbarData = snackbarData,
                shape = MaterialTheme.shapes.large,
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
        if (triggerId > 0) {

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


class SnackbarController<T> {

    var triggerId by mutableIntStateOf(0)
    var message by mutableStateOf("")
    var actionLabel by mutableStateOf("")
    var actionEvent by mutableStateOf<T?>(null)

    fun showSnackbar(message: String, actionLabel: String = "", actionEvent:T? = null) {
       this.message = message
       this.actionLabel = actionLabel
       this.actionEvent = actionEvent
       triggerId ++
    }

    fun dismissSnackbar(){
        triggerId = 0
        actionEvent = null
    }

}

@Composable
fun <T> rememberSnackbarController(): SnackbarController<T> {

    return remember { SnackbarController() }
}