package com.tonyxlab.notemark.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tonyxlab.notemark.navigation.AppNavHost
import com.tonyxlab.notemark.navigation.rememberNavOperations
import com.tonyxlab.notemark.presentation.core.utils.spacing
import com.tonyxlab.notemark.presentation.theme.NoteMarkTheme
import com.tonyxlab.notemark.presentation.theme.SurfaceLowest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {

            setKeepOnScreenCondition { false }
        }

        enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                        lightScrim =  Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb()

                ),
                navigationBarStyle = SystemBarStyle.light(
                        scrim = SurfaceLowest.toArgb(),
                        darkScrim = SurfaceLowest.toArgb()
                )
        )
        setContent {
            NoteMarkTheme {
                val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
                val padding = MaterialTheme.spacing.spaceDefault

                AppNavHost(
                        navOperations = rememberNavOperations(),
                        modifier = Modifier.padding(padding)
                )
            }
        }
    }
}