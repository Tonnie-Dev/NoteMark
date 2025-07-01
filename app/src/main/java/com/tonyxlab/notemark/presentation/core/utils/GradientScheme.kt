package com.tonyxlab.notemark.presentation.core.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class GradientScheme(val fabGradient: Brush = FabGradient) {

    companion object {

        val FabGradient =
            Brush.verticalGradient(
                    colors = listOf(Color(0xFF58A1F8), Color(0xFF5A4CF7)),

            )

    }
}


val LocalGradient = staticCompositionLocalOf { GradientScheme() }


val MaterialTheme.gradient: GradientScheme
    @Composable
    get() = LocalGradient.current