package com.example.test_coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthCommandWithDispatcher(private val dispatchers: DispatcherProvider = StandardDispatchers) {

    var isComplete = false

    fun execute() {
        CoroutineScope(dispatchers.io).launch {
            // Simulate some work
            println("AuthCommandWithDispatcher is running on IO thread")
            delay(1000L)
            isComplete = true
            println("Task completed successfully")
        }
    }

}
