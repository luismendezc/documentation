package com.oceloti.lemc.designuilemc

import com.oceloti.lemc.designlemc.LemcSDK
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.AnimationOperationResult
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.LemcOperationCallback
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.LemcOperationObserver

internal class DesignUiLemc(
  private val lemcSDK: LemcSDK,
  private val animationOperationObserver: LemcOperationObserver<AnimationOperationResult>
) : DesignUiLemcInterface {
  override fun initializeSDK() {
    lemcSDK.initialize(LemcSDK.Config(LemcSDK.Environment.DEV, "en"))
  }

  override fun startFlowSDK(email: String, token: String, callback: (LemcOperationCallback<AnimationOperationResult>) -> Unit) {
    unsubscribeOperationObservers()
    lemcSDK.authenticate(LemcSDK.AuthParams(userEmail = email, token = token))
    animationOperationObserver.subscribe(
      hostingMode = true,
      messages = lemcSDK.getMessages(),
      callback = callback
    )
  }

  private fun unsubscribeOperationObservers(){
    animationOperationObserver.unsubscribe()
  }

}