package com.oceloti.lemc.uilemc

import android.app.Application
import com.oceloti.lemc.designlemc.LemcSDK
import com.oceloti.lemc.uilemc.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

  val applicationScope = CoroutineScope(SupervisorJob())
  val lemcsdk: LemcSDK by inject()

  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@MainApplication)
      modules(appModule)
    }
    // LemcSDK initialization
    lemcsdk.initialize(LemcSDK.Config(
      environment = LemcSDK.Environment.DEV,
      locale = "en"
    ))

  }
}