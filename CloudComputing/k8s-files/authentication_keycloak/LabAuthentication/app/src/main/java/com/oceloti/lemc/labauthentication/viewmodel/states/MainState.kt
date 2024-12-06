package com.oceloti.lemc.labauthentication.viewmodel.states

data class MainState(
  val isLoggedIn: Boolean = false,
  val isCheckingAuth: Boolean = false
)