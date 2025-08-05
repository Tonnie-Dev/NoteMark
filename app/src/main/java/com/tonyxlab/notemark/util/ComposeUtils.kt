package com.tonyxlab.notemark.util

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import timber.log.Timber

inline fun Modifier.ifThen(flag: Boolean, modifierBuilder: Modifier.() -> Modifier): Modifier =
    if (flag) this.modifierBuilder() else this


@Composable
fun SetStatusBarIconsColor(darkIcons: Boolean) {
    Timber.tag("ComposeUtils")
            .i("SetStatusBarIconsColor called with darkIcons = $darkIcons")

    val view = LocalView.current

    val insetsController = remember(view) {
        (view.context as? Activity)
                ?.window
                ?.let { WindowCompat.getInsetsController(it, it.decorView) }
    }

    SideEffect {
        insetsController?.isAppearanceLightStatusBars = darkIcons
        Timber.tag("ComposeUtils")
                .i("Status bar icons set to ${if (darkIcons) "DARK" else "LIGHT"}")
    }
}

@Composable
fun IntSize.toDpSize(): DpSize {
    return DpSize(
            width = with(LocalDensity.current) { width.toDp() },
            height = with(LocalDensity.current) { height.toDp() }
    )
}

@Composable
fun InvisibleSpacer(
    componentSize: IntSize,
    modifier: Modifier = Modifier
) {
    Spacer(
            modifier = modifier
                    .size(with(LocalDensity.current) {
                        componentSize.toDpSize()
                    })
    )
}
