@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.test_coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnconfinedExampleTest {
    private lateinit var dispatcher: TestDispatcher

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        //dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test execute with Unconfined Dispatcher`() = runTest(dispatcher) {
        // Arrange
        val sut = UnconfinedExample(scope = this)
        // Act
        sut.execute()
        // Assert
        assertFalse(sut.status)
        assertEquals(2,sut.num)
    }

    /*
        If something is ready now — like a coroutine with no delay or with a delay that's
        already expired — it will run immediately.
     */

    @Test
    fun `test execute with Standard Dispatcher`() = runTest(dispatcher) {
        // Arrange
        val sut = UnconfinedExample(scope = this)

        // Act
        sut.execute()

        // Assert
        assertTrue(sut.status)
        assertEquals(1, sut.num)

        runCurrent()
        assertFalse(sut.status)
        assertEquals(1, sut.num)

        advanceTimeBy(500L)
        runCurrent()
        assertEquals(2, sut.num)
    }


}