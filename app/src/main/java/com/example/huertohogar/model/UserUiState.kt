package com.example.huertohogar.model

data class UserUiState(
    // Estado de Registro
    val nombre : String="",
    val correo :String = "",
    val clave : String = "",
    val confirmarClave: String = "",
    val direccion : String = "",
    val region :String = "",
    val aceptaTerminos : Boolean = false,

    // Estado de Login
    val loginCorreo: String = "",
    val loginClave: String = "",

    // Estado Común
    val recordarUsuario: Boolean = false,
    val errores : UserError = UserError()
) {
}