package com.tonyxlab.notemark.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


fun formatUserInitials(username: String): String {

    val stringsList = username.trim()
            .split(" ")
            .filter { it.isNotBlank() }

    return when (stringsList.size) {
        0 -> ""
        1 -> stringsList[0]
                .uppercase()
                .take(2)

        else -> {
            val firstInitial = stringsList.first()
                    .first()
                    .uppercaseChar()
            val lastInitial = stringsList.last()
                    .first()
                    .uppercaseChar()
            "$firstInitial$lastInitial"
        }
    }

}


fun generateLoremIpsum(wordCount: Int): String {
    val loremIpsumWords =
        listOf(
                "Lorem",
                "ipsum",
                "dolor",
                "sit",
                "amet",
                "consectetur",
                "adipiscing",
                "elit",
                "sed",
                "do",
        )

    return (0 until wordCount).joinToString(" ") {

        loremIpsumWords.random()
                .replaceFirstChar { it.titlecase() }
    }

}


fun trimTextToFit(content: String, maxCharacters: Int): String {
    return if (content.length > maxCharacters) {
        content.take(maxCharacters).trimEnd() + "…"
    } else content
}


@Composable
fun trimContentText(deviceType: DeviceType, content: String): String {
    val maxTextCharacters = remember(deviceType) {
        when (deviceType) {
            DeviceType.MOBILE_PORTRAIT,
            DeviceType.MOBILE_LANDSCAPE -> 150

            DeviceType.TABLET_PORTRAIT,
            DeviceType.TABLET_LANDSCAPE -> 250

            else -> 250
        }
    }

    return remember(content, maxTextCharacters) {
        trimTextToFit(content, maxTextCharacters)
    }
}


