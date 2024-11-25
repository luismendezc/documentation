package com.oceloti.lemc.labauthentication

import android.app.Application
import com.oceloti.lemc.labauthentication.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@MyApp)
      modules(appModule)
    }
  }
}