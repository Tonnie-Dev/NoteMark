package com.tonyxlab.notemark.util

import androidx.compose.ui.tooling.preview.Preview

private const val MOBILE_WIDTH = 350
private const val MOBILE_HEIGHT = 800

private const val TABLET_WIDTH = 600
private const val TABLET_HEIGHT = 900

private const val DESKTOP_WIDTH = 1500
private const val DESKTOP_HEIGHT = 900

@Preview(showBackground = true, widthDp = MOBILE_WIDTH, heightDp = MOBILE_HEIGHT)
annotation class MobilePortrait

@Preview(showBackground = true, widthDp = MOBILE_HEIGHT, heightDp = MOBILE_WIDTH)
annotation class MobileLandscape

@MobilePortrait @MobileLandscape
annotation class MobilePreviews

@Preview(showBackground = true, widthDp = TABLET_WIDTH, heightDp = TABLET_HEIGHT)
annotation class TabletPortrait

@Preview(showBackground = true, widthDp = TABLET_HEIGHT, heightDp = TABLET_WIDTH)
annotation class TabletLandscape

@TabletPortrait @TabletLandscape
annotation class TabletPreviews

@Preview(showBackground = true, widthDp = DESKTOP_WIDTH, heightDp = DESKTOP_HEIGHT)
annotation class DesktopPortrait

@Preview(showBackground = true, widthDp = DESKTOP_HEIGHT, heightDp = DESKTOP_WIDTH)
annotation class DesktopLandscape

@DesktopPortrait @DesktopLandscape
annotation class DesktopPreviews