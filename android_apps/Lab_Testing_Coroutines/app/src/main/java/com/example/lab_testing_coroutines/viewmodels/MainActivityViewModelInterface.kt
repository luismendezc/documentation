package com.example.lab_testing_coroutines.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lab_testing_coroutines.logic.auth.AuthState
import io.reactivex.rxjava3.core.Single

abstract class MainActivityViewModelAbstract: ViewModel(), MainActivityViewModelInterface

interface MainActivityViewModelInterface{

    val actionState: LiveData<AuthState>

    fun authorize(url:String)
    fun <T : Throwable> onError(throwable: T)

    fun getSingleResult(): Single<String>
}