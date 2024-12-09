package com.oceloti.lemc.labtestcoroutinesflows.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainDispatcherViewModel: ViewModel() {
    var boolFlag: Boolean = false

    // viewModelScope uses MainCoroutineDispatcher.immediate by default
    fun executeOnMainThread(){
        viewModelScope.launch {
            // Simulate some work
            delay(1000L)
            boolFlag = true
        }
    }
}