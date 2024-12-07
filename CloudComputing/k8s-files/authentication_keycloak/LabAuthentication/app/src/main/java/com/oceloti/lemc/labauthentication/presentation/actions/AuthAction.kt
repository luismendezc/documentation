package com.oceloti.lemc.labauthentication.presentation.actions

sealed interface AuthAction {
  data object OnSignInClick: AuthAction
  data object OnSignUpClick: AuthAction
}