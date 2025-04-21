package com.example.test_coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DispatcherChecker(private val dispatcherProvider: DispatcherProvider) {

    var isDone = false
        private set

    fun executeHeavyLogic() {
        CoroutineScope(dispatcherProvider.default).launch {
            println("Heavy logic started on: ${dispatcherProvider.default}")
            delay(1000)
            isDone = true
            println("Heavy logic done")
        }
    }
}
