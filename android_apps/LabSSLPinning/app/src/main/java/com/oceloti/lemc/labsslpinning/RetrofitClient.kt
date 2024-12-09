package com.oceloti.lemc.labsslpinning

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://10.151.130.198/"

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val client2 = OkHttpClient.Builder()
        .hostnameVerifier { hostname, session ->
            hostname == "10.151.130.198"
        }
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }



    private fun getUnsafeOkHttpClient() = okhttp3.OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true}
        .build()
}