package com.tonyxlab.notemark.domain.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountdownTimer {

    private val _remainingSecs = MutableStateFlow(0)
    val remainingSecs = _remainingSecs.asStateFlow()

    private var totalSeconds = 5L
    private var lastTimeStamp = 0L

    private var job: Job? = null

    fun start() {
        stop()
        launchTimer()
    }

    fun stop() {
        resetTimer()
    }

    private fun launchTimer() {

        job = CoroutineScope(Dispatchers.Main).launch {

            lastTimeStamp = System.currentTimeMillis()

            while (totalSeconds > 0) {
                delay(1_000)
                val elapsedSeconds = (System.currentTimeMillis() - lastTimeStamp) / 1_000
                totalSeconds = maxOf(0, totalSeconds - elapsedSeconds)
                lastTimeStamp = System.currentTimeMillis()
                _remainingSecs.value = totalSeconds.toInt()
            }
        }
    }

    private fun resetTimer() {
        job?.cancel()
        job = null
        totalSeconds = 5L
        lastTimeStamp = 0L
        _remainingSecs.value = 5
    }
}