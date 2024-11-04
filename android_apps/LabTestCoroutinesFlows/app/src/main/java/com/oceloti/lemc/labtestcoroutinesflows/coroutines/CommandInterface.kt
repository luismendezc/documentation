package com.oceloti.lemc.labtestcoroutinesflows.coroutines

interface CommandInterface {
    var isComplete: Boolean
    fun execute()
}