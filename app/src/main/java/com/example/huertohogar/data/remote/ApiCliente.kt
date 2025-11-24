package com.example.huertohogar.data.remote
import  retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ApiCliente {

    private const val  BASE_URL ="http://52.1.232.64:8089/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}