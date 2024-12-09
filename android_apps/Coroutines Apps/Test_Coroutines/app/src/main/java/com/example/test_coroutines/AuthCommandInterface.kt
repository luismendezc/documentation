package com.example.test_coroutines

interface AuthCommandInterface {
    var isComplete: Boolean

    fun execute()

}