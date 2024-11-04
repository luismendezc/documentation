package com.oceloti.lemc.labtestcoroutinesflows

import kotlinx.coroutines.flow.MutableStateFlow

interface CoroutineFlowTimeOut {
    val initializationStateFlow: MutableStateFlow<State>
    fun execute(callback: MyCallback)
    fun emitValue(state: State): Boolean
    suspend fun handleState(state: State, callback: MyCallback)
}