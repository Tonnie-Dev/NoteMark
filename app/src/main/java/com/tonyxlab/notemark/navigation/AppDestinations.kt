@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tonyxlab.notemark.navigation.Destinations.LandingScreenDestination
import com.tonyxlab.notemark.presentation.screens.editor.EditorScreen
import com.tonyxlab.notemark.presentation.screens.home.HomeScreen
import com.tonyxlab.notemark.presentation.screens.landing.LandingScreen
import com.tonyxlab.notemark.presentation.screens.login.LoginScreen
import com.tonyxlab.notemark.presentation.screens.settings.SettingsScreen
import com.tonyxlab.notemark.presentation.screens.signup.SignupScreen
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

    composable<Destinations.SignupScreenDestination> {

        SignupScreen(
                modifier = modifier,
                navOperations = navOperations
        )
    }

    composable<Destinations.HomeScreenDestination> {

        HomeScreen(
                modifier = modifier,
                navOperations = navOperations
        )
    }

    composable<Destinations.EditorScreenDestination> {

        EditorScreen(
                modifier = modifier,
                navOperations = navOperations
        )
    }

    composable<Destinations.SettingsScreenDestination> {

        SettingsScreen(
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
    data object SignupScreenDestination : Destinations()

    @Serializable
    data object HomeScreenDestination : Destinations()

    @Serializable
    data class EditorScreenDestination(val id: Long) : Destinations()

    @Serializable
    data object SettingsScreenDestination : Destinations()
}
