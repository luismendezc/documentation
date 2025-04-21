package com.example.test_coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit Test Rule that sets a given [TestDispatcher] as the main dispatcher for unit tests
 * and resets it after the test finishes.
 *
 * This rule is useful for testing coroutines that run on the main thread, such as those using
 * `Dispatchers.Main`, allowing tests to control and advance the coroutine execution.
 *
 *
 * @param dispatcher The [TestDispatcher] to set as the main dispatcher during the test.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher) : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher)
        // Set the Main dispatcher before the test
        super.starting(description)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        // Reset the Main dispatcher after the test
        super.finished(description)
    }
}
