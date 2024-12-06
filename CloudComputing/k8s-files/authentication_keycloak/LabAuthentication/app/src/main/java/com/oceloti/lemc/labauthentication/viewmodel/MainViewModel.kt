package com.oceloti.lemc.labauthentication.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.labauthentication.network.CodeVerifier
import com.oceloti.lemc.labauthentication.network.TokenResponse
import com.oceloti.lemc.labauthentication.network.toLabToken
import com.oceloti.lemc.labauthentication.repository.AuthRepository
import com.oceloti.lemc.labauthentication.security.SessionStorage
import com.oceloti.lemc.labauthentication.viewmodel.states.MainState
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
    if (state.isCheckingAuth) {
      Log.d(TAG, "Is alredy checking auth state")
    } else {
      viewModelScope.launch{
        val tokens = sessionStorage.get()
        if (!tokens?.refreshToken.isNullOrEmpty()) {
          updateIsLoggedIn(true)
          updateIsCheckingAuth(false)
        } else {
          updateIsLoggedIn(false)
          updateIsCheckingAuth(false)
        }
      }

    }
  }

  suspend fun exchangeToken(code: String): TokenResponse? {
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

  companion object {
    private val TAG = "MainViewModel"
  }
}



