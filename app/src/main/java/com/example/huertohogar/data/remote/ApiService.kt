package com.example.huertohogar.data.remote

import com.example.huertohogar.model.LoginRequest
import com.example.huertohogar.model.LoginResponse
import com.example.huertohogar.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/v1/usuario/guardar")
    suspend fun registarUsusario(@Body user : User) : Response<Unit>

    @POST("/api/v1/usuario/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>


}