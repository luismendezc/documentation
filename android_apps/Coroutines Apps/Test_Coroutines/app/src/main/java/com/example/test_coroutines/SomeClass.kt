package com.example.test_coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SomeClass(
    private val scope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
    var status: Boolean = false
) {
    fun launchSomething() {
        scope.launch(dispatchers.io) {
            status = true
        }
    }
}