package com.oceloti.lemc.labauthentication.data.security

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SessionManager(
  private val timeoutMillis: Long,
  private val scope: CoroutineScope
) {
  private var onTimeout: (() -> Unit)? = null
  private var job: Job? = null

  fun start(onTimeout: () -> Unit) {
    this.onTimeout = onTimeout
    reset()
  }

  fun reset() {
    job?.cancel()
    job = scope.launch {
      delay(timeoutMillis)
      onTimeout?.invoke()
    }
  }

  fun stop() {
    job?.cancel()
    job = null
    onTimeout = null
  }
}
