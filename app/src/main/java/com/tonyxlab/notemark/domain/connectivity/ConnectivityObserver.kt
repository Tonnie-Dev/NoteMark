package com.tonyxlab.notemark.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun isOnline():Flow<Boolean>
}