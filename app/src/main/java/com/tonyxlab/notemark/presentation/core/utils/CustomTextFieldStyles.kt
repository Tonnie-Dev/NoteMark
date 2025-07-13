package com.tonyxlab.notemark.presentation.core.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.tonyxlab.notemark.presentation.theme.textButtonStyle


sealed class FormTextFieldStyle() {

    @Composable
    abstract fun textColor(): Color

    @Composable
    abstract fun backgroundColor(): Color

    @Composable
    abstract fun borderColor(): Color

    @Composable
    abstract fun cursorColor(): Color

    @Composable
    abstract fun supportingTextColor(): Color

    @Composable
    abstract fun textStyle(): TextStyle


    @Composable
    abstract fun borderWidth(): Dp

    data object FormPlaceHolderStyle : FormTextFieldStyle() {

        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurfaceVariant

        @Composable
        override fun backgroundColor(): Color = MaterialTheme.colorScheme.surface

        @Composable
        override fun borderColor(): Color = MaterialTheme.colorScheme.surface

        @Composable
        override fun borderWidth(): Dp = MaterialTheme.spacing.spaceDefault

        @Composable
        override fun cursorColor(): Color = Color.Transparent

        @Composable
        override fun supportingTextColor(): Color = MaterialTheme.colorScheme.onSurfaceVariant

        @Composable
        override fun textStyle(): TextStyle =
            MaterialTheme.typography.bodyLarge.copy(color = this.textColor())
    }

    data object FormFocusedTextStyle : FormTextFieldStyle() {
        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurface

        @Composable
        override fun backgroundColor(): Color = Color.White

        @Composable
        override fun borderColor(): Color = MaterialTheme.colorScheme.primary

        @Composable
        override fun cursorColor(): Color = MaterialTheme.colorScheme.primary

        @Composable
        override fun borderWidth(): Dp = MaterialTheme.spacing.spaceSingleDp

        @Composable
        override fun supportingTextColor(): Color = MaterialTheme.colorScheme.onSurfaceVariant

        @Composable
        override fun textStyle(): TextStyle =
            MaterialTheme.typography.bodyLarge.copy(color = this.textColor())

    }

    data object FormErrorTextStyle : FormTextFieldStyle() {

        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.error

        @Composable
        override fun backgroundColor(): Color = Color.Transparent

        @Composable
        override fun borderColor(): Color = MaterialTheme.colorScheme.error

        @Composable
        override fun cursorColor(): Color = MaterialTheme.colorScheme.error

        @Composable
        override fun supportingTextColor(): Color = MaterialTheme.colorScheme.error

        @Composable
        override fun borderWidth(): Dp = MaterialTheme.spacing.spaceSingleDp

        @Composable
        override fun textStyle(): TextStyle =
            MaterialTheme.typography.bodySmall.copy(color = this.textColor())
    }
}

sealed class EditorTextFieldStyle {

    @Composable
    abstract fun textColor(): Color

    @Composable
    abstract fun cursorColor(): Color

    @Composable
    abstract fun textStyle(): TextStyle

    @Composable
    abstract fun textStateStyle(): TextStyle

    data object EditorTitleStyle : EditorTextFieldStyle() {

        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurface

        @Composable
        override fun cursorColor(): Color = MaterialTheme.colorScheme.primary

        @Composable
        override fun textStyle(): TextStyle = MaterialTheme.typography.titleLarge

        @Composable
        override fun textStateStyle(): TextStyle =
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    }


    data object EditorFocusedNoteStyle : EditorTextFieldStyle() {

        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurfaceVariant

        @Composable
        override fun cursorColor(): Color = MaterialTheme.colorScheme.primary

        @Composable
        override fun textStyle(): TextStyle =
            MaterialTheme.typography.bodyLarge.copy(color = this.textColor())

        @Composable
        override fun textStateStyle(): TextStyle =
            MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface)
    }
}