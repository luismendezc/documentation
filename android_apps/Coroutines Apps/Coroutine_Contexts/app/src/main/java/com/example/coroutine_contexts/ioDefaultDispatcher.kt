package com.example.coroutine_contexts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

fun ioDefaultDispatcher() {
    val threads = hashMapOf<Long, String>()
    val job = GlobalScope.launch(Dispatchers.Default) {
        repeat(100) {
            launch {
                threads[Thread.currentThread().id] = Thread.currentThread().name
                (1..100_000).map {
                    it * it
                }
            }
        }
    }
    GlobalScope.launch {
        val timeMillis = measureTimeMillis {
            job.join()
        }
        println("Launcher ${threads.keys.size} threads in $timeMillis ms.")
    }
}