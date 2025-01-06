package com.oceloti.lemc.uilemc

import com.oceloti.lemc.designlemc.LemcSDK

interface LemcServiceInterface {
  fun initialize(
    environment: LemcSDK.Environment,
    callback: (LemcOperationCallback<Unit>) -> Unit)
  fun startFlow(email: String, token: String, callback: (LemcOperationCallback<AuthOperationResult>) -> Unit)
  fun proveSdkStatus()
}