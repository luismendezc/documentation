package com.oceloti.lemc.labauthentication.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.oceloti.lemc.labauthentication.network.AuthApi
import com.oceloti.lemc.labauthentication.network.CodeVerifier
import com.oceloti.lemc.labauthentication.network.OidcCodeVerifier
import com.oceloti.lemc.labauthentication.network.provideOkHttpClient
import com.oceloti.lemc.labauthentication.repository.AuthRepository
import com.oceloti.lemc.labauthentication.security.EncryptedSessionStorage
import com.oceloti.lemc.labauthentication.security.SessionStorage
import com.oceloti.lemc.labauthentication.viewmodel.AuthViewModel
import com.oceloti.lemc.labauthentication.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
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

  single<CodeVerifier>{ OidcCodeVerifier() }
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

  viewModelOf(::MainViewModel)
  viewModelOf(::AuthViewModel)
}
