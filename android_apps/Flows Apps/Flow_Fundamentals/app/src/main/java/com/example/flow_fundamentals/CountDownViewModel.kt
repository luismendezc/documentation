package com.example.flow_fundamentals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountDownViewModel: ViewModel() {
    private val _countdownValue = MutableStateFlow(10)
    val countdownValue = _countdownValue.asStateFlow()
    
    private var countDownJob: Job? = null

    fun startCountdown(startValue:Int) {
        countDownJob?.cancel()
            countDownJob = viewModelScope.launch {
                countdownFlow(startValue).collect { value ->
                    _countdownValue.update { value }
                }
            }


    }

    private fun countdownFlow(startValue: Int) = flow<Int> {
        var currentValue = startValue
        while(currentValue >= 0)  {
            emit(currentValue)
            delay(1000L)
            currentValue--
        }
    }

}