package com.oceloti.lemc.labauthentication.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import com.oceloti.lemc.labauthentication.R
import javax.net.ssl.TrustManagerFactory

/**
 * Provides an instance of OkHttpClient with custom SSL configuration and logging interceptor.
 *
 * This method builds an OkHttpClient instance tailored for secure communication with a server that requires
 * a specific PEM certificate. It includes:
 * - A custom SSL configuration applied via [OkHttpClient.Builder.addCustomSslConfig].
 * - A logging interceptor to monitor network traffic for debugging purposes.
 *
 * @param context The Android context used for resource access, specifically the custom SSL certificate file.
 * @return A fully configured OkHttpClient instance.
 *
 * @throws java.security.KeyStoreException If there is an issue initializing the KeyStore in the SSL configuration.
 * @throws java.security.cert.CertificateException If the certificate format is invalid.
 * @throws javax.net.ssl.SSLException If there is an issue initializing the SSL context.
 */
fun provideOkHttpClient(context: Context): OkHttpClient {
  return OkHttpClient.Builder()
    .addCustomSslConfig(context)
    .addInterceptor(HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    })
    .build()
}

/**
 * Extension function for OkHttpClient.Builder to configure a custom SSL context.
 *
 * This method adds a custom SSL configuration to the OkHttpClient.Builder by:
 * - Loading a specific X.509 certificate from the app's raw resources.
 * - Creating a KeyStore containing the certificate.
 * - Initializing a TrustManagerFactory with the KeyStore.
 * - Creating and configuring an SSLContext using the TrustManager.
 *
 * @param context The Android context used to access resources, particularly the PEM certificate file.
 * @return The updated OkHttpClient.Builder instance with the custom SSL configuration applied.
 *
 * @throws java.security.KeyStoreException If there is an issue initializing the KeyStore.
 * @throws java.security.cert.CertificateException If the certificate format is invalid.
 * @throws javax.net.ssl.SSLException If there is an issue initializing the SSL context.
 */
fun OkHttpClient.Builder.addCustomSslConfig(context: Context): OkHttpClient.Builder {
  val certificateFactory = CertificateFactory.getInstance("X.509")
  val inputStream = context.resources.openRawResource(R.raw.my_cert)
  val certificate = certificateFactory.generateCertificate(inputStream)
  inputStream.close()

  val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
    load(null)
    setCertificateEntry("ca", certificate)
  }

  val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
  trustManagerFactory.init(keyStore)

  val sslContext = SSLContext.getInstance("TLS")
  sslContext.init(null, trustManagerFactory.trustManagers, null)

  return this.sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as javax.net.ssl.X509TrustManager)
}

