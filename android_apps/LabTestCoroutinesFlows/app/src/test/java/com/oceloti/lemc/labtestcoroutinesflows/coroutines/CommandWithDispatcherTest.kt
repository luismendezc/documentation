@file:OptIn(ExperimentalCoroutinesApi::class)

package com.oceloti.lemc.labtestcoroutinesflows.coroutines

import com.oceloti.lemc.labtestcoroutinesflows.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class CommandWithDispatcherTest {
    private val testDispatchers = TestDispatchers()
    private lateinit var command: CommandInterface

    @Before
    fun setUp() {
        command = CommandWithDispatcher(testDispatchers)
    }

    /**
     *   Level: Medium
     *
     *   Case: coroutine scopes launch coroutines (viewModelScope, lifecycleScope, GlobalScope,
     *   CoroutineScope).
     *   Use of Default or IO Dispatchers.
     *
     *  Solution: DispatcherProvider that overrides the default dispatchers, assign it to the
     *  runTest coroutine, and use advanceUntilIdle() to ensure the coroutine completes.
     */
    @Test
    fun `test execute with dispatcher control`() = runTest(testDispatchers.testDispatcher) {
        // Arrange

        // Act
        command.execute()
        // Fast-forward time to ensure the coroutine completes
        advanceUntilIdle()

        // Assert
        assertTrue(command.isComplete)
        println("Test for AuthCommandWithDispatcher completed")
    }
}