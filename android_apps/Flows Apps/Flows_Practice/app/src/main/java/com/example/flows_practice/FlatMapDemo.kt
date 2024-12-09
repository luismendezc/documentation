package com.example.flows_practice

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun flatMapDemo() {
    flow<Int> {
        emit(1)
        delay(1000L)
        emit(2)
        delay(1000L)
        emit(3)
    }.flatMapMerge {
        flow {
            emit("One")
            delay(1000L)
            emit("Two")
            delay(1000L)
            emit("Three")
        }
    }
        .onEach {
            println("Emission is $it")
        }
        .launchIn(GlobalScope)
}