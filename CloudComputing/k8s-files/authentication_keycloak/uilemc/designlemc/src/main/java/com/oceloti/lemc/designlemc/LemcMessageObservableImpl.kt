package com.oceloti.lemc.designlemc

internal class LemcMessageObservableImpl(
  private val sdkImpl: LemcSDKImpl,
  private val filter: (String) -> Boolean
) : LemcMessageObservable {
  override fun subscribe(subscriber: LemcMessageSubscriber): LemcDisposable {
    return sdkImpl.addSubscriber(subscriber, filter)
  }
}
