package com.example.flow_fundamentals

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun sharedFlowDemo() {
    val sharedFlow = MutableSharedFlow<Int>(
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    GlobalScope.launch {
        delay(3000L)
        sharedFlow.onEach {
            println("Collector 1: $it")
        }.launchIn(this)
        sharedFlow.onEach {
            println("Collector 2: $it")
        }.launchIn(this)
    }
    GlobalScope.launch {
        repeat(10) {
            delay(500L)
            sharedFlow.emit(it)
        }
    }
}