package com.oceloti.lemc.labtestcoroutinesflows.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommandWithoutDispatcher : CommandInterface {
    override var isComplete = false

    override fun execute() {
        CoroutineScope(Dispatchers.IO).launch {
            // Simulate work
            println("CommandWithoutDispatcher is running on IO thread")
            delay(1000L)
            isComplete = true
            println("Task completed successfully")
        }
    }
}