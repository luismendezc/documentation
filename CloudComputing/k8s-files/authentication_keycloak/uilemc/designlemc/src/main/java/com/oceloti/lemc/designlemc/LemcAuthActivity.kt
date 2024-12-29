package com.oceloti.lemc.designlemc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.oceloti.lemc.designlemc.presentation.navigation.NavigationRoot
import com.oceloti.lemc.designlemc.ui.theme.UilemcTheme

internal class LemcAuthActivity : ComponentActivity() {

  private val sdkImpl: LemcSDKImpl by lazy {
    // Grab the SDKImpl singleton. This is guaranteed to exist if user called LemcSDK.instance()
    LemcSDKImpl.getInstance(application)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Extract parameters from the intent if needed
    val email = intent.getStringExtra("EXTRA_EMAIL")
    val token = intent.getStringExtra("EXTRA_TOKEN")

    setContent {
      UilemcTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val navController = rememberNavController()
          NavigationRoot(
            navController = navController,
            email = email!!,
            token = token!!,
            lemcSdk = sdkImpl,
            onFinish = {this.finish()}
          )
        }
      }
    }
  }
}

