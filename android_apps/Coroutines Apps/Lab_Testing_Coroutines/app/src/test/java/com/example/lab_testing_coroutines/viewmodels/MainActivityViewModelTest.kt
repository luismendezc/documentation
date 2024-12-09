package com.example.lab_testing_coroutines.viewmodels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.lab_testing_coroutines.KikeError
import com.example.lab_testing_coroutines.RxImmediateSchedulerRule
import com.example.lab_testing_coroutines.TestDispatchers
import com.example.lab_testing_coroutines.logic.auth.AuthState
import com.example.lab_testing_coroutines.logic.auth.AuthenticationServiceInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

class MainActivityViewModelTest : KoinTest {

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var authenticationService: AuthenticationServiceInterface

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        authenticationService = mockk()
        testDispatchers = TestDispatchers()
        mainActivityViewModel = spyk(
            MainActivityViewModel(
                authService = authenticationService,
                disptachers = testDispatchers
            )
        )

        MockKAnnotations.init(this, relaxUnitFun = true)
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onError DisplayError`() = runTest(testDispatchers.testDispatcher) {
        assert(mainActivityViewModel.actionState.value is AuthState.Unauthenticated)

        val kikeError = KikeError(
            headline = "Sorry something went wrong",
            description = "We are sorry for the inconvenience",
            true,
            true
        )

        coEvery { authenticationService.enrichError(any()) } just runs

        mainActivityViewModel.onError(kikeError)

        advanceUntilIdle()


        assert(mainActivityViewModel.actionState.value is AuthState.DisplayError)

    }

    @Test
    fun `authorize should call getSingleResult and capture emitted value`() {
        // Define a slot to capture the emitted value
        val resultSlot = slot<String>()

        // Mock getSingleResult to return a Single with the expected result
        every { mainActivityViewModel.getSingleResult() } answers {
            Single.just("test_result")
        }

        // Call the authorize method
        mainActivityViewModel.authorize("test_url")

        // Verify that getSingleResult was called
        verify { mainActivityViewModel.getSingleResult() }

        // Assert that getSingleResult returned the expected result
        val emittedResult = mainActivityViewModel.getSingleResult()
            .blockingGet() // blockingGet is used to fetch the result synchronously
        assert(emittedResult == "test_result")
    }

}

