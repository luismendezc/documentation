package com.oceloti.lemc.labauthentication.security

import android.content.SharedPreferences
import com.oceloti.lemc.labauthentication.network.LabToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
  private val sharedPreferences: SharedPreferences
) : SessionStorage {
  override suspend fun get(): LabToken? {
    return with(Dispatchers.IO){
      val json = sharedPreferences.getString(KEY_AUTH_INFO, null) ?: return@with null
      Json.decodeFromString<LabToken>(json)
    }
  }

  override suspend fun set(info: LabToken?) {
    withContext(Dispatchers.IO) {
      if (info == null) {
        sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
        return@withContext
      }
      val json = Json.encodeToString(info)
      sharedPreferences.edit().putString(KEY_AUTH_INFO, json).apply()
    }
  }

  companion object {
    private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
  }

}