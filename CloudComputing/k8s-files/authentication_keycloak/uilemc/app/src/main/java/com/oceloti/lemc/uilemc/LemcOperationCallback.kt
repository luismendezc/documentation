package com.oceloti.lemc.uilemc

sealed interface LemcOperationCallback<T> {
  data class Success<T>(val data: T) : LemcOperationCallback<T>
  data class Error<T>(val throwable: Throwable) : LemcOperationCallback<T>
}