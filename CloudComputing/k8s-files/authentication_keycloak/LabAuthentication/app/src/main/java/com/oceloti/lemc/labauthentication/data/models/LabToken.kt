package com.oceloti.lemc.labauthentication.data.models

import com.oceloti.lemc.labauthentication.network.responsemodels.TokenResponseModel
import kotlinx.serialization.Serializable

@Serializable
data class LabToken(
  val accessToken: String,
  val refreshToken: String?,
  val idToken: String?,
  val tokenType: String,
  val expiresIn: Int
)

fun LabToken.toTokenResponse(): TokenResponseModel {
  return TokenResponseModel(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    idToken = this.idToken,
    tokenType = this.tokenType,
    expiresIn = this.expiresIn
  )
}