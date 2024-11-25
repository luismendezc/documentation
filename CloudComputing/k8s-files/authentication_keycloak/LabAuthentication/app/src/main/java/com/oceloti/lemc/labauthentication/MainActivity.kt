package com.oceloti.lemc.labauthentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.oceloti.lemc.labauthentication.ui.NavigationGraph
import com.oceloti.lemc.labauthentication.util.PKCEUtil
import com.oceloti.lemc.labauthentication.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

  private val authViewModel: AuthViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      NavigationGraph(
        navController = rememberNavController(),
        viewModel = authViewModel
      )
    }

    // Handle redirect URI if present
    handleRedirect(intent?.data)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleRedirect(intent.data)
  }

  /**
   * Starts the OAuth flow by opening the browser via Custom Tabs.
   */
  fun startOAuthFlow() {
    Log.d("MainActivity", "startOAuthFlow() called")
    val codeVerifier = PKCEUtil.generateCodeVerifier()
    val codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier)
    authViewModel.setCodeVerifier(codeVerifier) // Store code_verifier for later use

    val authUrl = "https://10.151.130.198:32080/realms/oauthrealm/protocol/openid-connect/auth" +
        "?client_id=lab-authentication-client" +
        "&response_type=code" +
        "&redirect_uri=https://10.151.130.198/oauth2redirect" +
        "&scope=openid" +
        "&state=xyz123" +
        "&code_challenge=$codeChallenge" +
        "&code_challenge_method=S256"

    openOAuthUrl(authUrl)
  }

  /**
   * Opens the provided URL in a Custom Tab.
   *
   * @param authUrl The URL to open.
   */
  private fun openOAuthUrl(authUrl: String) {
    val uri = Uri.parse(authUrl)
    val customTabsIntent = CustomTabsIntent.Builder()
      .setShowTitle(true)
      .build()
    customTabsIntent.launchUrl(this, uri)
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
        Log.d("MainActivity", "Authorization code received: $code")
        lifecycleScope.launch {
          authViewModel.exchangeToken(code)
        }
      } else {
        Log.e("MainActivity", "Authorization code is missing")
      }
    }
  }

}


