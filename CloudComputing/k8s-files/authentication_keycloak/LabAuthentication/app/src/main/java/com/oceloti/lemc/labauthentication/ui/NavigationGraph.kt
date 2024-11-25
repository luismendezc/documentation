package com.oceloti.lemc.labauthentication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oceloti.lemc.labauthentication.MainActivity
import com.oceloti.lemc.labauthentication.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: AuthViewModel) {
  val navigateToTokenScreen = viewModel.navigateToTokenScreen.collectAsState()

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
}



