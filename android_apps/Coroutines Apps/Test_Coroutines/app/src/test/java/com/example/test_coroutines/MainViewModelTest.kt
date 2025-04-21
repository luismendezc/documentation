@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.test_coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private val testDispatchers = TestDispatchers()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatchers.testDispatcher)

    @Test
    fun `test status change with main dispatcher rule`() = runTest(mainDispatcherRule.dispatcher) {
        // Arrange
        val sut = MainViewModel()
        assertFalse(sut.status)

        // Act
        sut.execute()
        runCurrent()

        // Assert
        assertTrue(sut.status)
        advanceUntilIdle()
        assertFalse(sut.status)
    }

    /*
    @Test
    fun `test status change with main dispatcher rule - FAIL`() = runTest {
        // Arrange
        val sut
         = MainViewModel()

        // Acts
        sut.executeFail()
        advanceUntilIdle()

        // Assert
        assertTrue(sut.status)
    }
     */

    @Test
    fun `test num change with nested coroutines`()= runTest(mainDispatcherRule.dispatcher){
        // Arrange
        val sut = MainViewModel()

        // Act
        sut.executeMultipleCorotuinesNested()
        runCurrent() // runs everything ready now — outer coroutine runs

        // Assert
        assertEquals(3, sut.num) // num is set to 3 after outer coroutine continues
        // Inner coroutine still delayed
        advanceTimeBy(1005L) // now inner coroutine will run
        assertEquals(2, sut.num)
    }
}