package com.tonyxlab.notemark.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tonyxlab.notemark.navigation.Destinations.LandingScreenDestination
import com.tonyxlab.notemark.presentation.screens.landing.LandingScreen
import com.tonyxlab.notemark.presentation.screens.login.LoginScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.appDestinations(
    navOperations: NavOperations,
    modifier: Modifier = Modifier
) {

    composable<LandingScreenDestination> {

        LandingScreen(
                modifier = modifier,
                navOperations = navOperations
        )
    }


    composable<Destinations.LoginScreenDestination> {

        LoginScreen(
                modifier = modifier,
                navOperations = navOperations
        )
    }

}


sealed class Destinations {


    @Serializable
    data object LandingScreenDestination : Destinations()


    @Serializable
    data object LoginScreenDestination : Destinations()


    @Serializable
    data object RegistrationScreenDestination : Destinations()
}
