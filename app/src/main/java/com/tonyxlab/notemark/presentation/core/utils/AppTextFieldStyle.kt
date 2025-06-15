package com.tonyxlab.notemark.presentation.core.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp


sealed class AppTextFieldStyle() {

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

    data object PlaceHolderStyle : AppTextFieldStyle() {

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
        override fun textStyle(): TextStyle = MaterialTheme.typography.bodyLarge.copy(color = this.textColor() )
    }

    data object FocusedTextStyle : AppTextFieldStyle() {
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
        override fun textStyle(): TextStyle = MaterialTheme.typography.bodyLarge.copy(color = this.textColor())

    }


    data object ErrorTextStyle : AppTextFieldStyle() {

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
        override fun textStyle(): TextStyle = MaterialTheme.typography.bodySmall.copy(color = this.textColor() )
    }
}