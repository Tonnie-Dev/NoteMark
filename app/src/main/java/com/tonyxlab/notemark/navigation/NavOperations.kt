@file:RequiresApi(Build.VERSION_CODES.O)
package com.tonyxlab.notemark.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


class NavOperations(val navHostController: NavHostController) {

    fun navigateToDestination(destination: Destinations) {

        navHostController.navigate(destination)
    }

    fun navigateToLandingScreenDestination() {

        navHostController.navigate(Destinations.LandingScreenDestination)
    }

    fun navigateToLoginScreenDestination() {

        navHostController.navigate(Destinations.LoginScreenDestination)
    }

    fun navigateToSignupScreenDestination() {

        navHostController.navigate(Destinations.SignupScreenDestination)
    }

    fun navigateToHomeScreenDestination() {

        navHostController.navigate(Destinations.HomeScreenDestination)

    }

    fun navigateToEditorScreenDestination(id: Long) {

        navHostController.navigate(Destinations.EditorScreenDestination(id = id))

    }

    fun navigateToSettingsScreenDestination() {

        navHostController.navigate(Destinations.SettingsScreenDestination)

    }

    fun popBackStack() {

        navHostController.popBackStack()
    }
}


@Composable
fun rememberNavOperations(
    navController: NavHostController = rememberNavController()
):NavOperations {
    return remember { NavOperations(navController) }
}
