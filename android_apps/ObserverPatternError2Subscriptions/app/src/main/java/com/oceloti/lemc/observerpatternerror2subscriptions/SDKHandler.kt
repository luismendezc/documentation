package com.oceloti.lemc.observerpatternerror2subscriptions

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SDKHandler {
  val events = EventManager()

  private val sdkScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)

  fun fetchTakId() {
    sdkScope.launch {
      kotlinx.coroutines.delay(500)  // TAK ID is slow
      withContext(kotlinx.coroutines.Dispatchers.Main) {
        Log.d("TESTING", "Sending TAK ID")
        events.notify(OperationResult(OperationType.TAK_ID, "TAK_ID_12345"))
      }
    }
  }

  fun fetchSealOneId() {
    sdkScope.launch {
      //kotlinx.coroutines.delay(500)  // SEAL ONE ID is very fast
      withContext(kotlinx.coroutines.Dispatchers.Main) {
        Log.d("TESTING", "Sending SEAL ONE ID")
        events.notify(OperationResult(OperationType.SEAL_ONE_ID, "SEAL_ONE_ID_67890"))
      }
    }
  }
}