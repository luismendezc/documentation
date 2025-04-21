package com.example.test_coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class UnconfinedExample(
    var status: Boolean = false,
    var num: Int = 0,
    private val scope: CoroutineScope
) {
    fun execute() {
        status = true
        num = 1
        scope.launch {
            status = false
            //delay(500)
            launch { num = 2 }
        }
    }
}