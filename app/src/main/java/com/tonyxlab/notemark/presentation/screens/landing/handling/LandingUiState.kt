package com.tonyxlab.notemark.presentation.screens.landing.handling

import com.tonyxlab.notemark.domain.model.Resource
import com.tonyxlab.notemark.presentation.core.base.handling.UiState

data class LandingUiState(
    val isShowDemo: Boolean = false,
    val loginStatus: Resource<Int> = Resource.Empty
) : UiState
