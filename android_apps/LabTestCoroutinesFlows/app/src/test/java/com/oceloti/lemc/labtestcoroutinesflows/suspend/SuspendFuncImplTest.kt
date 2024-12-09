package com.oceloti.lemc.labtestcoroutinesflows.suspend

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SuspendFuncImplTest {
    private lateinit var suspendFunc: SuspendFunc

    @Before
    fun setUp() {
        suspendFunc = SuspendFuncImpl()
    }

    /**
     *   Level: Easy
     *
     *   Case: suspend functions
     *
     *   Solution: runBlocking / runTest makes them run synchronously
     *   - runTest allows us to provide DispatcherProvider
     */
    @Test
    fun `test getData runBlocking`() = runBlocking {
        val result = suspendFunc.getData()
        assertEquals("testing", result)
    }

    @Test
    fun `test getData`() = runTest(StandardTestDispatcher()) {
        val result = suspendFunc.getData()
        assertEquals("testing", result)
    }

}