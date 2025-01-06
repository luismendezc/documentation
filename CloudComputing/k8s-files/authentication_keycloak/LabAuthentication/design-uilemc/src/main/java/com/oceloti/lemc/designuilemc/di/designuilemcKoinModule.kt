package com.oceloti.lemc.designuilemc.di

import com.oceloti.lemc.designlemc.LemcSDK
import com.oceloti.lemc.designuilemc.DesignUiLemc
import com.oceloti.lemc.designuilemc.DesignUiLemcInterface
import com.oceloti.lemc.designuilemc.LemcSdkService
import com.oceloti.lemc.designuilemc.SdkService
import com.oceloti.lemc.designuilemc.logic.lemc.operationobserver.AnimationOperationObserver
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val designuilemcKoinModule = module {
  single<LemcSDK> { LemcSDK.instance(application = androidApplication()) }
  single {
    AnimationOperationObserver()
  }
  single<DesignUiLemcInterface> {
    DesignUiLemc(
      lemcSDK = get(),
      animationOperationObserver = get<AnimationOperationObserver>()
    )
  }

  single<SdkService>{
    LemcSdkService(
      designUiLemc = get()
    )
  }
}