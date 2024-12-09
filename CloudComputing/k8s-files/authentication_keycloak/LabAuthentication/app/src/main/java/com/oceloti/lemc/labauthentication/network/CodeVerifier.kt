package com.oceloti.lemc.labauthentication.network

interface CodeVerifier {
  var code: String?

  fun setCodeVerifier(verifier: String)

  fun resetCodeVerifier()
}