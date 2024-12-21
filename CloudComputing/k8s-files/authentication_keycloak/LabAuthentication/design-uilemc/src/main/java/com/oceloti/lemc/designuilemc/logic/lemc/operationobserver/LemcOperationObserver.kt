package com.oceloti.lemc.designuilemc.logic.lemc.operationobserver

import com.oceloti.lemc.designlemc.LemcDisposable
import com.oceloti.lemc.designlemc.LemcMessageSubscriber
import com.oceloti.lemc.designlemc.LemcMessages

interface LemcOperationObserver<T> {
  var hostingMode: Boolean?
  var successMessagesDisposable: LemcDisposable?
  var errorMessagesDisposable: LemcDisposable?
  var updateMessagesDisposable: LemcDisposable?

  fun handleSuccess(callback: (LemcOperationCallback<T>) -> Unit): LemcMessageSubscriber?
  fun handleError(callback: (LemcOperationCallback<T>) -> Unit): LemcMessageSubscriber?
  fun handleUpdate(): LemcMessageSubscriber?

  fun subscribe(
    hostingMode: Boolean,
    messages: LemcMessages,
    callback: (LemcOperationCallback<T>) -> Unit
  ) {
    this.hostingMode = hostingMode
    updateMessagesDisposable = handleUpdate()?.let { messages.onlyUpdates().subscribe(it) }
    errorMessagesDisposable = handleError(callback)?.let { messages.onlyErrors().subscribe(it) }
    successMessagesDisposable = handleSuccess(callback)?.let { messages.onlySuccess().subscribe(it) }
  }

  fun unsubscribe() {
    updateMessagesDisposable?.dispose()
    errorMessagesDisposable?.dispose()
    successMessagesDisposable?.dispose()
    updateMessagesDisposable = null
    errorMessagesDisposable = null
    successMessagesDisposable = null
  }
}
