package com.oceloti.lemc.designuilemc

import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.AnimationOperationResult
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.LemcOperationCallback

internal interface DesignUiLemcInterface {
  fun initializeSDK()
  fun startFlowSDK(email: String, token: String, callback: (LemcOperationCallback<AnimationOperationResult>) -> Unit)
}