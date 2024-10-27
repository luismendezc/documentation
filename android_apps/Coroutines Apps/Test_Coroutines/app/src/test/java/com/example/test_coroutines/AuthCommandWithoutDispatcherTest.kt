package com.example.test_coroutines

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class AuthCommandWithoutDispatcherTest {
    /*
        Hardcoded time.
        No real control.
        Slow test.
    */
    @Test
    fun `test execute with CountDownLatch`() {
        // Arrange
        val authCommand = AuthCommandWithoutDispatcher()
        val latch = CountDownLatch(1)

        // Act
        authCommand.execute()
        latch.await(1500, TimeUnit.MILLISECONDS)

        // Assert
        assertTrue(authCommand.isComplete)
        println("Test for AuthCommandWithoutDispatcher completed")
    }
}