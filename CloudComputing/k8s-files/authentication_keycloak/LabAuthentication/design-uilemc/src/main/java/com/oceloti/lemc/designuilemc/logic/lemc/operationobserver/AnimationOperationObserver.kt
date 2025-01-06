package com.oceloti.lemc.designuilemc.logic.lemc.operationobserver

import android.util.Log
import com.oceloti.lemc.designlemc.LemcDisposable
import com.oceloti.lemc.designlemc.LemcMessageSubscriber

class AnimationOperationObserver : LemcOperationObserver<AnimationOperationResult> {
  override var hostingMode: Boolean? = null
  override var successMessagesDisposable: LemcDisposable? = null
  override var errorMessagesDisposable: LemcDisposable? = null
  override var updateMessagesDisposable: LemcDisposable? = null

  override fun handleError(callback: (LemcOperationCallback<AnimationOperationResult>) -> Unit): LemcMessageSubscriber {
    return object : LemcMessageSubscriber {
      override fun handleMessage(message: String) {
        // Check if it’s an error. Then call callback.
        Log.d("TESTING", "Error message")
        Log.d("TESTING", "handle message: $message")
        callback(LemcOperationCallback.Error(Exception(message)))
      }
    }
  }

  override fun handleSuccess(callback: (LemcOperationCallback<AnimationOperationResult>) -> Unit): LemcMessageSubscriber {
    return object : LemcMessageSubscriber {
      override fun handleMessage(message: String) {
        // Parse the message for success. Then do something:
        Log.d("TESTING", "Success message")
        Log.d("TESTING", "handle message: $message")
        val result = AnimationOperationResult(message)
        callback(LemcOperationCallback.Success(result))
      }
    }
  }

  override fun handleUpdate(): LemcMessageSubscriber {
    return object : LemcMessageSubscriber {
      override fun handleMessage(message: String) {
        Log.d("TESTING", "Update message")
        Log.d("TESTING", "handle message: $message")
        // Maybe handle progress updates or logs.
      }
    }
  }
}

data class AnimationOperationResult(val someData: String)
