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


