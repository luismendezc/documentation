package com.oceloti.lemc.observerpatternerror2subscriptions

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.oceloti.lemc.observerpatternerror2subscriptions.ui.theme.ObserverPatternError2SubscriptionsTheme
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.yield

class MainActivity : ComponentActivity() {

  private val sdkHandler = SDKHandler()

  // 👉 Listener compartido solo para monitorear eventos, sin afectar las operaciones
  private val sharedListener = object : EventListener {
    override fun update(result: OperationResult) {
      when (result.operation) {
        OperationType.TAK_ID -> {
          Log.d("SHARED_LISTENER", "Received TAK_ID update: ${result.data}")
          // No se debe reanudar ninguna continuación aquí
        }
        OperationType.SEAL_ONE_ID -> {
          Log.d("SHARED_LISTENER", "Received SEAL_ONE_ID update: ${result.data}")
          // No se debe reanudar ninguna continuación aquí
        }
      }
    }
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ObserverPatternError2SubscriptionsTheme {
        MainScreen()
      }
    }
  }

  // 🔹 1. Suspend Function for TAK ID
  @OptIn(ExperimentalCoroutinesApi::class)
  private suspend fun getTakId(): String {
    return suspendCancellableCoroutine { continuation ->
      val takIdListener = object : EventListener {
        override fun update(result: OperationResult) {
          if (result.operation == OperationType.TAK_ID||result.operation == OperationType.SEAL_ONE_ID) {
            if (continuation.isActive) {
              Log.d("TESTING", "TAK ID received and resuming: ${result.data}")
              continuation.resume(result.data, onCancellation = null)
            } else {
              Log.w("TESTING", "TAK ID listener received a value, but continuation was already resumed!")
            }
          } else {
            Log.w("TESTING", "TAK ID listener received unexpected operation: ${result.operation}")
          }
        }
      }

      sdkHandler.events.unsubscribe(sharedListener)  // ✅ Clean previous listeners
      sdkHandler.fetchTakId()
      sdkHandler.events.subscribe(takIdListener)
    }
  }



  // 🔹 2. Suspend Function for SEAL ONE ID
  @OptIn(ExperimentalCoroutinesApi::class)
  private suspend fun getSealOneId(): String {
    return suspendCancellableCoroutine { continuation ->
      val sealOneIdListener = object : EventListener {
        override fun update(result: OperationResult) {
          if (result.operation == OperationType.TAK_ID||result.operation == OperationType.SEAL_ONE_ID) {
            if (continuation.isActive) {
              Log.d("TESTING", "SEAL ONE ID received and resuming: ${result.data}")
              continuation.resume(result.data, onCancellation = null)
            } else {
              Log.w("TESTING", "SEAL ONE ID listener received a value, but continuation was already resumed!")
            }
          } else {
            Log.w("TESTING", "SEAL ONE ID listener received unexpected operation: ${result.operation}")
          }
        }
      }

      sdkHandler.events.unsubscribe(sharedListener)  // ✅ Clean previous listeners
      sdkHandler.fetchSealOneId()
      sdkHandler.events.subscribe(sealOneIdListener)
    }
  }



  @OptIn(ExperimentalCoroutinesApi::class)
  @Composable
  fun MainScreen() {
    var takId by remember { mutableStateOf("Esperando TAK ID...") }
    var sealOneId by remember { mutableStateOf("Esperando SEAL ONE ID...") }

    val coroutineScope = rememberCoroutineScope()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = takId, style = MaterialTheme.typography.bodyLarge)
      Spacer(modifier = Modifier.height(16.dp))
      Text(text = sealOneId, style = MaterialTheme.typography.bodyLarge)
      Spacer(modifier = Modifier.height(32.dp))

      Button(onClick = {
        coroutineScope.launch {
          takId = "Obteniendo TAK ID..."
          sealOneId = "Esperando SEAL ONE ID..."

          // 🔄 Ejecuta primero TAK ID, luego SEAL ONE ID
          takId = "TAK ID: ${getTakId()}"
          yield()
          sealOneId = "SEAL ONE ID: ${getSealOneId()}"
        }
      }) {
        Text(text = "Iniciar Operaciones")
      }
    }
  }
}