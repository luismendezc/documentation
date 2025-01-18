package com.oceloti.lemc.observerpatternerror2subscriptions

// Interfaz del suscriptor
interface EventListener {
  fun update(result: OperationResult)
}