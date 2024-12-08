package com.oceloti.lemc.labauthentication.network.networkinterfaces

import com.oceloti.lemc.labauthentication.network.responsemodels.TokenResponseModel
import retrofit2.http.*

/**
 * Interface for authentication-related network operations using OpenID Connect.
 *
 * This interface defines an API contract for exchanging an authorization code for tokens,
 * following the OpenID Connect protocol. It uses Retrofit to handle HTTP requests and responses.
 */
interface AuthApi {

  /**
   * Exchanges an authorization code for tokens as per OpenID Connect standards.
   *
   * This method sends a POST request to the token endpoint with the following fields:
   * - `grant_type`: Specifies the type of grant being used. For authorization code flow, this is always `"authorization_code"`.
   * - `code`: The authorization code received from the authorization server after user consent.
   * - `redirect_uri`: The same URI provided during the initial authorization request to ensure consistency.
   * - `code_verifier`: The plaintext code verifier (part of PKCE) that corresponds to the code challenge sent earlier.
   * - `client_id`: The identifier for the client application, registered with the authorization server.
   *
   * @param grantType The type of grant being used. Must be `"authorization_code"`.
   * @param code The authorization code received from the authorization server.
   * @param redirectUri The redirection URI provided during the authorization request.
   * @param codeVerifier The PKCE code verifier for enhanced security.
   * @param clientId The client application's registered identifier.
   * @return A [TokenResponseModel] containing the tokens issued by the authorization server.
   *
   * @throws retrofit2.HttpException If the server responds with an error.
   * @throws retrofit2.RetrofitError If there are network or serialization issues.
   */
  @POST("realms/oauthrealm/protocol/openid-connect/token")
  @FormUrlEncoded
  suspend fun exchangeToken(
    @Field("grant_type") grantType: String,
    @Field("code") code: String,
    @Field("redirect_uri") redirectUri: String,
    @Field("code_verifier") codeVerifier: String,
    @Field("client_id") clientId: String
  ): TokenResponseModel

  @FormUrlEncoded
  @POST("realms/oauthrealm/protocol/openid-connect/token")
  suspend fun refreshToken(
    @Field("grant_type") grantType: String = "refresh_token",
    @Field("refresh_token") refreshToken: String,
    @Field("client_id") clientId: String
  ): TokenResponseModel

  @FormUrlEncoded
  @POST("realms/oauthrealm/protocol/openid-connect/logout")
  suspend fun logout(
    @Field("client_id") clientId: String,
    @Field("id_token_hint") idTokenHint: String,
  ): retrofit2.Response<Void>


}


