package com.oceloti.lemc.labauthentication.network.responsemodels

import com.google.gson.annotations.SerializedName
import com.oceloti.lemc.labauthentication.data.models.LabToken

/**
 * Represents the response from the OpenID Connect token endpoint.
 *
 * This class models the JSON response returned when exchanging an authorization code
 * for tokens, as specified in the OpenID Connect Core 1.0 specification.
 *
 * @see [OpenID Connect Core 1.0: Token Response](https://openid.net/specs/openid-connect-core-1_0.html#TokenResponse)
 *
 * @property accessToken The token used to access protected resources on behalf of the user.
 *                       This corresponds to the `access_token` field in the OpenID Connect response.
 * @property refreshToken A token used to obtain a new access token without requiring user interaction.
 *                        This corresponds to the `refresh_token` field. It may be null if not provided.
 * @property idToken A JSON Web Token (JWT) containing claims about the authenticated user.
 *                   This corresponds to the `id_token` field. It may be null if the `openid` scope
 *                   was not requested or granted.
 * @property tokenType The type of token issued. Typically, this is `"Bearer"`, allowing the
 *                     client to use the access token in the `Authorization` header for API requests.
 *                     This corresponds to the `token_type` field.
 * @property expiresIn The lifetime of the access token in seconds. After this time, the token
 *                     becomes invalid. This corresponds to the `expires_in` field.
 *
 * Example of the JSON response this class maps to:
 * ```
 * {
 *   "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
 *   "refresh_token": "8xLOxBtZp8",
 *   "id_token": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
 *   "token_type": "Bearer",
 *   "expires_in": 3600
 * }
 * ```
 */
data class TokenResponseModel(
  @SerializedName("access_token") val accessToken: String,
  @SerializedName("refresh_token") val refreshToken: String?,
  @SerializedName("id_token") val idToken: String?,
  @SerializedName("token_type") val tokenType: String,
  @SerializedName("expires_in") val expiresIn: Int
)

fun TokenResponseModel.toLabToken(): LabToken {
  return LabToken(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    idToken = this.idToken,
    tokenType = this.tokenType,
    expiresIn = this.expiresIn
  )
}