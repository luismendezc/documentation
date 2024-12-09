package com.example.coroutine_cancellation

import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

object RemoteService {
    suspend fun uploadFile() {
        val chunks = List(400) { it }
        println("Uploading File")
        var index = 0
        while (index < chunks.size) {
            try {
                // Simulate uploading a single chunk of data
                delay(5)
                index++
                println("Progress: ${index * 100 / chunks.size}%")
            } catch (e: Exception) {
                // SOLUTION
                coroutineContext.ensureActive()
                // END - SOLUTION
                println("Error uploading file: ${e.message}")
            }
        }
        println("Upload Complete")
    }
}