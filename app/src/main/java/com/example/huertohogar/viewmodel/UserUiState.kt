package com.example.huertohogar.viewmodel

data class UserUiState(
    // Estado de Registro
    val id : Int=0,
    val nombre : String="",
    val correo :String = "",
    val fotopefil: String?=null,



    val clave : String = "",
    val confirmarClave: String = "",
    val direccion : String = "",
    val region :String = "",
    val aceptaTerminos : Boolean = false,

    // Estado de Login
    val loginCorreo: String = "",
    val loginClave: String = "",

    // Estado de Sesión esto sirve para el cambio de estado de inicio de sesion
    val isLoggedIn: Boolean = false,

    // Estado Común
    val recordarUsuario: Boolean = false,
    val errores : UserError = UserError()
)