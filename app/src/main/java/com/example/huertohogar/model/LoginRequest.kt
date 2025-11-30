package com.example.huertohogar.model

import com.google.gson.annotations.SerializedName
data class LoginRequest(

    // Tu backend espera "correo"
    @SerializedName("correo")
    val correo: String,

    // Tu backend espera "contrasena"
    @SerializedName("contrasena")
    val contrasena: String
)