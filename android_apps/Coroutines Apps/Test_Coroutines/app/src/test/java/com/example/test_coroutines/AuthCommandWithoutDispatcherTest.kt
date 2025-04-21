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
        val sut = AuthCommandWithoutDispatcher()
        val latch = CountDownLatch(1)

        // Act
        sut.execute()
        latch.await(1500, TimeUnit.MILLISECONDS)

        // Assert
        assertTrue(sut.isComplete)
        println("Test for AuthCommandWithoutDispatcher completed")
    }
}