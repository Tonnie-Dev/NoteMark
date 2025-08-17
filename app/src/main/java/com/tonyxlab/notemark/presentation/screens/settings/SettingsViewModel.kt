package com.tonyxlab.notemark.presentation.screens.settings

import com.tonyxlab.notemark.R
import com.tonyxlab.notemark.data.local.datastore.TokenStorage
import com.tonyxlab.notemark.data.workmanager.SyncRequest
import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.domain.usecase.LogOutUseCase
import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsActionEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiEvent
import com.tonyxlab.notemark.presentation.screens.settings.handling.SettingsUiState


typealias SettingsBaseViewModel =
        BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>

class SettingsViewModel(
    private val logOutUseCase: LogOutUseCase,
    private val syncRequest: SyncRequest,
) : SettingsBaseViewModel() {

    override val initialState: SettingsUiState
        get() = SettingsUiState()


    init {
        observeActiveSyncInterval()
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

        syncRequest.enqueueManualSync()
    }

    private fun showSyncIntervalMenu() {

        updateState { it.copy(syncMenuState = currentState.syncMenuState.copy(isMenuOpen = true)) }
    }

    private fun selectSyncInterval(interval: SyncInterval) {

        setSyncInterval(interval)



        launch {
            TokenStorage.saveSyncInterval(interval)
            syncRequest.enqueuePeriodicSync(interval)
        }
    }

    private fun observeActiveSyncInterval() {
        launch {


            val activeInterval = TokenStorage.getSyncInterval()
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