package com.oceloti.lemc.designuilemc.logic.lemc.operationobserver

sealed interface LemcOperationCallback<T> {
  data class Success<T>(val data: T) : LemcOperationCallback<T>
  data class Error<T>(val throwable: Throwable) : LemcOperationCallback<T>
}