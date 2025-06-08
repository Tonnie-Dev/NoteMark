package com.tonyxlab.notemark.presentation.core.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle


sealed class AppTextFieldStyle() {

    @Composable
    abstract fun textStyle(): TextStyle

    @Composable
    abstract fun textColor(): Color


    @Composable
    abstract fun backgroundColor(): Color

    data object PlaceHolderStyle : AppTextFieldStyle(){
        @Composable
        override fun textStyle(): TextStyle= MaterialTheme.typography.bodyLarge


        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurfaceVariant


        @Composable
        override fun backgroundColor(): Color = MaterialTheme.colorScheme.surface


    }

    data object OnFillingTextStyle : AppTextFieldStyle(){
        @Composable
        override fun textStyle(): TextStyle= MaterialTheme.typography.bodySmall


        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurfaceVariant

        @Composable
        override fun backgroundColor(): Color = Color.Transparent

    }
    data object FilledTextStyle : AppTextFieldStyle(){
        @Composable
        override fun textStyle(): TextStyle= MaterialTheme.typography.bodyLarge


        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.onSurface

        @Composable
        override fun backgroundColor(): Color = MaterialTheme.colorScheme.surface

    }

    data object ErrorTextStyle : AppTextFieldStyle(){
        @Composable
        override fun textStyle(): TextStyle= MaterialTheme.typography.bodySmall


        @Composable
        override fun textColor(): Color = MaterialTheme.colorScheme.error

        @Composable
        override fun backgroundColor(): Color = Color.Transparent
    }
}