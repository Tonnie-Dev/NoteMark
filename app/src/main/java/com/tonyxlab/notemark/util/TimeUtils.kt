@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun LocalDateTime.toHomeDisplayDate(): String {
    val currentYear = LocalDateTime.now().year
    val pattern = if (this.year == currentYear) "dd MMM" else "dd MMM yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)

}

fun LocalDateTime.localDateTimeToMillis(): Long {

    return this.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {

    return Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
}

fun LocalDateTime.toCreatedOnMetaData(): String {

    val pattern = "dd MMM yyyy, HH:mm"
    val formatter =
        DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

fun LocalDateTime.toLastEditedMetaData(): String {

    val now = LocalDateTime.now()
    val duration = Duration.between(this, now)

    return if (duration.toMinutes() < 5) {
        "Just now"
    } else {
        val pattern = "dd MMM yyyy, HH:mm"
        val formatter =
            DateTimeFormatter.ofPattern(pattern)
        formatter.format(this)
    }
}


