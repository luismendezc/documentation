package com.example.lab_testing_coroutines.logic.auth

import com.example.lab_testing_coroutines.KikeError

interface ResultOperationCallback<T> {
    class Success<T>(val data: T): ResultOperationCallback<T>
    class Error<T>(val throwable: KikeError): ResultOperationCallback<T>
}