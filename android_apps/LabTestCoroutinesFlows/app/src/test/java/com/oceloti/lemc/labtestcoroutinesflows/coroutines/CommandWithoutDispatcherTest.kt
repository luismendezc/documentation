@file:OptIn(ExperimentalCoroutinesApi::class)

package com.oceloti.lemc.labtestcoroutinesflows.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class CommandWithoutDispatcherTest {
    private lateinit var command: CommandInterface

    @Before
    fun setUp() {
        command = CommandWithoutDispatcher()
    }

    /*
        Hardcoded time.
        No real control.
        Slow test.
    */
    @Test
    fun `test execute with CountDownLatch`() {
        // Arrange
        val latch = CountDownLatch(1)

        // Act
        command.execute()
        latch.await(1500, TimeUnit.MILLISECONDS)

        // Assert
        assertTrue (command.isComplete)
        println("Test for AuthCommandWithoutDispatcher completed")
    }
}