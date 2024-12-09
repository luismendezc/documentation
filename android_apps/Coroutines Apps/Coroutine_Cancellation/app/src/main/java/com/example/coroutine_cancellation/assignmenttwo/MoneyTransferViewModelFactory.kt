package com.example.coroutine_cancellation.assignmenttwo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope

class MoneyTransferViewModelFactory(
    private val scope: CoroutineScope
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoneyTransferViewModel::class.java)){
            return MoneyTransferViewModel(scope) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }
}