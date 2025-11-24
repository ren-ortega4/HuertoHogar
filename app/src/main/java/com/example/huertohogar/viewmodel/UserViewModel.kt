package com.example.huertohogar.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.remote.ApiCliente
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.model.RolRequest
import com.example.huertohogar.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserViewModel(
    // La inyección del repositorio es una buena práctica, pero asegúrate
    // de tener un Factory para que el ViewModel pueda recibirlo.
    private val repository: UsuarioRepository,
) : ViewModel() {
    private val _estado = MutableStateFlow(UserUiState())
    val estado: StateFlow<UserUiState> = _estado

    companion object {
        private const val TAG = "UserViewModel"
    }

    val regiones = listOf(
        "Arica y Parinacota", "Tarapacá", "Antofagasta", "Atacama", "Coquimbo",
        "Valparaíso", "Metropolitana de Santiago", "Libertador General Bernardo O'Higgins",
        "Maule", "Ñuble", "Biobío", "La Araucanía", "Los Ríos", "Los Lagos",
        "Aysén del General Carlos Ibáñez del Campo", "Magallanes y de la Antártica Chilena"
    )

    //<editor-fold desc="Funciones para el formulario de Registro">
    fun onNombreChange(valor: String) { _estado.update { it.copy(nombre = valor) } }
    fun onApellidoChange(valor: String) { _estado.update { it.copy(apellido = valor) } }
    fun onCorreoChange(valor: String) { _estado.update { it.copy(correo = valor) } }
    // CORREGIDO: Usar 'clave' en lugar de 'contrasena'
    fun onClaveChange(valor: String) { _estado.update { it.copy(clave = valor) } }
    // CORREGIDO: Usar 'confirmarClave' en lugar de 'confirmarContrasena'
    fun onConfirmarClaveChange(valor: String) { _estado.update { it.copy(confirmarClave = valor) } }
    fun onRegionChange(valor: String) { _estado.update { it.copy(region = valor) } }
    fun onAceptarTerminosChange(valor: Boolean) { _estado.update { it.copy(aceptaTerminos = valor) } }
    //</editor-fold>

    //<editor-fold desc="Funciones para el formulario de Login">
    fun onLoginCorreoChange(valor: String) { _estado.update { it.copy(loginCorreo = valor) } }
    fun onLoginClaveChange(valor: String) { _estado.update { it.copy(loginClave = valor) } }
    //</editor-fold>


    suspend fun login(): Boolean {
        if (!validarLogin()) {
            return false
        }
        Log.d(TAG, "Intentando login con: ${_estado.value.loginCorreo}")

        // Asumiendo que tu repository.login devuelve un token (String?) como en versiones anteriores.
        val token = repository.login(_estado.value.loginCorreo, _estado.value.loginClave)

        return if (token != null) {
            Log.d(TAG, "Login exitoso, token obtenido: $token")
            // Aquí deberías obtener los datos del usuario con otro llamado a la API usando el token
            // y guardarlo en `currentUser`. Por ahora, solo guardamos el token.
            _estado.update {
                it.copy(
                    isLoggedIn = true,
                    authToken = token, // Guardamos el token en el estado
                    loginCorreo = "", // Limpiamos campos de login
                    loginClave = "",
                    errores = UserError() // Limpiamos errores
                )
            }
            true
        } else {
            Log.d(TAG, "Login falló: usuario no encontrado o clave incorrecta")
            _estado.update {
                it.copy(errores = it.errores.copy(errorLoginClave = "Correo o clave incorrectos"))
            }
            false
        }
    }

    fun logout() {
        _estado.value = UserUiState(recordarUsuario = _estado.value.recordarUsuario)
    }

    fun limpiarFormulario() {
        _estado.update {
            it.copy(
                nombre = "",
                apellido = "",
                correo = "",
                clave = "", // CORREGIDO
                confirmarClave = "", // CORREGIDO
                region = "",
                aceptaTerminos = false,
                loginCorreo = "",
                loginClave = "",
                errores = UserError()
            )
        }
    }

    fun validarFormularioRegistro(): Boolean {
        val estadoActual = _estado.value
        val erroresNuevos = UserError(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es requerido" else null,
            apellido = if (estadoActual.apellido.isBlank()) "El apellido es requerido" else null,
            correo = if (estadoActual.correo.isBlank()) "Correo es requerido" else if (!estadoActual.correo.contains("@")) "El correo debe tener '@'" else null,
            region = if (estadoActual.region.isBlank()) "La región es requerida" else null,
            // CORREGIDO: Usar 'clave' y 'confirmarClave'
            contrasena = if (estadoActual.clave.isBlank()) "La clave es requerida" else if (estadoActual.clave.length < 8) "La clave debe tener al menos 8 caracteres" else null,
            confirmarContrasena = if (estadoActual.confirmarClave.isBlank()) "Debe repetir la clave" else if (estadoActual.clave != estadoActual.confirmarClave) "Las claves no coinciden" else null
        )
        _estado.update { it.copy(errores = erroresNuevos) }

        val hayErrores = listOfNotNull(
            erroresNuevos.nombre, erroresNuevos.apellido, erroresNuevos.correo,
            erroresNuevos.region, erroresNuevos.contrasena, erroresNuevos.confirmarContrasena
        ).isNotEmpty()

        return !hayErrores
    }

    fun validarLogin(): Boolean {
        val estadoActual = _estado.value
        val erroresNuevos = estadoActual.errores.copy(
            errorLoginCorreo = if (estadoActual.loginCorreo.isBlank()) "El correo es requerido" else if (!estadoActual.loginCorreo.contains("@")) "El correo debe tener '@'" else null,
            errorLoginClave = if (estadoActual.loginClave.isBlank()) "La clave es requerida" else if (estadoActual.loginClave.length < 8) "Debe tener al menos 8 caracteres" else null
        )
        _estado.update { it.copy(errores = erroresNuevos) }

        val hayErrores = listOfNotNull(
            erroresNuevos.errorLoginCorreo, erroresNuevos.errorLoginClave
        ).isNotEmpty()

        return !hayErrores
    }

    suspend fun registrarUsuario(): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = sdf.format(Date())

        val user = User(
            nombre = _estado.value.nombre,
            apellido = _estado.value.apellido,
            correo = _estado.value.correo,
            region = _estado.value.region,
            contrasena = _estado.value.clave, // CORREGIDO: Envía 'clave' del estado como 'contrasena' al modelo User
            fecha_registro = fechaActual,
            estado = true,
            rol = RolRequest(id_rol = 2)
        )

        return try {
            val response = ApiCliente.instance.registarUsusario(user)
            if (response.isSuccessful) {
                Log.d(TAG, "Registro exitoso")
                true
            } else {
                Log.e(TAG, "Error al registrar usuario. Código: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al registrar usuario", e)
            false
        }
    }

    fun actualizarFotoPerfil(nuevoUri: Uri?) {
        val usuarioActual = _estado.value.currentUser
        if (usuarioActual == null) {
            Log.d(TAG, "No hay un usuario en sesión para actualizar la foto")
            return
        }
        val uriString = nuevoUri?.toString()
        viewModelScope.launch {
            // repository.actualizarFoto(usuarioActual.id, uriString) // Asegúrate de que esta función exista
            _estado.update { it.copy(currentUser = usuarioActual.copy(fotopefil = uriString)) }
        }
        Log.d(TAG, "Foto de perfil actualizada, nuevo estado usuario: ${_estado.value.currentUser}")
    }
}
