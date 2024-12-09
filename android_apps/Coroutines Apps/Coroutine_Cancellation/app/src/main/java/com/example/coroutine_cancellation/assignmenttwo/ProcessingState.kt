package com.example.coroutine_cancellation.assignmenttwo

sealed interface ProcessingState {
    data object CheckingFunds : ProcessingState
    data object DebitingAccount : ProcessingState
    data object CreditingAccount : ProcessingState
    data object CleanupResources : ProcessingState
}