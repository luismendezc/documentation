package com.oceloti.lemc.sentryapp

import android.app.Application
import io.sentry.Hint
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid

class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    SentryAndroid.init(this) { options ->
      options.dsn = "copy it from sentry is needed"
      // Add a callback that will be used before the event is sent to Sentry.
      // With this callback, you can modify the event or, when returning null, also discard the event.
      options.beforeSend =
        SentryOptions.BeforeSendCallback { event: SentryEvent, hint: Hint ->
          if (SentryLevel.DEBUG == event.level) {
            null
          } else {
            event
          }
        }
    }
  }
}