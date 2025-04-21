package com.example.test_coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    var status: Boolean = false,
    var num: Int = 0,
) : ViewModel() {


    fun execute() {
        viewModelScope.launch {
            status = true
            delay(1000L)
            status = false
        }
    }

    fun executeFail() {
        viewModelScope.launch {
            delay(1000L)
            status = true
        }
    }

    fun executeMultipleCorotuinesNested() {
        viewModelScope.launch {
            num = 1
            println(num)
            launch {
                delay(1000L)
                num = 2
                println(num)
            }
            num = 3
            println(num)
        }
    }

}