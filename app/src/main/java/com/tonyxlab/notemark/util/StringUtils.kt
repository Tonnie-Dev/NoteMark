package com.tonyxlab.notemark.util


fun formatUserInitials(username: String): String {

    val stringsList = username.trim().split(" ").filter { it.isNotBlank() }

    return when (stringsList.size) {
        0 -> ""
        1 -> stringsList[0]
                .uppercase()
                .take(2)

        else -> {
            val firstInitial = stringsList.first().first().uppercaseChar()
            val lastInitial = stringsList.last().first().uppercaseChar()
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
                "do",)

    return (0 until  wordCount).joinToString( " ") {

        loremIpsumWords.random().replaceFirstChar {it.titlecase()}
    }

}
