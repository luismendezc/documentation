@file:OptIn(ExperimentalStdlibApi::class)

package com.example.coroutine_contexts

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

suspend fun queryDatabase() {
    val job = coroutineContext[Job]
    val name = coroutineContext[CoroutineName]
    val handler = coroutineContext[CoroutineExceptionHandler]
    val dispatcher = coroutineContext[CoroutineDispatcher]

    println("Job: $job")
    println("Name: $name")
    println("Handler: $handler")
    println("Dispatcher: $dispatcher")

    CoroutineScope(Dispatchers.Main + CoroutineName("Cool coroutine!")).launch {
        println("Dispatcher: ${coroutineContext[CoroutineDispatcher]}")
        println("Name: ${coroutineContext[CoroutineName]}")
    }
}