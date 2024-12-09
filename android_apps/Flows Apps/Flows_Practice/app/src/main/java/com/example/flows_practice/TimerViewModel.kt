package com.example.flows_practice

import android.annotation.SuppressLint
import androidx.compose.material3.rememberTooltipState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.stateIn

class TimerViewModel: ViewModel() {
    val formattedTime = timeAndEmit(10f)
        .runningReduce { totalElapsedTime, newElapsedTime ->
            totalElapsedTime + newElapsedTime
        }.map { totalElapsedTime ->
            totalElapsedTime.toComponents { hours, minutes, seconds, nanoseconds ->
                String.format(
                    "%02d:%02d:%02d:%02d",
                    hours,
                    minutes,
                    seconds,
                    nanoseconds / (1_000_000L * 10L)
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            "00:00:00:00"
        )

    val totalProgressTimeMillis = 5000L
    val progress = timeAndEmit(100f)
        .runningReduce { totalElapsedTime, newElapsedTime ->
            totalElapsedTime + newElapsedTime
        }.map { totalDuration ->
            (totalDuration.inWholeMilliseconds / totalProgressTimeMillis.toFloat())
                .coerceIn(0f, 1f)
        }.filter { progressFraction ->
            progressFraction in (0f .. 1f)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            0f

        )
}