@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.workmanager.SyncRequest
import com.tonyxlab.notemark.domain.connectivity.ConnectivityObserver
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.domain.timer.tickerFlow
import com.tonyxlab.notemark.domain.usecase.LogoutUseCase
import com.tonyxlab.notemark.domain.usecase.SyncQueueReaderUseCase
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsDialogType
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState
import com.tonyxlab.notemark.util.toLastSyncLabel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeout
import timber.log.Timber

typealias SettingsBaseViewModel =
        BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>

class SettingsViewModel(
    private val logOutUseCase: LogoutUseCase,
    private val dataStore: DataStore,
    private val syncRequest: SyncRequest,
    private val workManager: WorkManager,
    private val connectivityObserver: ConnectivityObserver,
    private val syncQueueReaderUseCase: SyncQueueReaderUseCase
) : SettingsBaseViewModel() {

    override val initialState: SettingsUiState
        get() = SettingsUiState()

    init {
        updateLastSyncTimeLabel()
        observeNetwork()
        observeActiveSyncInterval()
        observeSyncProgressOnEntry()
    }

    override fun onEvent(event: SettingsUiEvent) {

        when (event) {
            is SettingsUiEvent.SelectSyncInterval -> {
                selectSyncInterval(event.syncInterval)
            }

            SettingsUiEvent.ShowSyncIntervalSettings -> showSyncIntervalMenu()

            SettingsUiEvent.PositiveButtonClick -> onClickDialogPositiveButton()
            SettingsUiEvent.NegativeButtonClick -> onClickDialogNegativeButton()

            SettingsUiEvent.DismissSyncMenu -> dismissSyncMenu()
            SettingsUiEvent.DismissDialog -> dismissDialog()

            SettingsUiEvent.SyncData -> syncData()

            SettingsUiEvent.ExitSettings -> exitSettings()

            SettingsUiEvent.LogOut -> {
                if (currentState.isSyncing) return
                initiateLogoutSequence()
            }
        }
    }

    private fun onClickDialogPositiveButton() {

        val dialogType = currentState.dialogState.dialogType
        dismissDialog()

        when (dialogType) {
            is SettingsDialogType.UnSyncedChanges -> logoutWithoutSyncing()
            SettingsDialogType.SyncError -> logoutWithoutSyncing()
            SettingsDialogType.NoInternet, null -> Unit
        }
    }

    private fun onClickDialogNegativeButton() {

        val dialogType = currentState.dialogState.dialogType
        dismissDialog()

        when (dialogType) {
            is SettingsDialogType.UnSyncedChanges -> logoutWithSync()
            SettingsDialogType.SyncError -> Unit
            SettingsDialogType.NoInternet, null -> Unit
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

    private fun observeNetwork() {

        connectivityObserver.isOnline()
                .distinctUntilChanged()
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

    private fun updateDialogState(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes positiveButtonText: Int,
        @StringRes negativeButtonText: Int = R.string.dialog_text_cancel,
        dialogType: SettingsDialogType? = null
    ) {

        updateState {
            it.copy(
                    dialogState = it.dialogState.copy(
                            showDialog = true,
                            dialogTitle = title,
                            dialogText = message,
                            positiveButtonText = positiveButtonText,
                            negativeButtonText = negativeButtonText,
                            dialogType = dialogType
                    )
            )
        }
    }

    private fun showDialog(dialogType: SettingsDialogType) {

        when (dialogType) {
            SettingsDialogType.NoInternet -> {

                updateDialogState(
                        title = R.string.dialog_text_no_internet,
                        message = R.string.dialog_text_no_internet_msg,
                        positiveButtonText = R.string.dialog_text_ok,
                        dialogType = SettingsDialogType.NoInternet
                )
            }

            is SettingsDialogType.UnSyncedChanges -> {
                updateDialogState(
                        title = R.string.dialog_text_unsynced_changes,
                        message = R.string.dialog_text_unsynced_changes_msg,
                        positiveButtonText = R.string.dialog_text_log_out_without_syncing,
                        negativeButtonText = R.string.dialog_text_sync_now,
                        dialogType = SettingsDialogType.UnSyncedChanges()
                )
            }

            SettingsDialogType.SyncError -> {
                updateDialogState(
                        title = R.string.dialog_text_sync_error,
                        message = R.string.dialog_text_sync_error_msg,
                        positiveButtonText = R.string.dialog_text_log_out_without_syncing,
                        negativeButtonText = R.string.dialog_text_cancel,
                        dialogType = SettingsDialogType.SyncError
                )
            }
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
        updateState {
            it.copy(
                    dialogState = currentState.dialogState.copy(
                            showDialog = false,
                            dialogType = null
                    )
            )
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

    private fun syncData() {
        updateState { it.copy(syncInProgress = true) }
        val workId = syncRequest.enqueueManualSync()

        workManager.getWorkInfoByIdFlow(workId)
                .filterNotNull()
                .onEach { info ->
                    if (info.state.isFinished) {
                        updateState { it.copy(syncInProgress = false) }
                    }
                }
                .launchIn(viewModelScope)

    }

    private suspend fun runManualSyncAndAwait(timeoutMillis: Long = 60_000): Boolean {

        val workId = syncRequest.enqueueManualSync()

        return withTimeout(timeMillis = timeoutMillis) {
            workManager.getWorkInfoByIdFlow(workId)
                    .filterNotNull()
                    .first { it.state.isFinished }
                    .state == WorkInfo.State.SUCCEEDED
        }
    }

    private suspend fun isSyncQueueEmpty(): Boolean {
        return syncQueueReaderUseCase()
    }

    private fun initiateLogoutSequence() = launchCatching(
            onStart = { updateState { it.copy(isLoggingOut = true) } },
            onCompletion = { updateState { it.copy(isLoggingOut = false) } },
            onError = {
                showDialog(dialogType = SettingsDialogType.SyncError)
                return@launchCatching
            }
    ) {

        if (!currentState.isOnline) {
            showDialog(dialogType = SettingsDialogType.NoInternet)
            return@launchCatching
        }

        val hasUnSyncedChanges = isSyncQueueEmpty().not()
        Timber.tag("SettingsViewModel")
                .i("is queue empty?: ${isSyncQueueEmpty()} ")
        Timber.tag("SettingsViewModel")
                .i("hasUnSynced: $hasUnSyncedChanges")
        if (hasUnSyncedChanges) {
            showDialog(dialogType = SettingsDialogType.UnSyncedChanges())
            return@launchCatching
        }

        logoutWithSync()
    }

    private fun logoutWithSync() = launchCatching(
            onError = {
                showDialog(dialogType = SettingsDialogType.SyncError)
                return@launchCatching
            }
    ) {

        val ok = runManualSyncAndAwait()

        if (!ok) {
            showDialog(dialogType = SettingsDialogType.SyncError)
            return@launchCatching
        }

        logoutHelper(preserveSyncQueue = false)
    }

    private fun logoutWithoutSyncing() = launchCatching(
            onStart = { updateState { it.copy(isLoggingOut = true) } },
            onCompletion = { updateState { it.copy(isLoggingOut = false) } },
            onError = {
                showDialog(dialogType = SettingsDialogType.SyncError)
                return@launchCatching
            }
    ) {
        logoutHelper(preserveSyncQueue = true)
    }

    private suspend fun logoutHelper(preserveSyncQueue: Boolean) {

        when (val result = logOutUseCase(preserveSyncQueue = preserveSyncQueue)) {

            is Resource.Success -> if (result.data) {
                exitSettingsAndClearBackStack()
            }

            is Resource.Error -> {
                showDialog(dialogType = SettingsDialogType.SyncError)
            }

            else -> Unit
        }

    }

    private fun exitSettings() {
        sendActionEvent(SettingsActionEvent.ExitSettings)
    }

    private fun exitSettingsAndClearBackStack() {
        sendActionEvent(SettingsActionEvent.Logout)
    }
}