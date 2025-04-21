@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.test_coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DispatcherCheckerTest {

    private val testDispatchers = TestDispatchers()

    /*
        When using StandardTestDispatcher, make sure both your test scope
        and class under test use the same dispatcher/scheduler, or else time manipulation won't sync.
     */
    @Test
    fun `test executeHeavyLogic should set isDone after delay`() =
        runTest(testDispatchers.testDispatcher) {
            // Arrange
            val sut = DispatcherChecker(testDispatchers)

            // Act
            sut.executeHeavyLogic()


            // Assert
            assertFalse(sut.isDone) // Not yet done
            advanceTimeBy(1005L)
            assertTrue(sut.isDone) // Should now be done
        }

    @Test
    fun `this test might be flaky if dispatcher is not passed`() = runTest {
        // Arrange
        val sut =
            DispatcherChecker(testDispatchers) // dispatcher inside logic is test-controlled

        //Act
        sut.executeHeavyLogic()
        advanceTimeBy(1000)
        runCurrent()

        // Assert
        // This might pass or fail depending on if internal dispatcher cooperates
        assertTrue(sut.isDone)
    }
}
