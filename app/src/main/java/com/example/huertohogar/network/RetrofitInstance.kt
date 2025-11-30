package com.example.huertohogar.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://3.225.35.217:8080/"//"http://192.168.1.189:8080/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val mercadoPagoApi: MercadoPagoApi by lazy {
        retrofit.create(MercadoPagoApi::class.java)
    }
}