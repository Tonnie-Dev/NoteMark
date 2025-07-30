package com.tonyxlab.notemark.util

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity

fun ComponentActivity.enterReaderMode(){

    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun ComponentActivity.exitReaderMode(){

    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}