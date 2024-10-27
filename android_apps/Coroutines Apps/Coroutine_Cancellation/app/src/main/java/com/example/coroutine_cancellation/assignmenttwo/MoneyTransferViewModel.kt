package com.example.coroutine_cancellation.assignmenttwo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoneyTransferViewModel(private val applicationScope: CoroutineScope) : ViewModel() {

    var state by mutableStateOf(MoneyTransferState())
        private set

    private var job: Job? = null



    @OptIn(ExperimentalFoundationApi::class)
    fun onAction(action: MoneyTransferAction) {
        when (action) {
            MoneyTransferAction.TransferFunds -> transferFunds()
            MoneyTransferAction.CancelTransfer -> {
                job?.cancel()
                state = state.copy(
                    isTransferring = false,
                    processingState = null,
                    resultMessage = null,
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun transferFunds() {
        job = viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    state = state.copy(
                        isTransferring = true,
                        resultMessage = null,
                    )
                    val amountToTransfer = state.transferAmount.text.toString().toDoubleOrNull()

                    if (amountToTransfer == null) {
                        state = state.copy(
                            resultMessage = "Invalid Amount"
                        )
                        return@withContext
                    }

                    if (amountToTransfer == 0.0) {
                        state = state.copy(
                            resultMessage = "Enter amount greater than 0"
                        )
                        return@withContext
                    }

                    state = state.copy(
                        processingState = ProcessingState.CheckingFunds,
                    )
                    val hasEnoughFunds = checkHasEnoughFunds(state.savingsBalance, amountToTransfer)

                    if (!hasEnoughFunds) {
                        state = state.copy(
                            resultMessage = "Insufficient funds",
                        )
                        return@withContext
                    }

                    debitAccount(state.savingsBalance, amountToTransfer)
                    applicationScope.launch {
                        creditAccount(state.checkingBalance, amountToTransfer)
                    }.join()

                    state = state.copy(
                        resultMessage = "Transfer complete!",
                    )
                    println("--------------")
                    println(state.checkingBalance)
                    println(state.savingsBalance)
                    println("--------------")

                } catch (e: Exception) {
                    coroutineContext.ensureActive()
                    println("Error processing transfer: ${e.message}")
                } finally {
                    withContext(NonCancellable){
                        cleanupResources()
                    }
                    state = state.copy(
                        processingState = null,
                        isTransferring = false,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private suspend fun creditAccount(toAccountBalance: Double, amount: Double) {
        state = state.copy(
            processingState = ProcessingState.CreditingAccount,
        )
        println("Crediting account with balance $toAccountBalance and amount $amount")
        delay(3000)
        state = state.copy(
            checkingBalance = toAccountBalance + amount,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    private suspend fun debitAccount(fromAccountBalance: Double, amount: Double) {
        state = state.copy(processingState = ProcessingState.DebitingAccount)
        delay(3000)
        state = state.copy(
            savingsBalance = fromAccountBalance - amount,
        )
    }

    private suspend fun checkHasEnoughFunds(fromAccountBalance: Double, amount: Double): Boolean {
        println("Checking balance with balance $fromAccountBalance and amount $amount")
        delay(2000)
        return amount <= fromAccountBalance
    }

    @OptIn(ExperimentalFoundationApi::class)
    private suspend fun cleanupResources() {
        state = state.copy(
            processingState = ProcessingState.CleanupResources,
        )
        delay(2000)
    }
}