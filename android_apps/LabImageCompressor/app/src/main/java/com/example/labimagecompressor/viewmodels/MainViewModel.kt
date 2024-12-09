package com.example.labimagecompressor.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.labimagecompressor.util.DispatcherProvider
import com.example.labimagecompressor.util.StandardDispatcher
import com.example.labimagecompressor.util.interfaces.FileManager
import com.example.labimagecompressor.util.interfaces.ImageCompressor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(
    private val imageCompressor: ImageCompressor,
    private val fileManager: FileManager,
    private val dispatcher: DispatcherProvider = StandardDispatcher(),
) : AbstractMainViewModel() {
    private val _appState = MutableStateFlow<AppState>(AppState.IDLE)
    override val appState = _appState.asStateFlow()

    override fun selectImage() {
        Log.d(TAG, "selectImage()")
        Log.d(TAG, "State: SELECT_IMAGE")
        _appState.value = AppState.SELECT_IMAGE
    }

    override fun idle() {
        Log.d(TAG, "idle()")
        Log.d(TAG, "State: IDLE")
        _appState.value = AppState.IDLE
    }

    override fun compressImageProcess(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            Log.d(TAG, "compressImageProcess()")
            Log.d(TAG, "State: COMPRESSING")
            _appState.value = AppState.COMPRESSING
            try {
                val imageCompressed =
                    imageCompressor.compressImage(imageUri, 200 * 1024L )
                val savedImageUri = imageCompressed?.let {
                    fileManager.saveImage(
                        it, UUID.randomUUID().toString() + "_compressed"
                    )
                }
                ensureActive()
                savedImageUri?.let { uri ->
                    //val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Log.d(TAG, "State: FINISHED")
                    _appState.value = AppState.FINISHED(uri)
                } ?: run {
                    _appState.value = AppState.FAILED
                }
            } catch (e: Exception) {
                Log.d(TAG, "compressImageProcess exception: " + e.message)
                if (e is CancellationException) throw e
                _appState.value = AppState.FAILED
            }

        }
    }

    override fun cancelCompression() {
        Log.d(TAG, "cancelCompression()")
        viewModelScope.coroutineContext.cancelChildren()
        _appState.value = AppState.CANCELLED
    }

    override fun restartToIdle() {
        _appState.value = AppState.IDLE
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

sealed class AppState {
    data object IDLE : AppState()
    data object SELECT_IMAGE : AppState()
    data object COMPRESSING : AppState()
    data object CANCELLED : AppState()
    class FINISHED(val imageUri: Uri? = null) : AppState()
    data object FAILED : AppState()

}