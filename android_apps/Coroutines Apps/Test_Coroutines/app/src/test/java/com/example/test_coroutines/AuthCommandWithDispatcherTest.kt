package com.example.test_coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthCommandWithDispatcherTest {
    private val testDispatchers = TestDispatchers()

    /*
      advanceUntilIdle() – Runs everything until there’s nothing left to run (including scheduled delay).
      runCurrent() – Runs only what's ready to run right now, without advancing virtual time.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test execute with dispatcher control`() = runTest(testDispatchers.testDispatcher) {
        // Arrange
        val sut = AuthCommandWithDispatcher(testDispatchers)

        // Act
        sut.execute()
        runCurrent()

        // Assert
        assertFalse(sut.isComplete)
        // Fast-forward time to ensure the coroutine completes
        advanceUntilIdle()
        //advanceTimeBy(1005L)
        assertTrue(sut.isComplete)
        println("Test for AuthCommandWithDispatcher completed")
    }
}

