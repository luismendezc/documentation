package com.oceloti.lemc.labauthentication

import android.app.Application
import com.oceloti.lemc.labauthentication.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

  val applicationScope = CoroutineScope(SupervisorJob())

  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@MyApp)
      modules(appModule)
    }
  }
}