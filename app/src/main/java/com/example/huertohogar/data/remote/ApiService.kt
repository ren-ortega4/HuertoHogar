package com.example.huertohogar.data.remote

import androidx.room.Delete
import com.example.huertohogar.model.LoginRequest
import com.example.huertohogar.model.LoginResponse
import com.example.huertohogar.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // registro de usuarios
    @POST("/api/v1/usuario/guardar")
    suspend fun registarUsusario(@Body user : User) : Response<User>
    //login de usuario
    @POST("/api/v1/usuario/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>
    // listar usuarios
    @GET("/api/v1/usuario/listar")
    suspend fun getallUser(
        @Header("Authorization") token: String
    ): Response<List<User>>

    // eliminar usuario
    @DELETE("/api/v1/usuario/{id}/eliminar")
    suspend fun eliminarUsuario(
        @Path("id") id : Int
    ): Response<Unit>
}