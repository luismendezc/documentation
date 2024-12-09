package com.example.flow_fundamentals

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun stateFlowDemo() {
    val stateFlow = MutableStateFlow(0)
    stateFlow.onEach {
        println("Value is $it")
    }.launchIn(GlobalScope)

    GlobalScope.launch {
        repeat(10){
            delay(500L)
            stateFlow.value = it
        }
    }
}