package com.oceloti.lemc.observerpatternerror2subscriptions

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

// 📦 EventManager (add delay in unsubscribe)
class EventManager {
  private val listeners = mutableListOf<EventListener>()

  fun subscribe(listener: EventListener) {
    Log.d("TESTING", "Subscribing listener")
    listeners.add(listener)
  }

  @OptIn(DelicateCoroutinesApi::class)
  fun unsubscribe(listener: EventListener) {
    Log.d("TESTING", "Forcefully unsubscribing listener")
    listeners.remove(listener)
  }

  fun notify(result: OperationResult) {
    Log.d("TESTING", "im notifying values per listener")
    // Deliver notifications immediately
    listeners.forEach { listener ->
      listener.update(result)
    }
  }
}