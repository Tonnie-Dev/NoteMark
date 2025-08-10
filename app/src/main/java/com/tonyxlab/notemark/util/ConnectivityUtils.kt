package com.tonyxlab.notemark.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import com.tonyxlab.notemark.domain.connectivity.ConnectivityObserver

fun ConnectivityManager.isConnectionAvailable(status: ConnectivityObserver.Status): Boolean {

    val capabilities = getNetworkCapabilities(activeNetwork) ?: return false

    return capabilities.isValidatedConnection()
            &&
            status == ConnectivityObserver.Status.AVAILABLE
}


fun NetworkCapabilities?.isValidatedConnection(): Boolean {

    return this?.hasCapability(NET_CAPABILITY_VALIDATED) == true
    // If you really need to exclude VPNs, add:
            //&& this.hasCapability(NET_CAPABILITY_NOT_VPN)
}


//val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
