package com.example.mytodolist.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://hive.mrdekk.ru/todo/"
    private const val TOKEN = "Earendil"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${TOKEN}")
                .build()
            chain.proceed(newRequest)
        }
        .build()

    val instance: TodoApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(TodoApi::class.java)
    }
}

val api = RetrofitClient.instance