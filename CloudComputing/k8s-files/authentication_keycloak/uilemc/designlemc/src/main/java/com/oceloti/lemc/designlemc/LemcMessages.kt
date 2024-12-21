package com.oceloti.lemc.designlemc

interface LemcMessages {
  fun all(): LemcMessageObservable
  fun onlyErrors(): LemcMessageObservable
  fun onlySuccess(): LemcMessageObservable
  fun onlyUpdates(): LemcMessageObservable
}

interface LemcMessageObservable {
  fun subscribe(subscriber: LemcMessageSubscriber): LemcDisposable
}

interface LemcMessageSubscriber {
  fun handleMessage(message: String)
}

interface LemcDisposable {
  fun dispose()
}