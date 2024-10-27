package com.example.testingcoroutinesflows

import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FibonacciKtTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testFibonacci() = runTest {
        val dispatcher = coroutineContext[CoroutineDispatcher]
        val result = fib(30, dispatcher!!)
        assertk.assertThat(result).isEqualTo(832_040)
    }
}