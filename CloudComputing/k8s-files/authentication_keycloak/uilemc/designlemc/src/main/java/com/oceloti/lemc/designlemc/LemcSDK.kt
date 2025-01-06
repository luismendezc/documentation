package com.oceloti.lemc.designlemc

import android.app.Application

interface LemcSDK {
  fun initialize(config: Config)
  fun authenticate(params: AuthParams)
  fun isInitialized(): Boolean

  /**
   * Returns an object that gives you different filters (all, errors, success, updates).
   */
  fun getMessages(): LemcMessages

  data class AuthParams(val userEmail: String?, val token: String?)
  data class Config(val environment: Environment, val locale: String)
  enum class Environment { DEV, QA, PROD }

  companion object {
    @JvmStatic
    fun instance(application: Application): LemcSDK {
      return LemcSDKImpl.getInstance(application)
    }
  }
}

