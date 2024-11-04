@file:OptIn(ExperimentalCoroutinesApi::class)

package com.oceloti.lemc.labtestcoroutinesflows.flows

import app.cash.turbine.test
import com.oceloti.lemc.labtestcoroutinesflows.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FlowViewModelTest{
    private lateinit var viewModel: FlowViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        viewModel = FlowViewModel(
            dispatchers = testDispatchers
        )
    }

    // launching coroutine
    @Test
    fun testRegisterLoading_withFlow() = runTest(testDispatchers.testDispatcher) {
        val loadingState = mutableListOf<Boolean>()
        val job = launch {
            viewModel.isLoading.collect { loading ->
                loadingState.add(loading)
            }
        }
        viewModel.register()
        advanceUntilIdle()
        assertEquals(listOf(false, true, false), loadingState)
        job.cancel()
    }

    // turbine
    @Test
    fun testRegisterLoading_withFlow_turbine() = runTest(testDispatchers.testDispatcher) {
        viewModel.isLoading.test {
            val initialEmission = awaitItem()
            assertFalse(initialEmission)
            viewModel.register()
            val loadingEmission = awaitItem()
            assertTrue(loadingEmission)
            val finalEmission = awaitItem()
            assertFalse(finalEmission)
        }
    }
}