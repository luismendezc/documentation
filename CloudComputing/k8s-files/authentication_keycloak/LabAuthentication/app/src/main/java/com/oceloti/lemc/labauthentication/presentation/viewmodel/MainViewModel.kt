package com.oceloti.lemc.labauthentication.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.labauthentication.data.repository.AuthRepository
import com.oceloti.lemc.labauthentication.data.security.SessionStorage
import com.oceloti.lemc.labauthentication.network.CodeVerifier
import com.oceloti.lemc.labauthentication.network.responsemodels.TokenResponseModel
import com.oceloti.lemc.labauthentication.network.responsemodels.toLabToken
import com.oceloti.lemc.labauthentication.presentation.states.MainState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch


class MainViewModel(
  private val repository: AuthRepository,
  private val codeVerifier: CodeVerifier,
  private val sessionStorage: SessionStorage
) : ViewModel() {

  var state by mutableStateOf(MainState())
    private set

  init {
    viewModelScope.launch {
      val tokens = sessionStorage.get()
      if (!tokens?.refreshToken.isNullOrEmpty()) {
        updateIsLoggedIn(true)
      } else {
        updateIsLoggedIn(false)
      }
      updateIsCheckingAuth(false)
    }
  }

  suspend fun exchangeToken(code: String): TokenResponseModel? {
    updateIsCheckingAuth(true)
    val verifier = codeVerifier.code
    if (verifier == null) {
      Log.e(TAG, "Code verifier is missing")
      return null
    }
    return try {
      Log.d(TAG, "Exchanging token for code: $code")
      val response = repository.exchangeToken(
        code = code,
        redirectUri = "https://10.151.130.198/oauth2redirect",
        codeVerifier = verifier
      )
      if (response != null) {
        sessionStorage.set(response.toLabToken())
        updateIsLoggedIn(true)
      }
      updateIsCheckingAuth(false)
      response
    } catch (e: Exception) {
      Log.e("AuthViewModel", "Error during token exchange: ${e.message}")
      if (e is CancellationException) {
        throw e
      } else {
        null
      }
    }
  }

  fun updateIsCheckingAuth(isChecking: Boolean) {
    state = state.copy(isCheckingAuth = isChecking)
  }

  fun updateIsLoggedIn(isLoggedIn: Boolean) {
    state = state.copy(isLoggedIn = isLoggedIn)
  }

  fun logout(){
    viewModelScope.launch {
      val tokens = sessionStorage.get()
      val idToken = tokens?.idToken

      // If we have an ID token, use it to inform Keycloak which session to log out.
      if (!idToken.isNullOrEmpty()) {
        val result = repository.performLogout(
          idToken = idToken
        )
        if (!result) {
          Log.e(TAG, "Server logout failed. Proceeding with local logout.")
        }
      } else {
        Log.e(TAG, "No ID token found, proceeding with local logout only.")
      }

      // Clear local session regardless of server logout result.
      sessionStorage.clear()
      updateIsLoggedIn(false)
    }
  }

  companion object {
    private val TAG = "MainViewModel"
  }
}



