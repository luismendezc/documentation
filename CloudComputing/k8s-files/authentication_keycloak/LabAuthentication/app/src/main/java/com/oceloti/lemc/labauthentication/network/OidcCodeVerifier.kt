package com.oceloti.lemc.labauthentication.network

import android.util.Log

class OidcCodeVerifier: CodeVerifier {
  override var code: String? = null

  override fun setCodeVerifier(verifier: String) {
    Log.d(TAG, "Code verifier set: $verifier")
    code = verifier
  }

  override fun resetCodeVerifier(){
    Log.d(TAG, "Code verifier is null")
    code = null
  }

  companion object {
    private val TAG = "OidcCodeVerifier"
  }

}