package com.example.labimagecompressor.viewmodels

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
}