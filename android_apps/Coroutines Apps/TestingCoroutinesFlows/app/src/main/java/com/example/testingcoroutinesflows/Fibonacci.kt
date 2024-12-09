package com.example.testingcoroutinesflows

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

suspend fun fib(n: Int, dispatcher: CoroutineDispatcher): Int {
    delay(5000L)
    return withContext(dispatcher) {
        if(n<=1) {
            n
        } else {
            fib(n-1, dispatcher) + fib(n-2, dispatcher)
        }
    }
}