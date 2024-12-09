package com.oceloti.lemc.labtestcoroutinesflows

import com.oceloti.lemc.labtestcoroutinesflows.util.DispatcherProvider
import com.oceloti.lemc.labtestcoroutinesflows.util.StandardDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class CoroutineFlowTimeOutImpl(
    private val timeOut: Long = 30000L,
    private val dispatchers: DispatcherProvider = StandardDispatchers()
) : CoroutineFlowTimeOut {
    override val initializationStateFlow = MutableStateFlow(State.a)

    override fun execute(callback: MyCallback) {
        CoroutineScope(dispatchers.io).launch {
            try {
                withTimeout(timeOut) {
                    initializationStateFlow.collect { state ->
                        handleState(state, callback)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                callback.onError(Exception("Timed out"))
            }
        }
    }

    override suspend fun handleState(state: State, callback: MyCallback) {
        when (state) {
            State.a -> {
                //Nothing
            }

            State.b -> {
                callback.onSuccess().also { coroutineScope { cancel() } }
            }

            State.c -> {
                fakeSuspendFunction()
                callback.onError(Exception("Error, State.c")).also { coroutineScope { cancel() } }
            }

            State.d -> {
                //Nothing
            }
        }


    }

    override fun emitValue(state: State): Boolean {
        //it’s a method call that returns true if the update succeeds, or false if the flow couldn’t
        // be updated
        return initializationStateFlow.tryEmit(state)
        /*
        Best for atomic transformations based on the current state, ensuring thread-safe updates,
        especially in concurrent environments.
        initializationStateFlow.update { state }

        Use when you need a simple, immediate update in a non-concurrent environment. It’s direct
        and effective for controlled updates.
        initializationStateFlow.value = state

        Use when you need suspension, waiting for collectors to receive the new value, making it
        suitable for controlled flow interactions.
        initializationStateFlow.emit(state)
        */
    }

    private suspend fun fakeSuspendFunction() {
        delay(1000L)
    }

}

enum class State {
    a, b, c, d
}

interface MyCallback {
    fun onSuccess()
    fun onError(error: Exception)
}

