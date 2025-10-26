package com.example.huertohogar.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserViewModel(
    private val repository: UsuarioRepository, // Tu repositorio de Room
) : ViewModel() {
    private val _estado = MutableStateFlow(UserUiState())
    val estado : StateFlow<UserUiState> = _estado

    companion object {
        private const val TAG = "UserViewModel"
    }

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
        if (!validarLogin()) return

        viewModelScope.launch {
            Log.d(TAG, "Intentando login con: ${_estado.value.loginCorreo}")
            val usuarioEncontrado = repository.login(_estado.value.loginCorreo, _estado.value.loginClave)

            if (usuarioEncontrado != null) {
                Log.d(TAG, "Usuario encontrado: $usuarioEncontrado")
                _estado.update {
                    it.copy(
                        isLoggedIn = true,
                        currentUser = usuarioEncontrado, //Almacena usuario Completo
                        loginCorreo = "",
                        loginClave = "",
                        id = usuarioEncontrado.id,
                        nombre = usuarioEncontrado.nombre,
                        correo = usuarioEncontrado.correo,
                        direccion = usuarioEncontrado.direccion,
                        region = usuarioEncontrado.region,
                        fotopefil = usuarioEncontrado.fotopefil,
                        errores = UserError()
                    )
                }
                Log.d(TAG, "Estado después de update: ${_estado.value}")
            } else {
                Log.d(TAG, "Login falló: usuario no encontrado")
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(errorLoginGeneral = "Correo o clave incorrectos")
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
        if (validarFormularioRegistro()) {
            viewModelScope.launch {
                val nuevoUser= User(
                    nombre = _estado.value.nombre,
                    correo = _estado.value.correo,
                    clave = _estado.value.clave,
                    confirmarClave = _estado.value.confirmarClave,
                    direccion = _estado.value.direccion,
                    region = _estado.value.region,
                    aceptaTerminos = _estado.value.aceptaTerminos
                )
                repository.insertar(nuevoUser)
                limpiarFormulario()
            }
        }
    }

    fun actualizarFotoPerfil(nuevoUri : Uri?){
        val usuarioActual = _estado.value.currentUser
        if (usuarioActual == null){
            Log.d(TAG, "No hay un usuario en sesión para actualizar la foto")
            println("No hay un usuario en sesión para actualizar la foto")
            return
        }
        val uriString= nuevoUri?.toString()
        viewModelScope.launch { repository. actualizarFoto(usuarioActual.id,uriString)
            _estado.update { it.copy(currentUser = usuarioActual.copy(fotopefil = uriString)) }}
            Log.d(TAG, "Foto de perfil actualizada, nuevo estado usuario: ${_estado.value.currentUser}")
        println("Foto de perfil actualizada")
    }


}