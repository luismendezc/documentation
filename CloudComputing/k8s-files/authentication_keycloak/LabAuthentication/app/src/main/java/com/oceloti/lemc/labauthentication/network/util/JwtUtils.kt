package com.oceloti.lemc.labauthentication.network.util

import android.util.Log
import com.nimbusds.jwt.SignedJWT
import com.oceloti.lemc.labauthentication.presentation.models.LabUser
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.JwtConsumerBuilder

object JwtUtils {
  fun decodeIdToken(idToken: String): LabUser {
    val signedJWT = SignedJWT.parse(idToken)
    val claims = signedJWT.jwtClaimsSet

    Log.d("AuthInterceptor", "Decoded Claims: $claims")

    return LabUser(
      id = claims.subject,
      name = claims.getStringClaim("name"),
      firstName = claims.getStringClaim("given_name"),
      lastName = claims.getStringClaim("family_name"),
      email = claims.getStringClaim("email"),
      picture = claims.getStringClaim("picture"),
      nonce = claims.getStringClaim("nonce")
    )
  }

  fun decodeJwtAndCheckExpiry(token: String): Boolean {
    return try {
      val jwtConsumer = JwtConsumerBuilder()
        .setSkipSignatureVerification() // Only decoding
        .setExpectedAudience("https://10.151.130.198:32080/realms/oauthrealm") // Adjust expected audience
        .build()
      val jwtClaims: JwtClaims = jwtConsumer.processToClaims(token)

      val expiryTime = jwtClaims.getExpirationTime().value
      val currentTime = System.currentTimeMillis() / 1000
      Log.d("AuthInterceptor", "Expiry Time: $expiryTime, Current Time: $currentTime")

      currentTime < expiryTime
    } catch (e: Exception) {
      Log.e("AuthInterceptor", "Failed to decode JWT: ${e.message}")
      false
    }
  }


}