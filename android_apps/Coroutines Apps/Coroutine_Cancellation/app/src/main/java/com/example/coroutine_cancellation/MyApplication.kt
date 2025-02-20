package com.example.coroutine_cancellation

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
}