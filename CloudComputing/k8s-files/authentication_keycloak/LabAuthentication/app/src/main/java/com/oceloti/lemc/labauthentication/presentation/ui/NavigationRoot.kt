package com.oceloti.lemc.labauthentication.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oceloti.lemc.labauthentication.presentation.ui.views.auth.AuthScreenRoot
import com.oceloti.lemc.labauthentication.presentation.ui.components.GradientBackground
import com.oceloti.lemc.labauthentication.presentation.ui.theme.LabAuthenticationWhite
import com.oceloti.lemc.labauthentication.presentation.ui.views.dashboard.DashboardScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController, isLoggedIn: Boolean) {
  val startDestination = if (isLoggedIn) "dashboard" else "auth"

  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    authGraph(navController)
    dashboardGraph(navController)
  }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
  navigation(
    startDestination = "intro",
    route = "auth"
  ) {
    composable(route = "intro") {
      AuthScreenRoot()
    }
  }
}

private fun NavGraphBuilder.dashboardGraph(navController: NavHostController) {
  navigation(
    startDestination = "dashboard_main",
    route = "dashboard"
  ) {
    composable(route = "dashboard_main") {
      DashboardScreenRoot()
    }
  }
}
