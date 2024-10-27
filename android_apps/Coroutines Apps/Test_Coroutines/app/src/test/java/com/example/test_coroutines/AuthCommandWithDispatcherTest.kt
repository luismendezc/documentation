package com.example.test_coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertTrue

class AuthCommandWithDispatcherTest {
    private val testDispatchers = TestDispatchers()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test execute with dispatcher control`() = runTest(testDispatchers.testDispatcher) {
        // Arrange
        val authCommand = AuthCommandWithDispatcher(testDispatchers)

        // Act
        authCommand.execute()
        // Fast-forward time to ensure the coroutine completes
        advanceUntilIdle()


        // Assert
        assertTrue(authCommand.isComplete)
        println("Test for AuthCommandWithDispatcher completed")
    }
}