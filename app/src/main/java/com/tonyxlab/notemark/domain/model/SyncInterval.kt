package com.tonyxlab.notemark.domain.model

enum class SyncInterval {
    MANUAL,
    MIN_15,
    MIN_30,
    MIN_60
}

fun SyncInterval.toMinutes(): Int = when (this) {
    SyncInterval.MANUAL -> 0
    SyncInterval.MIN_15 -> 15
    SyncInterval.MIN_30 -> 30
    SyncInterval.MIN_60 -> 60
}

fun SyncInterval.toPrefsInt(): Int = this.toMinutes()

private val codeToInterval: Map<Int, SyncInterval> =
    SyncInterval.entries.associateBy { it.toMinutes() }

fun Int.toSyncInterval(): SyncInterval =
    codeToInterval[this] ?: SyncInterval.MANUAL

