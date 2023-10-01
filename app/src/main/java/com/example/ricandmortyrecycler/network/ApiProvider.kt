package com.example.ricandmortyrecycler.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiProvider {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val api: RickAndMortyAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /*
   создается экземпляр интерфейса RickAndMortyAPI через метод create()
   Это возможно благодаря прокси Джава (хитрая штука) и механизмам рефлексии.*/
        retrofit.create(RickAndMortyAPI::class.java)
    }
}