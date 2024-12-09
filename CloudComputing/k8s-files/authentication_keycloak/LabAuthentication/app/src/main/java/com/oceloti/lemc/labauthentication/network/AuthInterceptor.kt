package com.oceloti.lemc.labauthentication.network

import android.util.Log
import com.oceloti.lemc.labauthentication.data.repository.AuthRepository
import com.oceloti.lemc.labauthentication.data.security.SessionStorage
import com.oceloti.lemc.labauthentication.network.responsemodels.toLabToken
import com.oceloti.lemc.labauthentication.network.util.JwtUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AuthInterceptor(
  private val sessionStorage: SessionStorage,
  private val authRepository: AuthRepository
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    // Fetch the access token from session storage
    val accessToken = sessionStorage.get()?.accessToken

    // Add Authorization header if access token is available
    val authorizedRequest = accessToken?.let { addAuthorizationHeader(originalRequest, it) } ?: originalRequest

    // Send the initial request
    var response = chain.proceed(authorizedRequest)

    // Handle 401 Unauthorized response
    if (response.code == 401) {
      response.close()

      // Attempt to refresh the token
      val newAccessToken = refreshTokenIfNeeded() ?: return createUnauthorizedResponse(chain)

      // Retry the request with the new token
      val newRequest = addAuthorizationHeader(originalRequest, newAccessToken)
      response = chain.proceed(newRequest)
    }

    return response
  }

  private fun addAuthorizationHeader(request: Request, token: String): Request {
    return request.newBuilder()
      .header("Authorization", "Bearer $token")
      .build()
  }

  private fun refreshTokenIfNeeded(): String? {
    return try {
      runBlocking {
        val tokens = sessionStorage.get()
        val refreshToken = tokens?.refreshToken

        if (refreshToken.isNullOrEmpty() || !JwtUtils.decodeJwtAndCheckExpiry(refreshToken)) {
          return@runBlocking null // Refresh token is invalid or expired
        }

        val newTokens = authRepository.refreshToken(refreshToken)
        sessionStorage.set(newTokens.toLabToken())
        newTokens.accessToken
      }
    } catch (e: Exception) {
      null // Refresh failed
    }
  }

  private fun createUnauthorizedResponse(chain: Interceptor.Chain): Response {
    return Response.Builder()
      .request(chain.request())
      .protocol(okhttp3.Protocol.HTTP_1_1)
      .code(401)
      .message("Unauthorized")
      .body("Unauthorized".toResponseBody())
      .build()
  }
}
