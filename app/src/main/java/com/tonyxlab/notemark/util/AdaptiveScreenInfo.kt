package com.tonyxlab.notemark.util

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.window.Popup

enum class DeviceType{


    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {

        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceType {


            val widthClass = windowSizeClass.widthSizeClass
            val heightClass = windowSizeClass.heightSizeClass
            return when{



                widthClass == WindowWidthSizeClass.Compact && heightClass == WindowHeightSizeClass.Medium -> MOBILE_PORTRAIT

                widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Compact -> MOBILE_LANDSCAPE

                widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded ->TABLET_PORTRAIT

               widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Medium -> TABLET_LANDSCAPE

               else -> DESKTOP
            }
        }
    }
}