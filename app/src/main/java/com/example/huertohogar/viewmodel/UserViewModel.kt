package com.example.huertohogar.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.model.Usuario
import com.example.huertohogar.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserViewModel(private val repository: UsuarioRepository) : ViewModel(){
    private val _estado = MutableStateFlow(UserUiState())
    val estado : StateFlow<UserUiState> = _estado



    val regiones = listOf(
        "Arica y Parinacota",
        "Tarapacá",
        "Antofagasta",
        "Atacama",
        "Coquimbo",
        "Valparaíso",
        "Metropolitana de Santiago",
        "Libertador General Bernardo O'Higgins",
        "Maule",
        "Ñuble",
        "Biobío",
        "La Araucanía",
        "Los Ríos",
        "Los Lagos",
        "Aysén del General Carlos Ibáñez del Campo",
        "Magallanes y de la Antártica Chilena"
    )

    //<editor-fold desc="Funciones para el formulario de Registro">
    fun onNombreChange (valor : String) { _estado.update { it.copy(nombre = valor, errores = it.errores.copy()) } }
    fun onCorreoChange (valor : String) { _estado.update { it.copy(correo = valor, errores = it.errores.copy()) } }
    fun onClaveChange (valor : String) { _estado.update { it.copy(clave = valor, errores = it.errores.copy()) }}
    fun onConfirmarClaveChange (valor : String) { _estado.update { it.copy(confirmarClave = valor, errores = it.errores.copy()) } }
    fun onDireccionChange (valor : String) { _estado.update { it.copy(direccion = valor, errores = it.errores.copy()) } }
    fun onRegionChange(valor:String){ _estado.update { it.copy(region = valor, errores = it.errores.copy()) } }
    fun onAceptarTerminosChange(valor : Boolean) { _estado.update { it.copy(aceptaTerminos = valor) } }
    //</editor-fold>

    //<editor-fold desc="Funciones para el formulario de Login">
    fun onLoginCorreoChange(valor: String) { _estado.update { it.copy(loginCorreo = valor, errores = it.errores.copy()) } }
    fun onLoginClaveChange(valor: String) { _estado.update { it.copy(loginClave = valor, errores = it.errores.copy()) } }
    //</editor-fold>

    //<editor-fold desc="Funciones de Sesión">
    // En UserViewModel.kt

    fun login() {
        if (!validarLogin()) {
            return
        }

        val correoLogin = _estado.value.loginCorreo
        val claveLogin = _estado.value.loginClave

        viewModelScope.launch {
            val usuarioEncontrado = repository.login(correoLogin, claveLogin)

            if (usuarioEncontrado != null) {
                _estado.update {
                    it.copy(
                        isLoggedIn = true,
                        id = usuarioEncontrado.id,
                        nombre = usuarioEncontrado.nombre,
                        correo = usuarioEncontrado.correo,
                        direccion = usuarioEncontrado.direccion,
                        region = usuarioEncontrado.region,
                        fotopefil = usuarioEncontrado.fotopefil,
                        loginCorreo = "",
                        loginClave = "",
                        errores = UserError()
                    )
                }
            } else {
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(
                            errorLoginGeneral = "Correo o clave incorrectos"
                        )
                    )
                }
            }
        }
    }


    fun logout() {
        // Al cerrar sesión, reseteamos el estado completamente,
        // excepto la preferencia de "recordar usuario".
        _estado.value = UserUiState(recordarUsuario = _estado.value.recordarUsuario)
    }
    // Función común


    fun limpiarFormulario() {
        // Esta función ahora limpia SOLO los campos de los formularios,
        // sin afectar el estado de la sesión (isLoggedIn).
        _estado.update {
            it.copy(
                // Limpia campos de registro (dejando los datos de sesión si existen)
                nombre = "",
                correo = "",
                clave = "",
                confirmarClave = "",
                direccion = "",
                region = "",
                aceptaTerminos = false,
                // Limpia campos de login
                loginCorreo = "",
                loginClave = "",
                // Limpia todos los errores
                errores = UserError()
            )
        }
    }

    fun validarFormularioRegistro(): Boolean{
        val estadoActual=_estado.value
        val erroresNuevos = estadoActual.errores.copy(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es requerido" else null,
            correo = if (estadoActual.correo.isBlank())" Correo es requerido" else if (!estadoActual.correo.contains("@"))"El correo debe tener '@' " else null,
            clave = if (estadoActual.clave.isBlank())"La clave es requerida" else if (estadoActual.clave.length<8)"La clave debe tener al menos 8 caracteres" else null,
            confirmarClave = if (estadoActual.confirmarClave.isBlank()) "Debe repetir la clave" else if (estadoActual.clave != estadoActual.confirmarClave) "Las claves no coinciden" else null,
            direccion = if (estadoActual.direccion.isBlank())"La direccion es requerida" else null,
            region = if(estadoActual.region.isBlank())"la region es requerida" else null,
        )
        val hayErrores= listOfNotNull(
            erroresNuevos.nombre,
            erroresNuevos.correo,
            erroresNuevos.clave,
            erroresNuevos.confirmarClave,
            erroresNuevos.direccion,
            erroresNuevos.region
        ).isNotEmpty()
        _estado.update { it.copy(errores=erroresNuevos) }
        return !hayErrores
    }

    fun validarLogin(): Boolean {
        val estadoActual = _estado.value
        val erroresNuevos = estadoActual.errores.copy(
            errorLoginCorreo = if (estadoActual.loginCorreo.isBlank()) "El correo es requerido" else if (!estadoActual.loginCorreo.contains("@"))"@ en el correo es requerida" else null,
            errorLoginClave = if (estadoActual.loginClave.isBlank()) "La clave es requerida" else if (estadoActual.loginClave.length<8)"Debe tener al menos 8 caracteres" else null,
        )
        val hayErrores = listOfNotNull(
            erroresNuevos.errorLoginCorreo,
            erroresNuevos.errorLoginClave
        ).isNotEmpty()
        _estado.update { it.copy(errores = erroresNuevos) }
        return !hayErrores
    }

    fun guardarUsuario(){
        val estadoActual = _estado.value
        if (validarFormularioRegistro()) {
            viewModelScope.launch {
                val nuevoUsuario= Usuario(
                    nombre = estadoActual.nombre,
                    correo = estadoActual.correo,
                    clave = estadoActual.clave,
                    confirmarClave = estadoActual.confirmarClave,
                    direccion = estadoActual.direccion,
                    region = estadoActual.region,
                    aceptaTerminos = estadoActual.aceptaTerminos
                )
                repository.insertar(nuevoUsuario)
            }
        }
    }

    fun actualizarFotoPerfil(nuevoUri : Uri?){
        val usuarioId =_estado.value.id
        if (usuarioId==0){
            println("Error no ahi usuario para actualizar la foto")
            return
        }
        val uriString= nuevoUri?.toString()
        viewModelScope.launch { repository. actualizarFoto(usuarioId,uriString)

        _estado.update { it.copy(fotopefil = uriString) }}
        println("Foto de perfil actualizada")
    }

}