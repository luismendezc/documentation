package com.oceloti.lemc.labauthentication.viewmodel.states

data class AuthState(
  val isLoginInProgress: Boolean = false,
  val isRegisterInProgress: Boolean = false,
  val authUrl: String? = null
)
