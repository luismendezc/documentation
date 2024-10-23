package com.example.labimagecompressor.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppState (
    val compressionState: CompressState
    )

enum class CompressState {
    IDLE,
    SELECT_IMAGE,
    COMPRESSING,
    FAILED
}

class MainViewModel() : AbstractMainViewModel() {
    private val _appState = MutableStateFlow(AppState(CompressState.IDLE))
    override val appState = _appState.asStateFlow()

    override fun selectImage() {
        Log.d(TAG, "selectImage()")
        Log.d(TAG, "State: SELECT_IMAGE")
        _appState.value = AppState(CompressState.SELECT_IMAGE)
    }

    override fun idle(){
        _appState.value = AppState(CompressState.IDLE)
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}