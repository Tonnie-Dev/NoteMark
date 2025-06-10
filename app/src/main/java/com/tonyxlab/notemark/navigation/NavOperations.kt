package com.tonyxlab.notemark.navigation

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
