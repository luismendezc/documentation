package com.oceloti.lemc.labauthentication.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.labauthentication.network.CodeVerifier
import com.oceloti.lemc.labauthentication.ui.auth.AuthAction
import com.oceloti.lemc.labauthentication.util.PKCEUtil
import com.oceloti.lemc.labauthentication.viewmodel.events.AuthEvent
import com.oceloti.lemc.labauthentication.viewmodel.states.AuthState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
  private val oidcCodeVerifier: CodeVerifier
): ViewModel() {

  var state by mutableStateOf(AuthState())
    private set

  private val eventChannel = Channel<AuthEvent>()
  val events = eventChannel.receiveAsFlow()

  fun onAction(action: AuthAction){
    when(action){
      AuthAction.OnSignInClick -> {
        state.copy(isLoginInProgress = true)
        startOAuthLoginFlow()
      }
      AuthAction.OnSignUpClick -> {
        state.copy(isRegisterInProgress = true)
        startOAuthRegisterFlow()
      }
    }
  }

  fun enableLogin(){
    state.copy(isLoginInProgress = false)
  }
  fun enableRegister(){
    state.copy(isRegisterInProgress = false)
  }

  /**
   * Starts the OAuth flow by opening the browser via Custom Tabs.
   */
  fun startOAuthLoginFlow() {
    Log.d(TAG, "startOAuthFlow() called")
    val codeVerifier = PKCEUtil.generateCodeVerifier()
    val codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier)

    // Store codeVerifier for later use
    oidcCodeVerifier.setCodeVerifier(codeVerifier)

    // TODO: Secure this links and make them reusable for different cases
    val authUrl = "https://10.151.130.198:32080/realms/oauthrealm/protocol/openid-connect/auth" +
        "?client_id=lab-authentication-client" +
        "&response_type=code" +
        "&redirect_uri=https://10.151.130.198/oauth2redirect" +
        "&scope=openid" +
        "&state=xyz123" +
        "&code_challenge=$codeChallenge" +
        "&code_challenge_method=S256"

    Log.d("HOLA", authUrl)
    // Send to chanel
    viewModelScope.launch {
      eventChannel.send(AuthEvent.OauthLoginUrlGenerated(
        url = authUrl
      ))
    }
  }

  private fun startOAuthRegisterFlow(){

  }

  companion object {
    private val TAG = "AuthViewModel"
  }

}