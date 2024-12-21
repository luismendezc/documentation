package com.oceloti.lemc.designuilemc

import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.AnimationOperationResult
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.LemcOperationCallback

interface SdkService {
  fun initialize()
  fun startFlow(email: String, token: String)
}