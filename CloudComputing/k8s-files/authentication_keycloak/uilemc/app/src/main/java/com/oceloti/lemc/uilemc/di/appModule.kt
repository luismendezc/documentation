package com.oceloti.lemc.uilemc.di

import com.oceloti.lemc.designlemc.LemcSDK
import com.oceloti.lemc.uilemc.AuthOperationObserver
import com.oceloti.lemc.uilemc.LemcService
import com.oceloti.lemc.uilemc.LemcServiceInterface
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
  single<LemcSDK> { LemcSDK.instance(application = androidApplication()) }
  single {
    AuthOperationObserver()
  }
  single<LemcServiceInterface> {
    LemcService(
      lemcSDK = get(),
      authOperationObserver = get<AuthOperationObserver>()
    )
  }
}