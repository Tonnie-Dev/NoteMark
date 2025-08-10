package com.tonyxlab.notemark.presentation.screens.home.handling

import com.tonyxlab.notemark.domain.model.NoteItem
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

data class HomeUiState(
    val username: String = "",
    val notes: List<NoteItem> = emptyList(),
    val showDialog: Boolean = false,
    val isOffline: Boolean = false
) : UiState