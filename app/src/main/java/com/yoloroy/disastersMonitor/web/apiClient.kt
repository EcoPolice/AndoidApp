package com.yoloroy.disastersMonitor.web

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://84.252.135.1/"

val apiClient: Link
    get() {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build().create(Link::class.java)
    }
