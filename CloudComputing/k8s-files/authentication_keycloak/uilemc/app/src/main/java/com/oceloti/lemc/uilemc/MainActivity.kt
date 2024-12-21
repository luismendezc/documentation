package com.oceloti.lemc.uilemc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.designlemc.LemcSDK
import com.oceloti.lemc.designlemc.LemcDisposable
import com.oceloti.lemc.designlemc.LemcMessageSubscriber
import com.oceloti.lemc.uilemc.ui.theme.UilemcTheme
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

  val lemcService: LemcServiceInterface by inject()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      var textResult by remember { mutableStateOf("Welcome youn must click the button to start the flow") }
      UilemcTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Button(onClick = {
            lemcService.startFlow(email = "lemc@oceloti.com", token = "123456", callback = { callback ->
              when( callback) {
                is LemcOperationCallback.Error -> {
                  Log.d(TAG, "Fue un error cambiando el state...")
                  textResult = "Success: ${callback.throwable.toString()}"
                }
                is LemcOperationCallback.Success -> {
                  Log.d(TAG, "Fue un exito cambiando el state...")
                  textResult = "Success: ${callback.data.someData}"
                }
              }
            })
          }, modifier = Modifier.padding(innerPadding)) {
            Text(text = "HOLA")
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(text = textResult)
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume MainActivity")
  }

  override fun onPause() {
    super.onPause()
    Log.d(TAG, "onPause MainActivity")
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "onDestroy MainActivity")
  }
  companion object {
    private const val TAG = "MainActivity"
  }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  UilemcTheme {
    Greeting("Android")
  }
}