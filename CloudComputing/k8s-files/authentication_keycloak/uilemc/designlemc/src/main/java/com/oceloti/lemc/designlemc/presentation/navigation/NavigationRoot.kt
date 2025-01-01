package com.oceloti.lemc.designlemc.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.gson.Gson
import com.oceloti.lemc.designlemc.LemcResponseModel
import com.oceloti.lemc.designlemc.LemcSDK
import com.oceloti.lemc.designlemc.LemcSDKImpl
import com.oceloti.lemc.designlemc.LemcSdkOperation
import com.oceloti.lemc.designlemc.presentation.actions.DrawingAction
import com.oceloti.lemc.designlemc.presentation.components.DrawingCanvas
import com.oceloti.lemc.designlemc.presentation.screens.LemcAuthScreen
import com.oceloti.lemc.designlemc.presentation.screens.LemcFlowScreen
import com.oceloti.lemc.designlemc.presentation.screens.LemcMapScreenRoot
import com.oceloti.lemc.designlemc.presentation.states.allColors
import com.oceloti.lemc.designlemc.presentation.viewmodels.DrawingViewModel

@Composable
fun NavigationRoot(
  navController: NavHostController,
  email: String,
  token: String,
  lemcSdk: LemcSDK,
  onFinish: () -> Unit
) {
  NavHost(
    navController = navController,
    startDestination = "lemcAuth"
  ) {
    lemcAuthGraph(navController, email, token, (lemcSdk as LemcSDKImpl), onFinish)
  }
}

private fun NavGraphBuilder.lemcAuthGraph(
  navController: NavHostController,
  email: String,
  token: String,
  sdkImpl: LemcSDKImpl,
  onFinish: () -> Unit
) {
  navigation(
    startDestination = "intro",
    route = "lemcAuth"
  ) {
    composable(route = "intro") {
      LemcAuthScreen(
        email = email,
        token = token,
        onError = { errorMsg ->
          val errorPayload = LemcResponseModel(
            operation = LemcSdkOperation.AUTH,
            traceId = "someTraceId",
            error = LemcResponseModel.Error(
              type = "CRITICAL",
              errorClass = "SOME_ERROR_CLASS",
              title = "AuthError",
              message = "Something went wrong",
              errorCode = "123"
            )
          )
          val errorJson = Gson().toJson(errorPayload)
          sdkImpl.emitMessage("ERROR")
          onFinish()
        },
        onSuccess = {
          val successPayload = LemcResponseModel(
            operation = LemcSdkOperation.AUTH,
            traceId = "auth-trace-id-456",
            success = LemcResponseModel.Success(
              status = "SUCCEEDED",
              redirectUrl = "https://example.com/myAuthRedirect",
              singleUserMode = false,
              isDeviceSecure = true,
              is2FATransactionActive = false,
              takId = "myTakId",
              sealOneId = "someSealOneId"
            )
          )
          val successJson = Gson().toJson(successPayload)
          sdkImpl.emitMessage(successJson)
          onFinish()
        },
        onUpdate = {
          val updatePayload = LemcResponseModel(
            operation = LemcSdkOperation.AUTH,
            traceId = "auth-trace-id-789",
            update = LemcResponseModel.Update(
              type = "PROGRESS",
              status = "IN_PROGRESS",
              description = "Waiting for user input..."
            )
          )
          val updateJson = Gson().toJson(updatePayload)
          sdkImpl.emitMessage(updateJson)
          navController.navigate("draw")
        },
        onFlowCLick = {
          navController.navigate("flow")
        } ,
        onMapClick = {
          navController.navigate("map")
        }
      )
    }



    composable(route = "draw") {
      val viewModel = viewModel<DrawingViewModel>()
      val state by viewModel.state.collectAsStateWithLifecycle()
      // https://www.youtube.com/watch?v=Eq1ZgoI_QPQ&ab_channel=PhilippLackner
      Column(
        modifier = Modifier
          .fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
      ) {
        DrawingCanvas(
          paths = state.paths,
          currentPath = state.currentPath,
          onAction = viewModel::onAction,
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
          allColors.fastForEach { color ->
            val isSelected = state.selectedColor == color
            Box(
              modifier = Modifier
                .graphicsLayer {
                  val scale = if (isSelected) 1.2f else 1f
                  scaleX = scale
                  scaleY = scale
                }
                .size(40.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                  width = 2.dp,
                  color = if (isSelected) {
                    Color.Black
                  } else {
                    Color.Transparent
                  },
                  shape = CircleShape
                )
                .clickable {
                  viewModel.onAction(DrawingAction.OnSelectColor(color))
                }
            )
          }
        }
        androidx.compose.material3.Button(
          onClick = {
            viewModel.onAction(DrawingAction.OnClearCanvasClick)
          }
        ) {
          Text(text = "Clear Canvas")
        }
      }
    }

    composable(route = "flow") {
      LemcFlowScreen()
    }

    composable(route = "map") {
      LemcMapScreenRoot()
    }


  }
}