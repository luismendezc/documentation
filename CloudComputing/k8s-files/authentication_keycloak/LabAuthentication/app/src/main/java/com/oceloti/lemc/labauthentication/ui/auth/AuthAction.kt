package com.oceloti.lemc.labauthentication.ui.auth

sealed interface AuthAction {
  data object OnSignInClick: AuthAction
  data object OnSignUpClick: AuthAction
}