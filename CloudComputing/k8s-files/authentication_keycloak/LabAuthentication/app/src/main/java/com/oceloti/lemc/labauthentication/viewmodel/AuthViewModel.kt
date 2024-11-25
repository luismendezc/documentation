package com.oceloti.lemc.labauthentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.oceloti.lemc.labauthentication.network.TokenResponse
import com.oceloti.lemc.labauthentication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

  private val _tokens = MutableStateFlow<TokenResponse?>(null)
  val tokens = _tokens.asStateFlow()

  private val _navigateToTokenScreen = MutableStateFlow(false)
  val navigateToTokenScreen = _navigateToTokenScreen.asStateFlow()

  private var codeVerifier: String? = null

  fun setCodeVerifier(verifier: String) {
    Log.d("AuthViewModel", "Code verifier set: $verifier")
    codeVerifier = verifier
  }

  suspend fun exchangeToken(code: String): TokenResponse? {
    val verifier = codeVerifier
    if (verifier == null) {
      Log.e("AuthViewModel", "Code verifier is missing")
      return null
    }

    return try {
      Log.d("AuthViewModel", "Exchanging token for code: $code")
      val response = repository.exchangeToken(
        code = code,
        redirectUri = "https://10.151.130.198/oauth2redirect",
        codeVerifier = verifier
      )
      _tokens.value = response
      _navigateToTokenScreen.value = true // Set navigation flag
      response
    } catch (e: Exception) {
      Log.e("AuthViewModel", "Error during token exchange: ${e.message}")
      null
    }
  }

  fun resetNavigationState() {
    _navigateToTokenScreen.value = false
  }
}



