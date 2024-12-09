package com.example.lab_testing_coroutines.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lab_testing_coroutines.KikeError
import com.example.lab_testing_coroutines.logic.auth.AuthState
import com.example.lab_testing_coroutines.logic.auth.AuthenticationServiceInterface
import com.example.lab_testing_coroutines.util.DispatcherProvider
import com.example.lab_testing_coroutines.util.StandardDispatchers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    private val authService:AuthenticationServiceInterface,
    private val disptachers: DispatcherProvider = StandardDispatchers
) : MainActivityViewModelAbstract() {

    private val compositeDisposable = CompositeDisposable()

    private val _actionState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    override val actionState: LiveData<AuthState>
        get() = _actionState



    override fun authorize(url: String) {
        Log.d(TAG, "authorize: $url")
        val disposable = getSingleResult()
            .subscribeOn(Schedulers.io()) // Perform on IO thread
            .observeOn(AndroidSchedulers.mainThread()) // Observe on main thread for UI operations
            .subscribe({ result ->
                // On success, log the result on main thread
                Log.d(TAG, "Success: $result")
            }, { kikeError ->
                // On error, start another activity on main thread
                Log.e(TAG, "Got error in authorize", kikeError)
                onError(kikeError)
            })

        compositeDisposable.add(disposable)
    }

    override fun <T : Throwable> onError(throwable: T) {
        if(throwable is KikeError) {
            Log.d(TAG, "throwable is KikeError")
            viewModelScope.launch(disptachers.io) {
                Log.d(TAG, "Inside customScope coroutine launch")
                authService.enrichError(throwable)
                Log.d(TAG, "Before withContext(Dispatchers.Main)")
                withContext(disptachers.main) {
                    Log.d(TAG, "Inside withContext(Dispatchers.Main)")
                    if(throwable.displayError) {
                        Log.d(TAG, "Displaying error")
                        _actionState.postValue(AuthState.DisplayError(throwable.headline, throwable.description))
                    }
                }
            }
        }
    }

    override fun getSingleResult(): Single<String> {
        // This function returns a Single which can either emit a String or an error
        return Single.create{ emitter ->
            try {
                // Simulate some work, e.g., fetching data or some computation
                val result = "Hello from Single"
                throw KikeError("TESTING", "HACIENDO TESTING", true, true)
                emitter.onSuccess(result)
            } catch (e: Exception) {
                // If an error occurs, emit an error
                emitter.onError(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //customScope.cancel()
        compositeDisposable.dispose()
        //Log.d(TAG, "customScope cancel and compositeDisposable dispose")
    }

    companion object {
        private const val TAG = "MainActivityViewModel"
    }
}