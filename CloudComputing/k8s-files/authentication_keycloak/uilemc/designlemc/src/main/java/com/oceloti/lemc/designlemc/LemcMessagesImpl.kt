package com.oceloti.lemc.designlemc

internal class LemcMessagesImpl(
  private val sdkImpl: LemcSDKImpl
) : LemcMessages {

  override fun all(): LemcMessageObservable {
    return LemcMessageObservableImpl(sdkImpl) { true }
  }

  override fun onlyErrors(): LemcMessageObservable {
    // If the message contains "ERROR", it’s an error
    return LemcMessageObservableImpl(sdkImpl) { message ->
      message.contains("ERROR", ignoreCase = true)
    }
  }

  override fun onlySuccess(): LemcMessageObservable {
    return LemcMessageObservableImpl(sdkImpl) { message ->
      message.contains("SUCCESS", ignoreCase = true)
    }
  }

  override fun onlyUpdates(): LemcMessageObservable {
    return LemcMessageObservableImpl(sdkImpl) { message ->
      message.contains("UPDATE", ignoreCase = true)
    }
  }
}
