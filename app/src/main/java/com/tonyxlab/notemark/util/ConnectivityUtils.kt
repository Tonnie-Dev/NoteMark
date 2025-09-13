package com.tonyxlab.notemark.util

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_NOT_VPN
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED

fun NetworkCapabilities?.isValidatedConnection(): Boolean {
    return this?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true &&
            this.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

