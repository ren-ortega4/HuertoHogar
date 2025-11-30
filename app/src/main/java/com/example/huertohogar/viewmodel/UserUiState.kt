package com.example.huertohogar.viewmodel

import com.example.huertohogar.model.User

data class UserUiState(

    // --- Estado de la Sesión ---
    val currentUser: User? = null,
    val idApi : Int? =null,
    val isLoggedIn: Boolean = false,
    val authToken: String? = null, // Para guardar el token JWT de la API
    // Estado General de la UI
    val isLoading: Boolean = false,


    // Formulario de Registro
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val region: String = "",
    val aceptaTerminos: Boolean = false,

    // Formulario de Login
    val loginCorreo: String = "",
    val loginClave: String = "",

    // --- Estado Común / Otros ---
    val recordarUsuario: Boolean = false,

    val registroExitoso: Boolean = false,
    val errores: UserError = UserError()
)