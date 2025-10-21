package com.example.huertohogar.model

data class Notificacion (
    val id: Int,
    val mensaje: String,
    val leido: Boolean = false
)