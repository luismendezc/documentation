package com.oceloti.lemc.labauthentication.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.oceloti.lemc.labauthentication.MyApp
import com.oceloti.lemc.labauthentication.data.repository.AuthRepository
import com.oceloti.lemc.labauthentication.data.repository.StoreRepository
import com.oceloti.lemc.labauthentication.data.security.EncryptedSessionStorage
import com.oceloti.lemc.labauthentication.data.security.SessionStorage
import com.oceloti.lemc.labauthentication.network.AuthInterceptor
import com.oceloti.lemc.labauthentication.network.CodeVerifier
import com.oceloti.lemc.labauthentication.network.OidcCodeVerifier
import com.oceloti.lemc.labauthentication.network.networkinterfaces.AuthApi
import com.oceloti.lemc.labauthentication.network.networkinterfaces.StoreApi
import com.oceloti.lemc.labauthentication.network.provideOkHttpClient
import com.oceloti.lemc.labauthentication.network.provideOkHttpClientAuth
import com.oceloti.lemc.labauthentication.presentation.viewmodel.AuthViewModel
import com.oceloti.lemc.labauthentication.presentation.viewmodel.DashboardViewModel
import com.oceloti.lemc.labauthentication.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
  // SECURITY
  single {
    EncryptedSharedPreferences(
      androidApplication(),
      "auth_pref",
      MasterKey(androidApplication()),
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
  }

  singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

// DATA REPOSITORY
  single { StoreRepository(get()) }
  single { AuthRepository(get()) }


// NETWORK
  singleOf(::AuthInterceptor).bind<Interceptor>()

  single {
    Retrofit.Builder()
      .baseUrl("https://10.151.130.198:32080/")
      .client(provideOkHttpClientAuth(androidContext()))
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(AuthApi::class.java)
  }

  single {
    Retrofit.Builder()
      .baseUrl("https://10.151.130.198:3000/") // Use the Node.js API base URL
      .client(provideOkHttpClient(androidContext(), lazy { get<AuthInterceptor>() }))
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(StoreApi::class.java)
  }

  single<CodeVerifier> { OidcCodeVerifier() }

  // COROUTINES
  single<CoroutineScope> {
    (androidApplication() as MyApp).applicationScope
  }

  // VIEW MODELS
  viewModelOf(::MainViewModel)
  viewModelOf(::AuthViewModel)
  viewModelOf(::DashboardViewModel)
}
