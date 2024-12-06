package com.oceloti.lemc.labauthentication.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oceloti.lemc.labauthentication.ui.auth.AuthScreenRoot
import com.oceloti.lemc.labauthentication.ui.components.GradientBackground
import com.oceloti.lemc.labauthentication.ui.theme.LabAuthenticationWhite

@Composable
fun NavigationRoot(navController: NavHostController, isLoggedIn: Boolean) {
  // Observe changes to the isLoggedIn state
  LaunchedEffect(isLoggedIn) {
    val destination = if (isLoggedIn) "dashboard" else "auth"
    navController.navigate(destination) {
      popUpTo(navController.graph.startDestinationId) { inclusive = true }
    }
  }

  NavHost(
    navController = navController,
    startDestination =  when {
      isLoggedIn -> "dashboard"
      else -> "auth"
    }
  ) {
    placeholderGraph(navController)
    authGraph(navController)
    dashboardGraph(navController)
  }
}

private fun NavGraphBuilder.placeholderGraph(navController: NavHostController) {
  composable(route = "placeholder") {
    GradientBackground {
      Text(
        text = "Comprobando auth code...",
        color = LabAuthenticationWhite,
        fontSize = 16.sp
      )
    }
  }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController){
  navigation(
    startDestination = "intro",
    route="auth"
  ){
    composable(route= "intro"){
      AuthScreenRoot()
    }
  }
}

private fun NavGraphBuilder.dashboardGraph(navController: NavHostController){
  navigation(
    startDestination = "dashboard_main",
    route="dashboard"
  ){
    composable(route= "dashboard_main"){
      GradientBackground {
        Text(
          text = "Todo salio bien en teoría",
          color = LabAuthenticationWhite,
          fontSize = 16.sp
        )
      }
    }
  }
}



/*
LaunchedEffect(navigateToTokenScreen.value) {
    if (navigateToTokenScreen.value) {
      navController.navigate("tokens")
      viewModel.resetNavigationState()
    }
  }

  NavHost(
    navController = navController,
    startDestination = "main"
  ) {
    composable("main") {
      MainScreen(
        onStartLogin = {
          (navController.context as? MainActivity)?.startOAuthFlow()
        }
      )
    }
    composable("tokens") {
      TokensScreen(viewModel)
    }
  }
 */

