package com.example.huertohogar.viewmodel

import com.example.huertohogar.model.User

/**
 * Representa el estado completo de la UI relacionado con el usuario.
 * ESTA ES LA VERSIÓN FINAL Y CORRECTA
 */
data class UserUiState(
    // --- Estado de la Sesión ---
    val currentUser: User? = null,
    val isLoggedIn: Boolean = false,
    val authToken: String? = null, // Para guardar el token JWT de la API

    // --- Estado del Formulario de Registro ---
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val clave: String = "", // Nombre corregido
    val confirmarClave: String = "", // Nombre corregido
    val region: String = "",
    val aceptaTerminos: Boolean = false,

    // --- Estado del Formulario de Login ---
    val loginCorreo: String = "",
    val loginClave: String = "",

    // --- Estado Común / Otros ---
    val recordarUsuario: Boolean = false,

    val registroExitoso: Boolean = false,
    val errores: UserError = UserError()
)
