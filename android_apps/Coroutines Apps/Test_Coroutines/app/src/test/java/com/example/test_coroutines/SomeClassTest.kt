@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.test_coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse


import org.junit.Test

class SomeClassTest {
    private val testDispatchers = TestDispatchers()

    @Test
    fun `test launchSomething`() = runTest(testDispatchers.testDispatcher){
        // Arrange
        val sut = SomeClass(scope = this, dispatchers = testDispatchers)
        assertFalse(sut.status)
        // Act
        sut.launchSomething()
        runCurrent()
        // Assert
        assertTrue(sut.status)

    }
}