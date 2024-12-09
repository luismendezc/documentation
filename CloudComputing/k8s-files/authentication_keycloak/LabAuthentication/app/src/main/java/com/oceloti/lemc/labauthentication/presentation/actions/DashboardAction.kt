package com.oceloti.lemc.labauthentication.presentation.actions

sealed interface DashboardAction {
  data object OnSignInClick: DashboardAction
  data object OnSignUpClick: DashboardAction
}