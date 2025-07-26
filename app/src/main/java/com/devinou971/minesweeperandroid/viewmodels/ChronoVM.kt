package com.devinou971.minesweeperandroid.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ChronoVM : ViewModel() {
    var currentTime: Duration by mutableStateOf(Duration.ZERO)
        private set

    private var job: Job? = null
    private val running get() = job != null

    private companion object {
        val INCREMENT = 1.seconds
    }

    fun start() {
        if (running) return

        job = viewModelScope.launch {
            while (true) {
                delay(INCREMENT)
                currentTime += INCREMENT
            }
        }
    }

    fun stop() {
        if (!running) return
        job!!.cancel()
        job = null
    }

    fun reset() {
        stop()
        currentTime = Duration.ZERO
    }
}