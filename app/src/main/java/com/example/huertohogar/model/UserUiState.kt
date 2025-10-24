package com.example.huertohogar.model

data class UserUiState(
    val nombre : String="",
    val correo :String = "",
    val clave : String = "",
    val confirmarClave: String = "",
    val direccion : String = "",
    val region :String = "",
    val recordarUsuario: Boolean = false,
    val aceptaTerminos : Boolean = false,
    val errores : UserError = UserError()
) {
}