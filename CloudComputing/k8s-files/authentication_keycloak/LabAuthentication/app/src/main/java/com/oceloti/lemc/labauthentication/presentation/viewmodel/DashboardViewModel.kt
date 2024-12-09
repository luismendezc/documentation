package com.oceloti.lemc.labauthentication.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.labauthentication.R
import com.oceloti.lemc.labauthentication.data.repository.AuthRepository
import com.oceloti.lemc.labauthentication.data.repository.StoreRepository
import com.oceloti.lemc.labauthentication.data.security.SessionStorage
import com.oceloti.lemc.labauthentication.network.util.JwtUtils
import com.oceloti.lemc.labauthentication.presentation.events.DashboardEvent
import com.oceloti.lemc.labauthentication.presentation.states.DashboardState
import com.oceloti.lemc.labauthentication.presentation.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(
  private val repository: StoreRepository,
  private val authRepository: AuthRepository,
  private val sessionStorage: SessionStorage,
) : ViewModel() {
  var state by mutableStateOf(DashboardState())
    private set

  private val eventChannel = Channel<DashboardEvent>()
  val events = eventChannel.receiveAsFlow()

  init {
    viewModelScope.launch {
      launch {
        fetchLabUser()
      }
      launch {
        getStores()
      }
    }
  }

  private suspend fun fetchLabUser() {
    sessionStorage.get()?.idToken?.let {
      val labUser = JwtUtils.decodeIdToken(it)
      withContext(Dispatchers.Main.immediate) {
        Log.d("DashboardViewModel", "$labUser")
        state = state.copy(labUser = labUser)
      }
    }
  }

  private suspend fun getStores() {
    try {
      val stores = repository.getStores()
      withContext(Dispatchers.Main.immediate) {
        Log.d("DashboardViewModel", "$stores")
        state = state.copy(stores = stores)
      }
    } catch (e: retrofit2.HttpException) {
      if (e.code() == 401) {
        // Handle unauthorized access (e.g., refresh token expired)
        Log.e("DashboardViewModel", "Unauthorized error: Refresh token might be expired.")
        logout()
      } else {
        // Handle other HTTP errors
        Log.e("DashboardViewModel", "API error: ${e.message()}")
      }
    } catch (e: Exception) {
      // Handle non-HTTP errors
      Log.e("DashboardViewModel", "Unexpected error: ${e.message}")
    }
  }


  private fun logout() {
    viewModelScope.launch {
      val tokens = sessionStorage.get()
      val idToken = tokens?.idToken

      // If we have an ID token, use it to inform Keycloak which session to log out.
      if (!idToken.isNullOrEmpty()) {
        val result = authRepository.performLogout(
          idToken = idToken,
        )
        if (!result) {
          Log.e(TAG, "Server logout failed. Proceeding with local logout.")
        }
      } else {
        Log.e(TAG, "No ID token found, proceeding with local logout only.")
      }

      //Removing tokens:
      sessionStorage.clear()
      eventChannel.send(
        DashboardEvent.Error(UiText.StringResource(R.string.session_not_valid), true)
      )
    }
  }

  companion object {
    private val TAG = "DashboardViewModel"
  }
}