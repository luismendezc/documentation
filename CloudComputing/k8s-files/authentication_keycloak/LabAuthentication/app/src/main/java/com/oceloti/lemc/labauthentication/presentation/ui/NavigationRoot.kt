package com.oceloti.lemc.labauthentication.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.oceloti.lemc.labauthentication.presentation.ui.views.auth.AuthScreenRoot
import com.oceloti.lemc.labauthentication.presentation.ui.components.GradientBackground
import com.oceloti.lemc.labauthentication.presentation.ui.theme.LabAuthenticationWhite
import com.oceloti.lemc.labauthentication.presentation.ui.views.dashboard.DashboardScreenRoot
import com.oceloti.lemc.labauthentication.presentation.util.UiText

@Composable
fun NavigationRoot(navController: NavHostController, isLoggedIn: Boolean) {
  val startDestination = if (isLoggedIn) "dashboard" else "auth"

  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    authGraph(navController)
    dashboardGraph(navController)
    errorGraph(navController)
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
      DashboardScreenRoot(
        onError = { title->
          navController.navigate("error_main?title=${title}") {
            popUpTo("dashboard") {
              inclusive = true
            }
          }
        }
      )
    }
  }
}

private fun NavGraphBuilder.errorGraph(navController: NavHostController) {
  navigation(
    startDestination = "error_main",
    route = "error"
  ) {
    composable(
      route = "error_main?title={title}",
      arguments = listOf(
        navArgument("title") {
          type = NavType.IntType
          defaultValue = 1
        })
    ) { backStackEntry ->
      val title = backStackEntry.arguments?.getInt("title")
      val context = LocalContext.current
      Box(
        modifier = Modifier.fillMaxSize()
      ){
        Column(modifier = Modifier.fillMaxSize()) {
          Text(text = title?.let { UiText.StringResource(it).asString()} ?: "Uknown error", fontSize = 30.sp, modifier = Modifier.fillMaxWidth())
        }
      }
    }
  }
}
