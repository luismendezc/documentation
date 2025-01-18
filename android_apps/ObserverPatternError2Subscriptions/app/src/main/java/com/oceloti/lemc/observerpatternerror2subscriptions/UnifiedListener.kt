package com.oceloti.lemc.observerpatternerror2subscriptions

import android.util.Log

// Implementación del suscriptor
class UnifiedListener : EventListener {
  override fun update(result: OperationResult) {
    when (result.operation) {
      OperationType.TAK_ID -> Log.d("TESTING","Recibido TAK ID: ${result.data}")
      OperationType.SEAL_ONE_ID -> Log.d("TESTING","Recibido SEAL ONE ID: ${result.data}")
    }
  }
}