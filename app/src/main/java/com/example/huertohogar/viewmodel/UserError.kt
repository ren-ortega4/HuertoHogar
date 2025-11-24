package com.example.huertohogar.viewmodel

data class UserError(
    // Errores de Registro
    val nombre: String? = null,
    val apellido: String? = null,
    val correo: String? = null,
    val contrasena: String? = null,
    val confirmarContrasena: String? = null,
    val region: String? = null,

    // Errores de Login
    val errorLoginCorreo: String? = null,
    val errorLoginClave: String? = null,

    val errorLoginGeneral: String?=null
)