package com.tonyxlab.notemark.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val AppColorScheme = lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        surface = Surface,
        onSurface = OnSurface,
        onSurfaceVariant = OnSurfaceVariant,
        background = SurfaceLowest,
        error = Error,
)

@Composable
fun NoteMarkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
            colorScheme = AppColorScheme,
            shapes = customMaterialShapes,
            typography = Typography,
            content = content
    )
}