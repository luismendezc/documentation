package com.oceloti.lemc.labauthentication.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Utility object for generating and handling PKCE (Proof Key for Code Exchange) values.
 *
 * PKCE is a security mechanism used in OAuth 2.0 and OpenID Connect flows to mitigate
 * authorization code interception attacks.
 */
object PKCEUtil {

  /**
   * Generates a random, high-entropy code verifier as per PKCE specifications.
   *
   * The code verifier is a cryptographically random string that is later used to generate
   * a code challenge and is sent to the token endpoint during the exchange process.
   *
   * @return A Base64-encoded, URL-safe string representing the code verifier.
   */
  fun generateCodeVerifier(): String {
    val secureRandom = SecureRandom()
    val code = ByteArray(32)
    secureRandom.nextBytes(code)
    return Base64.encodeToString(code, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
  }

  /**
   * Generates a SHA-256-based code challenge from the given code verifier.
   *
   * The code challenge is derived from the code verifier and sent to the authorization
   * server during the authorization request. The server uses the code challenge to
   * validate the code verifier during the token exchange process.
   *
   * @param codeVerifier The code verifier generated using [generateCodeVerifier].
   * @return A Base64-encoded, URL-safe string representing the code challenge.
   */
  fun generateCodeChallenge(codeVerifier: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
  }
}