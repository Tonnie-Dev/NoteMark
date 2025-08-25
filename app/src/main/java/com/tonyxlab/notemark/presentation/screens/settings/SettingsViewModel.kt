@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.workmanager.SyncRequest
import com.tonyxlab.notemark.domain.connectivity.ConnectivityObserver
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.domain.timer.tickerFlow
import com.tonyxlab.notemark.domain.usecase.LogOutUseCase
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState
import com.tonyxlab.notemark.util.toLastSyncLabel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeout


typealias SettingsBaseViewModel =
        BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>


class SettingsViewModel(
    private val logOutUseCase: LogOutUseCase,
    private val dataStore: DataStore,
    private val syncRequest: SyncRequest,
    private val workManager: WorkManager,
    private val connectivityObserver: ConnectivityObserver,
) : SettingsBaseViewModel() {

    override val initialState: SettingsUiState
        get() = SettingsUiState()

    init {
        updateLastSyncTimeLabel()
        observerNetwork()
        observeActiveSyncInterval()
        observeSyncProgressOnEntry()
    }

    override fun onEvent(event: SettingsUiEvent) {

        when (event) {
            SettingsUiEvent.LogOut -> logout()
            SettingsUiEvent.ExitSettings -> exitSettings()
            is SettingsUiEvent.SelectSyncInterval -> {
                selectSyncInterval(event.syncInterval)
            }

            SettingsUiEvent.ShowSyncIntervalSettings -> showSyncIntervalMenu()
            SettingsUiEvent.SyncData -> syncData()
            SettingsUiEvent.DismissSyncMenu -> dismissSyncMenu()
            SettingsUiEvent.DismissDialog -> dismissDialog()
        }
    }

    private fun observeSyncProgressOnEntry() {
        //in case we enter the settings screens while syncing
        syncRequest.manualSyncInfosFlow()
                .onEach { infos ->

                    val syncing = infos.any { !it.state.isFinished }
                    updateState { it.copy(isSyncing = syncing) }
                }
                .launchIn(viewModelScope)
    }

    private fun observerNetwork() {

        connectivityObserver.isOnline()
                .onEach { status ->

                    updateState { it.copy(isOnline = status) }

                }
                .launchIn(viewModelScope)
    }

    private fun observeActiveSyncInterval() {
        launch {
            val activeInterval = dataStore.getSyncInterval()
            setSyncInterval(activeInterval)
        }

    }

    private fun showDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes positiveButtonText: Int,
        @StringRes negativeButtonText: Int = R.string.dialog_text_cancel,
        showNegativeButton: Boolean = false
    ) {

        updateState {
            it.copy(
                    dialogState = currentState.dialogState.copy(
                            showDialog = true,
                            title = title,
                            message = message,
                            positiveText = positiveButtonText,
                            negativeText = negativeButtonText,
                            showNegativeButton = showNegativeButton
                    )
            )
        }

    }

    private fun showSyncIntervalMenu() {

        updateState { it.copy(syncMenuState = currentState.syncMenuState.copy(isMenuOpen = true)) }
    }

    private fun selectSyncInterval(interval: SyncInterval) {

        setSyncInterval(interval)

        launch {
            dataStore.saveSyncInterval(interval)
            syncRequest.enqueuePeriodicSync(interval)
        }
    }

    private fun setSyncInterval(interval: SyncInterval) {

        updateState {
            it.copy(
                    syncMenuState = currentState.syncMenuState.copy(
                            activeInterval = interval
                    )
            )
        }
    }

    private fun dismissSyncMenu() {
        updateState { it.copy(syncMenuState = currentState.syncMenuState.copy(isMenuOpen = false)) }
    }
    private fun dismissDialog() {
        updateState { it.copy(dialogState = currentState.dialogState.copy(showDialog = false)) }
    }
    private fun syncData() {

        updateState { it.copy(isSyncing = true) }
        val requestId = syncRequest.enqueueManualSync()

        workManager.getWorkInfoByIdFlow(requestId)
                .onEach { info ->
                    when {
                        info?.state?.isFinished == true -> {

                            updateState { it.copy(isSyncing = false) }
                        }
                    }

                }
                .launchIn(viewModelScope)

    }

    suspend fun runManualSyncAndAwait(timeoutMillis: Long = 60_000): Boolean {

        val workId = syncRequest.enqueueManualSync()

        return withTimeout(timeMillis = timeoutMillis) {
            workManager.getWorkInfoByIdFlow(workId)
                    .filter { it != null }
                    .map { it!! }
                    .first { it.state.isFinished }.state.isFinished
        }
    }


    private fun updateLastSyncTimeLabel() {
        launch {

            val lastSyncFlow = dataStore.getLastSyncTimeInMillis()
            val tickerFlow = tickerFlow(emitAfter = 60_000L)
            combine(lastSyncFlow, tickerFlow) { lastSyncMillis, _ ->

                updateState {
                    it.copy(
                            lastSyncTime = lastSyncMillis?.toLastSyncLabel() ?: "Never Synced"
                    )
                }
            }.launchIn(viewModelScope)

        }
    }

    private fun exitSettings() {
        sendActionEvent(SettingsActionEvent.ExitSettings)
    }

    private fun logout() {
        launchCatching(
                onStart = { updateState { it.copy(isLoggingOut = true) } },
                onCompletion = { updateState { it.copy(isLoggingOut = false) } },
                onError = {

                    showDialog(
                            title = R.string.dialog_text_sync_error,
                            message = R.string.dialog_text_sync_error_msg,
                            positiveButtonText = R.string.dialog_text_log_out,
                            negativeButtonText = R.string.dialog_text_cancel,
                            showNegativeButton = true

                    )
                    /*sendActionEvent(
                            SettingsActionEvent.ShowDialog(
                                    title = R.string.dialog_text_sync_error,
                                    message = R.string.dialog_text_sync_error_msg,
                                    positiveText = R.string.dialog_text_log_out,
                                    negativeText = R.string.dialog_text_cancel,
                                    showNegativeButton = true
                            )
                    )*/
                }
        ) {

            if (!currentState.isOnline) {

                showDialog(
                        title = R.string.dialog_text_no_internet,
                        message = R.string.dialog_text_no_internet_msg,
                        positiveButtonText = R.string.dialog_text_ok,
                        showNegativeButton = false
                )
              /*  sendActionEvent(
                        SettingsActionEvent.ShowDialog(
                                title = R.string.dialog_text_no_internet,
                                message = R.string.dialog_text_no_internet_msg,
                                positiveText = R.string.dialog_text_ok,
                                showNegativeButton = false
                        )
                )*/
                return@launchCatching
            }

            val isSyncFinished = runManualSyncAndAwait()

            if (!isSyncFinished) {

                showDialog(
                        title = R.string.dialog_text_unsynced_changes,
                        message = R.string.dialog_text_unsynced_changes_msg,
                        positiveButtonText = R.string.dialog_text_log_out_without_syncing,
                        negativeButtonText = R.string.dialog_text_sync_now,
                        showNegativeButton = true
                )
            }
            when (val result = logOutUseCase()) {

                is Resource.Success -> {
                    if (result.data) {
                        exitSettings()
                    } else {
                        sendActionEvent(
                                SettingsActionEvent.ShowSnackbar(
                                        messageRes = R.string.snack_text_logout_incomplete,
                                        labelRes = R.string.snack_text_ok
                                )
                        )
                    }

                }

                is Resource.Error -> throw result.exception
                else -> Unit
            }
        }
    }
}