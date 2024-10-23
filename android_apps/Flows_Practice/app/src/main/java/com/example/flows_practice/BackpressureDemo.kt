package com.example.flows_practice

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun backpressureDemo() {
    GlobalScope.launch {
        flow<String> {
            println("FOOD: Preparing appetizer")
            delay(1000L)
            emit("Appetizer")

            println("FOOD: Preparing main dish")
            delay(1000L)
            emit("Main dish")

            println("FOOD: Preparing dessert")
            delay(500L)
            emit("Dessert")
        }.collectLatest { dish ->
            println("FOOD: Strat eating $dish")
            delay(2500L)
            println("FOOD: Finished eating $dish")
        }
    }
}