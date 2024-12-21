package com.oceloti.lemc.designuilemc

import android.util.Log
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.AnimationOperationResult
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.LemcOperationCallback

internal class LemcSdkService(
  private val designUiLemc: DesignUiLemcInterface
): SdkService {
  override fun initialize() {
    Log.d(TAG, "initialize()")
    designUiLemc.initializeSDK()
  }

  override fun startFlow(email: String, token: String) {
    Log.d(TAG, "startFlow()")
    designUiLemc.startFlowSDK(email, token) { animationResult ->
      when(animationResult){
        is LemcOperationCallback.Error -> {
          Log.d(TAG, "LemcOperationCallback.Error")
        }
        is LemcOperationCallback.Success -> {
          Log.d(TAG, "LemcOperationCallback.Success")
        }
      }
    }
  }

  companion object {
    private val TAG = "LemcSdkService"
  }

}