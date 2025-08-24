package com.tonyxlab.notemark.domain.timer

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun tickerFlow(emitAfter: Long): Flow<Unit> = flow {
    while (true) {
        emit(Unit)
        delay(emitAfter)
    }
}