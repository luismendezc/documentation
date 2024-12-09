package com.example.coroutine_contexts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun mainDispatcher() {
    withContext(Dispatchers.Main) {
        withContext(Dispatchers.Main.immediate) {

        }
    }
}