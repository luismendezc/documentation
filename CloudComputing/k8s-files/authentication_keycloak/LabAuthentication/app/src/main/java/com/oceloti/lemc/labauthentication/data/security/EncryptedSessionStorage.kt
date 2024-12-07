package com.oceloti.lemc.labauthentication.data.security

import android.content.SharedPreferences
import com.oceloti.lemc.labauthentication.data.models.LabToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
  private val sharedPreferences: SharedPreferences
) : SessionStorage {
  override fun get(): LabToken? {
    val json = sharedPreferences.getString(KEY_AUTH_INFO, null) ?: return null
    return Json.decodeFromString<LabToken>(json)
  }

  override fun set(info: LabToken?) {
      if (info == null) {
        sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
        return
      }
      val json = Json.encodeToString(info)
      sharedPreferences.edit().putString(KEY_AUTH_INFO, json).apply()
  }

  override fun clear() {
      sharedPreferences.edit().clear().apply()
  }


  companion object {
    private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
  }

}