package com.example.teammaravillaapp.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // ⚠️ placeholder: luego lo pones al del swagger
    private const val BASE_URL = "https://example.com/api/"

    private val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val okHttp: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}