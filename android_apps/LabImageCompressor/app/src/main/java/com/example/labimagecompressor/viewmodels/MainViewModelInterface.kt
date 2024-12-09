package com.example.labimagecompressor.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class AbstractMainViewModel :
    ViewModel(),
    MainViewModelInterface

interface MainViewModelInterface {
    val appState: StateFlow<AppState>

    fun selectImage()
    fun idle()

    fun compressImageProcess(imageUri: Uri, context: Context)

    fun cancelCompression()

    fun restartToIdle()
}