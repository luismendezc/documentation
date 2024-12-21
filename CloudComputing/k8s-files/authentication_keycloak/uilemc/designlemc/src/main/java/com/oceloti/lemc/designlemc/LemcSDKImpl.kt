package com.oceloti.lemc.designlemc

import android.app.Application
import android.content.Intent
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

internal class LemcSDKImpl private constructor(
  private val application: Application
) : LemcSDK {

  private var config: LemcSDK.Config? = null
  private val messagesImpl by lazy { LemcMessagesImpl(this) }

  // A thread-safe list of subscriber records
  private val subscribers = CopyOnWriteArrayList<SubscriberRecord>()

  data class SubscriberRecord(
    val subscriber: LemcMessageSubscriber,
    val filter: (String) -> Boolean
  )

  override fun initialize(config: LemcSDK.Config) {
    this.config = config
    Log.d("TESTING", "initialized")
  }

  override fun authenticate(params: LemcSDK.AuthParams) {
    if (!isInitialized()) {
      emitMessage("ERROR: SDK not initialized!")
      return
    }
    val intent = Intent(application, LemcAuthActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
      putExtra("EXTRA_EMAIL", params.userEmail)
      putExtra("EXTRA_TOKEN", params.token)
    }
    application.startActivity(intent)
  }

  override fun isInitialized(): Boolean = config != null

  override fun getMessages(): LemcMessages = messagesImpl

  fun emitMessage(message: String) {
    Log.d("TESTING", "Emitting message $message")
    // For each subscriber whose filter passes, call handleMessage
    subscribers.forEach { record ->
      if (record.filter(message)) {
        record.subscriber.handleMessage(message)
      }
    }
  }

  // Add a subscriber with filter
  fun addSubscriber(
    subscriber: LemcMessageSubscriber,
    filter: (String) -> Boolean
  ): LemcDisposable {
    Log.d("TESTING", "Adding subscriber")
    val record = SubscriberRecord(subscriber, filter)
    subscribers.add(record)
    // Return a Disposable that removes them
    return object : LemcDisposable {
      override fun dispose() {
        subscribers.remove(record)
      }
    }
  }

  companion object {
    @Volatile
    private var instance: LemcSDKImpl? = null

    fun getInstance(application: Application): LemcSDKImpl {
      return instance ?: synchronized(this) {
        instance ?: LemcSDKImpl(application).also { instance = it }
      }
    }
  }
}
