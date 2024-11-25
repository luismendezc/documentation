package com.oceloti.lemc.labauthentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState) // Corrected parentheses
    Log.d("MainActivity", "onCreate called")

    // Check if the activity was launched with a redirect URI
    val launchUri = intent?.data
    Log.d("MainActivity", "Intent URI: $launchUri")
    handleRedirect(launchUri)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    val data = intent?.data
    if (data != null && data.host == "mywebsite.com") {
      // Handle redirect logic
      val code = data.getQueryParameter("code")
      if (code != null) {
        Log.d("MainActivity", "Authorization code received: $code")
        // Exchange code for tokens
      } else {
        Log.e("MainActivity", "Authorization code missing")
      }
    }
  }


  private fun handleRedirect(uri: Uri?) {
    if (uri != null && uri.scheme == "https" && uri.host == "10.151.130.198") {
      Log.d("MainActivity", "Redirect URI detected: $uri")

      // Extract the authorization code
      val code = uri.getQueryParameter("code")
      val state = uri.getQueryParameter("state")

      if (code != null) {
        Log.d("MainActivity", "Authorization code: $code")
        // Exchange the code for tokens
      } else {
        Log.e("MainActivity", "Authorization code is missing in redirect URI")
      }
    } else {
      Log.d("MainActivity", "No valid redirect URI detected")
    }
  }

}
