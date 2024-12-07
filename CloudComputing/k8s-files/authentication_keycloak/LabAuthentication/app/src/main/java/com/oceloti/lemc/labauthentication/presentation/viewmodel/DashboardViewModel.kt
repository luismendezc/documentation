package com.oceloti.lemc.labauthentication.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.labauthentication.data.repository.StoreRepository
import com.oceloti.lemc.labauthentication.data.security.SessionStorage
import com.oceloti.lemc.labauthentication.presentation.states.DashboardState
import com.oceloti.lemc.labauthentication.network.util.JwtUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(
  private val repository: StoreRepository,
  private val sessionStorage: SessionStorage,
) : ViewModel() {
  var state by mutableStateOf(DashboardState())
    private set

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
        /*
        withContext(Dispatchers.Main.immediate) {
          // Update state to show logged-out UI or inform the user
          state = state.copy(error = "Session expired. Please log in again.")
        }*/
      } else {
        // Handle other HTTP errors
        Log.e("DashboardViewModel", "API error: ${e.message()}")
      }
    } catch (e: Exception) {
      // Handle non-HTTP errors
      Log.e("DashboardViewModel", "Unexpected error: ${e.message}")
    }
  }

}