package com.example.coroutine_cancellation.assignmenttwo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState

data class MoneyTransferState @OptIn(ExperimentalFoundationApi::class) constructor(
    val savingsBalance: Double = 1000.0,
    val checkingBalance: Double = 500.0,
    val transferAmount: TextFieldState = TextFieldState(),
    val isTransferring: Boolean = false,
    val resultMessage: String? = null,
    val processingState: ProcessingState? = null,
)