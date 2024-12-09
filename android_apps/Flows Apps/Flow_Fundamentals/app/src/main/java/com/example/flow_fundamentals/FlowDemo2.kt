package com.example.flow_fundamentals

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

fun flowDemo2() {
    GlobalScope.launch {
        val flow = flow<Int> {
            delay(1000L)
            emit(1)
            delay(1000L)
            emit(2)
            delay(1000L)
            emit(3)
        }.stateIn(
            GlobalScope,
            SharingStarted.WhileSubscribed(),
            0
        )

        flow.onEach {
            println("Collector 1: $it")
        }.launchIn(GlobalScope)

        GlobalScope.launch {
            delay(5000L)
            flow.onEach {
                println("Collector 2: $it")
            }.launchIn(GlobalScope)
        }

    }
}