package com.example.huertohogar.data.repository

import android.util.Log
import com.example.huertohogar.data.remote.ApiService
import com.example.huertohogar.model.LoginRequest
import com.example.huertohogar.model.User

/**
 * ¡VERSIÓN FINAL!
 * Este repositorio se comunica EXCLUSIVAMENTE con la API.
 */
class UsuarioRepository(private val apiService: ApiService) {

    /**
     * Llama al endpoint de la API para registrar un nuevo usuario.
     * Devuelve 'true' si fue exitoso, 'false' en caso contrario.
     */
    suspend fun register(user: User): Boolean {
        return try {
            val response = apiService.registarUsusario(user)
            if (response.isSuccessful) {
                Log.d("UsuarioRepository", "Registro en API exitoso.")
                true
            } else {
                Log.e("UsuarioRepository", "Error en registro API: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Excepción al registrar usuario.", e)
            false
        }
    }

    /**
     * Llama al endpoint de la API para iniciar sesión.
     * Devuelve el token (String) si es exitoso, o null si falla.
     */
    suspend fun login(correo: String, clave: String): String? {
        // ¡CORREGIDO! Ahora usa los nombres correctos de tu backend.
        val loginRequest = LoginRequest(correo = correo, contrasena = clave)
        
        return try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                val token = response.body()?.token 
                Log.d("UsuarioRepository", "Login en API exitoso.")
                token
            } else {
                Log.e("UsuarioRepository", "Error en login API: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Excepción al iniciar sesión.", e)
            null
        }
    }
}
