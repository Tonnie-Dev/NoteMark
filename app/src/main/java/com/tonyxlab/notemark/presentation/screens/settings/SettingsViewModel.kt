@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.presentation.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.workmanager.SyncRequest
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


typealias SettingsBaseViewModel =
        BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>


class SettingsViewModel(
    private val logOutUseCase: LogOutUseCase,
    private val dataStore: DataStore,
    private val syncRequest: SyncRequest,
    private val workManager: WorkManager
) : SettingsBaseViewModel() {

    override val initialState: SettingsUiState
        get() = SettingsUiState()


    init {
        observeActiveSyncInterval()
        updateLastSyncTimeLabel()
        //in case we enter the settings screens while syncing
        syncRequest.manualSyncInfosFlow().onEach { infos ->

            val syncing = infos.any { !it.state.isFinished }
            updateState { it.copy(isSyncing = syncing) }
        }.launchIn(viewModelScope)
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
        }
    }

    private fun dismissSyncMenu() {

        updateState { it.copy(syncMenuState = currentState.syncMenuState.copy(isMenuOpen = false)) }
    }

    private fun syncData() {

        updateState { it.copy(isSyncing = true) }
        val requestId = syncRequest.enqueueManualSync()

        workManager.getWorkInfoByIdFlow(requestId).onEach { info ->

            when{
                info?.state?.isFinished == true -> {

                    updateState { it.copy(isSyncing = false) }
                }
            }


        }.launchIn(viewModelScope)



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

    private fun observeActiveSyncInterval() {
        launch {
            val activeInterval = dataStore.getSyncInterval()
            setSyncInterval(activeInterval)

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
        launchCatching(onError = {
            sendActionEvent(
                    SettingsActionEvent.ShowSnackbar(
                            messageRes = R.string.snack_text_logout_failed,
                            labelRes = R.string.snack_text_retry
                    )
            )
        }
        ) {
            when (val result = logOutUseCase()) {

                is Resource.Success -> {
                    if (result.data) {
                        sendActionEvent(SettingsActionEvent.ExitSettings)
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