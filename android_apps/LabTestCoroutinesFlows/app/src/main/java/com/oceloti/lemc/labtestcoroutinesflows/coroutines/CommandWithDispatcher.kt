package com.oceloti.lemc.labtestcoroutinesflows.coroutines

import com.oceloti.lemc.labtestcoroutinesflows.util.DispatcherProvider
import com.oceloti.lemc.labtestcoroutinesflows.util.StandardDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommandWithDispatcher(private val dispatchers: DispatcherProvider = StandardDispatchers()) :
    CommandInterface {

    override var isComplete = false

    override fun execute() {
        CoroutineScope(dispatchers.io).launch {
            // Simulate some work
            println("CommandWithDispatcher is running on IO thread")
            delay(1000L)
            isComplete = true
            println("Task completed successfully")
        }
    }

}