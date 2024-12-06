package com.oceloti.lemc.labauthentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.oceloti.lemc.labauthentication.ui.NavigationRoot
import com.oceloti.lemc.labauthentication.ui.theme.LabAuthenticationTheme
import com.oceloti.lemc.labauthentication.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

  private val mainViewModel by viewModel<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    installSplashScreen().apply {
        setKeepOnScreenCondition {
          mainViewModel.state.isCheckingAuth
        }
    }

    setContent {
      LabAuthenticationTheme(){
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val state = mainViewModel.state
           if(!state.isCheckingAuth){
             val navController = rememberNavController()
             NavigationRoot(
               navController = navController,
               isLoggedIn = state.isLoggedIn,
             )
           }
        }
      }
    }

    // Handle redirect URI if present
    intent?.data?.let {
      handleRedirect(it)
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    val uri = intent.data
    if (uri != null) {
      handleRedirect(uri)
    }
  }

  /**
   * Handles the redirect URI after the user completes the login in the browser.
   *
   * @param uri The redirect URI containing the authorization code.
   */
  private fun handleRedirect(uri: Uri?) {
    if (uri != null && uri.host == "10.151.130.198" && uri.path == "/oauth2redirect") {
      val code = uri.getQueryParameter("code")
      if (code != null) {
        Log.d(TAG, "Authorization code received: $code")
        lifecycleScope.launch {
          mainViewModel.exchangeToken(code)
        }
      } else {
        Log.e(TAG, "Authorization code is missing")
        mainViewModel.updateIsCheckingAuth(false)
      }
    }
  }

  companion object {
    private val TAG = "MainActivity"
  }

}


