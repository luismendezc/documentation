package com.oceloti.lemc.labauthentication.di

import com.oceloti.lemc.labauthentication.network.AuthApi
import com.oceloti.lemc.labauthentication.network.provideOkHttpClient
import com.oceloti.lemc.labauthentication.repository.AuthRepository
import com.oceloti.lemc.labauthentication.viewmodel.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {

  single { provideOkHttpClient(androidContext()) }

  single {
    Retrofit.Builder()
      .baseUrl("https://10.151.130.198:32080/")
      .client(get())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(AuthApi::class.java)
  }

  single { AuthRepository(get()) }

  viewModelOf(::AuthViewModel)
}
