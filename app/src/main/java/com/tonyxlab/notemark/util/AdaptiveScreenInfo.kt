package com.tonyxlab.notemark.util

enum class DeviceType {


    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {

        fun fromWindowSizeClass(windowSizeClass: androidx.window.core.layout.WindowSizeClass): DeviceType {


            val widthClass = windowSizeClass.windowWidthSizeClass
            val heightClass = windowSizeClass.windowHeightSizeClass
            return when {


                widthClass == androidx.window.core.layout.WindowWidthSizeClass.COMPACT && heightClass == androidx.window.core.layout.WindowHeightSizeClass.MEDIUM -> MOBILE_PORTRAIT

                widthClass == androidx.window.core.layout.WindowWidthSizeClass.EXPANDED && heightClass == androidx.window.core.layout.WindowHeightSizeClass.COMPACT -> MOBILE_LANDSCAPE

                widthClass == androidx.window.core.layout.WindowWidthSizeClass.MEDIUM && heightClass == androidx.window.core.layout.WindowHeightSizeClass.EXPANDED -> TABLET_PORTRAIT

                widthClass == androidx.window.core.layout.WindowWidthSizeClass.EXPANDED && heightClass == androidx.window.core.layout.WindowHeightSizeClass.MEDIUM -> TABLET_LANDSCAPE

                else -> DESKTOP
            }
        }
    }
}