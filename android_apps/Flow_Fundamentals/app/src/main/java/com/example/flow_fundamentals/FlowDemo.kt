package com.example.flow_fundamentals

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

suspend fun fetchUsername(id: String): String {
    delay(1000L)

    return "Username"
}

fun flowDemo() {
    GlobalScope.launch {
        flow<Int> {
            delay(1000L)
            emit(1)
            delay(1000L)
            emit(2)
            delay(1000L)
            emit(3)
        }.collect {
            println("Current value $it")
        }
    }
}