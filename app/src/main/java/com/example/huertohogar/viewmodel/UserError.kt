package com.example.huertohogar.viewmodel

data class UserError(
    // Errores de Registro
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmarClave: String? = null,
    val direccion: String? = null,
    val region: String? = null,

    // Errores de Login
    val errorLoginCorreo: String? = null,
    val errorLoginClave: String? = null,

    val errorLoginGeneral: String?=null
)