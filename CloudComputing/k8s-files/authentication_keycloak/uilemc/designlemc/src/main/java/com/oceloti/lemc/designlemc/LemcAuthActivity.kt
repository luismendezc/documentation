package com.oceloti.lemc.designlemc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.oceloti.lemc.designlemc.ui.theme.UilemcTheme
import kotlinx.coroutines.launch

internal class LemcAuthActivity : ComponentActivity() {

  private val sdkImpl: LemcSDKImpl by lazy {
    // Grab the SDKImpl singleton. This is guaranteed to exist if user called LemcSDK.instance()
    LemcSDKImpl.getInstance(application)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Extract parameters from the intent if needed
    val email = intent.getStringExtra("EXTRA_EMAIL")
    val token = intent.getStringExtra("EXTRA_TOKEN")

    setContent {
      UilemcTheme {
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
            finish()
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
            finish()
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
          }
        )
      }
    }
  }
}

