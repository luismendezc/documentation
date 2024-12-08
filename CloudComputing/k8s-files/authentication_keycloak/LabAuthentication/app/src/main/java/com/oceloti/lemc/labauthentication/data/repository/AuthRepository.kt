package com.oceloti.lemc.labauthentication.data.repository

import android.util.Log
import com.oceloti.lemc.labauthentication.network.networkinterfaces.AuthApi
import com.oceloti.lemc.labauthentication.network.responsemodels.TokenResponseModel
import retrofit2.HttpException

/**
 * Repository class responsible for handling token exchange operations with the authentication server.
 *
 * @property api An instance of [AuthApi] used to perform network calls for authentication.
 */
class AuthRepository(private val api: AuthApi) {

  companion object {
    private const val GRANT_TYPE = "authorization_code"
    private const val CLIENT_ID = "lab-authentication-client"
    private const val TAG = "AuthRepository"
  }

  /**
   * Exchanges an authorization code for an access token, refresh token, and ID token.
   *
   * This method makes a call to the token endpoint using the provided authorization code, redirect URI, and
   * PKCE code verifier to retrieve the OpenID Connect tokens.
   *
   * @param code The authorization code received from the OpenID Connect authorization endpoint.
   * @param redirectUri The redirect URI used in the initial authorization request.
   * @param codeVerifier The PKCE code verifier used to secure the token exchange.
   * @return A [TokenResponseModel] object containing access, refresh, and ID tokens.
   * @throws retrofit2.HttpException If the server returns an HTTP error.
   * @throws Exception For any unexpected errors during the network call.
   */
  suspend fun exchangeToken(
    code: String,
    redirectUri: String,
    codeVerifier: String
  ): TokenResponseModel {
    Log.d(TAG, "Token exchange request initiated")
    Log.d(TAG, "grant_type=$GRANT_TYPE")
    Log.d(TAG, "code=$code")
    Log.d(TAG, "redirect_uri=$redirectUri")
    Log.d(TAG, "code_verifier=$codeVerifier")
    Log.d(TAG, "client_id=$CLIENT_ID")

    return try {
      // Make the API call to exchange the authorization code for tokens.
      val response = api.exchangeToken(
        grantType = GRANT_TYPE,
        code = code,
        redirectUri = redirectUri,
        codeVerifier = codeVerifier,
        clientId = CLIENT_ID
      )
      Log.d(TAG, "Token exchange successful: $response")
      response
    } catch (e: HttpException) {
      // Log and rethrow HTTP errors returned by the server.
      Log.e(TAG, "Token exchange failed: ${e.response()?.errorBody()?.string()}")
      Log.e(TAG, "HTTP Code: ${e.code()}")
      throw e
    } catch (e: Exception) {
      // Log and rethrow any unexpected errors during the API call.
      Log.e(TAG, "Unexpected error: ${e.message}")
      throw e
    }
  }

  suspend fun refreshToken(refreshToken: String): TokenResponseModel {
    return api.refreshToken(
      refreshToken = refreshToken,
      clientId = "lab-authentication-client"
    )
  }

  suspend fun performLogout(idToken: String): Boolean {
    return try {
      val response = api.logout(
        clientId = "lab-authentication-client",
        idTokenHint = idToken
      )
      response.isSuccessful
    } catch (e: Exception) {
      Log.e(TAG, "Logout request failed: ${e.message}")
      false
    }
  }
}



