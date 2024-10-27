package com.example.labdaggerhiltcompose

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.labdaggerhiltcompose.domain.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {

    init {
        Log.d(TAG, "MyViewModel: init")
    }

    companion object {
        private const val TAG = "MyViewModel"
    }
}