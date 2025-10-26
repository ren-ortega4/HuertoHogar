package com.example.huertohogar.viewmodel

import com.example.huertohogar.model.User

data class UserUiState(
    // Estado del Usuario en sesión
    val currentUser: User? = null,
    val isLoggedIn: Boolean = false,

    // Estado de Registro
    val id : Int=0,
    val nombre : String= "",
    val correo : String = "",
    val fotopefil: String?= null,
    val clave : String = "",
    val confirmarClave: String = "",
    val direccion : String = "",
    val region : String = "",
    val aceptaTerminos : Boolean = false,


    // Estado del formulario de Login
    val loginCorreo: String = "",
    val loginClave: String = "",

    // Estado Común / OTROS
    val recordarUsuario: Boolean = false,
    val errores : UserError = UserError()
)