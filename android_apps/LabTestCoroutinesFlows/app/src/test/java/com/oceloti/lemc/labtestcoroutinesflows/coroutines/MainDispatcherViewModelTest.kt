@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.oceloti.lemc.labtestcoroutinesflows.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainDispatcherViewModelTest {
    private lateinit var viewModel: MainDispatcherViewModel

    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = MainDispatcherViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     *   Level: Medium
     *
     *   Case: coroutine scopes launch coroutines (viewModelScope, lifecycleScope, GlobalScope,
     *   CoroutineScope)
     *   Use of main or main.immediate Dispatcher.
     *
     *   Solution: use StandardTestDispatchers to override the main or main.immediate dispatchers,
     *   use advanceUntilIdle() to ensure the coroutine completes.
     */
    @Test
    fun `test executeOnMainThread`() = runTest {
        // Arrange

        // Act
        viewModel.executeOnMainThread()
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.boolFlag)

    }
}