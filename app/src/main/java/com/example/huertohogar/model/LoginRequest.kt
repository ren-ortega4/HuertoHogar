package com.example.huertohogar.model

import com.google.gson.annotations.SerializedName

/**
 * Representa el cuerpo de la solicitud para el endpoint de login.
 * ¡CORREGIDO! Ahora usa los nombres que tu backend espera.
 */
data class LoginRequest(

    // Tu backend espera "correo"
    @SerializedName("correo")
    val correo: String,

    // Tu backend espera "contrasena"
    @SerializedName("contrasena")
    val contrasena: String
)
