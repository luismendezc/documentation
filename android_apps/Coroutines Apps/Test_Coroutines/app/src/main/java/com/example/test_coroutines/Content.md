# 1 - AuthCommandWithoutDispatcher, AuthCommandWithoutDispatcherTest
Old fashion wait synchronously

# 2 - DispatcherProvider, TestDispatchers
Helper classes for Dispatcher providers

# 3 - AuthCommandWithDispatcher, AuthCommandWithDispatcherTest
Usage kotlinx-coroutines-test, control of virtual time

# 4 - MainViewModel, MainViewModelTest, MainDispatcherRule
viewModelScope, how to control main dispatcher?

# 5 - DispatcherChecker, DispatcherCheckerTest
Flaky test, same dispatcher in test

# 6 - UnconfinedExample, UnconfinedExampleTest
UnconfinedTestDispatcher and StandardTestDispatcher

# 7 - SomeClass, SomeClassTest
scope and dispatcher provider injection