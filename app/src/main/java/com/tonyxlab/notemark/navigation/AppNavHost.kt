@file:RequiresApi(Build.VERSION_CODES.O)
package com.tonyxlab.notemark.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {
    val navController = navOperations.navHostController

    NavHost(
            navController = navController,
            startDestination = Destinations.LandingScreenDestination
    ) {
        appDestinations(
                navOperations = navOperations,
                modifier = modifier
        )
    }
}
