
@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.util
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun LocalDateTime.toNoteDate(): String {
    val currentYear = LocalDateTime.now().year
    val pattern = if (this.year == currentYear) "dd MMM" else "dd MMM yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)

}

fun LocalDateTime.localDateTimeToMillis(): Long {

    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {

    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}