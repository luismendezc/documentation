@file:OptIn(ExperimentalCoroutinesApi::class)

package com.oceloti.lemc.labtestcoroutinesflows

import com.oceloti.lemc.labtestcoroutinesflows.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch

class CoroutineFlowTimeOutImplTest {

    private val testDispatchers = TestDispatchers()

    /**
     *   Level: Hard
     *
     *   Case: Testing coroutine flow interactions with controlled dispatchers under a timeout.
     *   This test evaluates how well the coroutine execution manages state changes and timeout
     *   handling when processing a sequence of states with a countdown latch.
     *
     *   Solution:
     *   - `runTest` with a custom dispatcher (`TestDispatchers`) enables test-controlled coroutine
     *      behavior.
     *   - `runCurrent()` is used after each state emission to process only the current coroutine
     *      tasks.
     *     This is necessary to avoid advancing the virtual time, which could trigger the
     *     `withTimeout` prematurely and cause a `TimeoutCancellationException`.
     *   - `advanceUntilIdle()` is only called after processing `State.b` to ensure that all remaining
     *     coroutine actions complete, specifically confirming `onSuccess` has been called, which
     *     then cancel the coroutine.
     *   - The latch and assertions confirm that all expected actions have taken place without
     *     the coroutine prematurely cancelling due to timeout.
     */
    @Test
    fun `test execute SUCCESS`() = runTest(testDispatchers.testDispatcher) {
        // Arrange
        val latch = CountDownLatch(1)
        val coroutineFlowTimeOut = CoroutineFlowTimeOutImpl(dispatchers = testDispatchers)
        // Act
        coroutineFlowTimeOut.execute(object : MyCallback {
            override fun onSuccess() {
                latch.countDown()
            }
            override fun onError(error: Exception) {
                fail("MyCallback.onError() must not be called")
            }
        })
        coroutineFlowTimeOut.emitValue(State.d)
        // If we call advanceUntilIdle() will also end the withTimeout coroutine leading to a
        // TimeoutCancellationException
        runCurrent()
        coroutineFlowTimeOut.emitValue(State.b)
        advanceUntilIdle()
        // Assert
        latch.run {
            assertEquals(0, count)
        }
    }
}