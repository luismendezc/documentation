package com.oceloti.lemc.labauthentication.network

import kotlinx.serialization.Serializable

@Serializable
data class LabToken(
  val accessToken: String,
  val refreshToken: String?,
  val idToken: String?,
  val tokenType: String,
  val expiresIn: Int
)

fun LabToken.toTokenResponse(): TokenResponse {
  return TokenResponse(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    idToken = this.idToken,
    tokenType = this.tokenType,
    expiresIn = this.expiresIn
  )
}