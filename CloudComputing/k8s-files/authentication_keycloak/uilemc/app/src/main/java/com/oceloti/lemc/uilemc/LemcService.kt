package com.oceloti.lemc.uilemc

import com.oceloti.lemc.designlemc.LemcSDK

class LemcService(
  private val lemcSDK: LemcSDK,
  private val authOperationObserver: LemcOperationObserver<AuthOperationResult>
) : LemcServiceInterface {
  override fun initialize(
    environment: LemcSDK.Environment,
    callback: (LemcOperationCallback<Unit>) -> Unit
  ) {
    //TODO("Not yet implemented")
  }

  override fun startFlow(
    email: String,
    token: String,
    callback: (LemcOperationCallback<AuthOperationResult>) -> Unit
  ) {
    authOperationObserver.unsubscribe()
    lemcSDK.authenticate(LemcSDK.AuthParams(userEmail=email, token=token))
    authOperationObserver.subscribe(
      hostingMode = true,
      messages = lemcSDK.getMessages(),
      callback = callback
    )
  }

  override fun proveSdkStatus() {
    //TODO("Not yet implemented")
  }

}