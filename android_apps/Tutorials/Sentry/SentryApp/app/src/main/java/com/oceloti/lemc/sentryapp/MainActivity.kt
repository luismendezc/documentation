package com.oceloti.lemc.sentryapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.oceloti.lemc.sentryapp.ui.theme.SentryAppTheme
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import java.time.LocalDate
import java.util.Date

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      SentryAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Column(modifier = modifier){
    Text(
      text = "Hello $name!",
      modifier = modifier
    )
    HorizontalDivider()
    Button(onClick = {
      val breadcrumb = Breadcrumb().apply {
        category = "ui.click"
        message = "User clicked the login button"
        level = SentryLevel.INFO
      }
      Sentry.addBreadcrumb(breadcrumb)

      startLogin()
    }) {
      Text("Here a button that might be a breadcrumb")
    }

    Button(onClick = {
      val breadcrumb = Breadcrumb().apply {
        category = "ui.click"
        message = "User clicked the ID setup button"
        level = SentryLevel.INFO
      }
      Sentry.addBreadcrumb(breadcrumb)

      setId()
    }) {
      Text("Here a button that might be another breadcrumb")
    }


    Button(onClick = {
      Log.d("MainActivity", "Trigger Sentry exception")
      Sentry.captureException(RuntimeException("This app uses Sentry! :) ${LocalDate.now()}"))
    }) {
      Text("Here the button that triggers the sentry exception")
    }
  }
}

private fun startLogin(){

  var perro = 1
  val perroRandom = Math.random()
  if(perroRandom > 0.5){
    perro = 2
  } else {
    perro = 3
  }

}

private fun setId(){
  val id = 2
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  SentryAppTheme {
    Greeting("Android")
  }
}