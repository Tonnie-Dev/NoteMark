package com.tonyxlab.notemark.data.remote.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.tonyxlab.notemark.domain.connectivity.ConnectivityObserver
import com.tonyxlab.notemark.util.isValidatedConnection


import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class ConnectivityObserverImpl(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {

    override fun isOnline(): Flow<Boolean> = callbackFlow {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                trySend(capabilities.isValidatedConnection())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(networkCapabilities.isValidatedConnection())
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onUnavailable() {
                trySend(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }

    }.distinctUntilChanged()
}