package com.example.huertohogar.network

import retrofit2.Call
import com.example.huertohogar.model.CartRequest
import com.example.huertohogar.model.PreferenceResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MercadoPagoApi {
    @POST("mercadopago/preference")
    fun createPreference(@Body cartRequest: CartRequest): Call<PreferenceResponse>
}