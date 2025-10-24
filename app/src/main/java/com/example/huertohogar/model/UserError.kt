package com.example.huertohogar.model

data class UserError(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmarClave: String? = null,
    val direccion: String? = null,
    val region:String? = null
)
