package com.oceloti.lemc.labauthentication.presentation.states

data class MainState(
  val isLoggedIn: Boolean = false,
  val isCheckingAuth: Boolean = false
)