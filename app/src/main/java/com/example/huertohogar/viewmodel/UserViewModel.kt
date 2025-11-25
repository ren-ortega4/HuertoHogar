package com.example.huertohogar.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.model.RolRequest
import com.example.huertohogar.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- 1. UNA ÚNICA CLASE PARA TODO EL ESTADO DE LA PANTALLA ---
data class UserScreenUiState(
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

    // Estado General de la UI
    val isLoading: Boolean = false,
    val errores: UserError = UserError(),

    // Estado de la Sesión (vendrá del Flow de la BD)
    val usuarioLogueado: User? = null
)

class UserViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {
    // --- CORRECTA UBICACIÓN: función dentro del cuerpo de la clase ---
    fun loginWithScope() {
        viewModelScope.launch {
            login()
        }
    }

    // Estado privado SOLO para los campos que el usuario modifica en los formularios.
    private val _formState = MutableStateFlow(UserScreenUiState())

    // --- 2. UN ÚNICO STATEFLOW PÚBLICO PARA LA UI ---
    val uiState: StateFlow<UserScreenUiState> = combine(
        _formState,
        repository.activeUser.map { userEntity -> 
            userEntity?.let { entity ->
                User(
                    id = entity.id,
                    nombre = entity.nombre,
                    apellido = entity.apellido,
                    correo = entity.correo,
                    region = entity.region,
                    contrasena = "",
                    fecha_registro = entity.fecha_registro,
                    estado = entity.estado,
                    rol = null,
                    fotopefil = entity.fotopefil
                )
            }
        } // <-- Observamos la base de datos
    ) { formState, activeUser ->
        // Fusiona el estado del formulario con el usuario de la sesión
        formState.copy(usuarioLogueado = activeUser)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserScreenUiState() // Estado inicial vacío
    )

    companion object {
        private const val TAG = "UserViewModel"
    }

    val regiones = listOf(
        "Arica y Parinacota", "Tarapacá", "Antofagasta", "Atacama", "Coquimbo",
        "Valparaíso", "Metropolitana de Santiago", "Libertador General Bernardo O'Higgins",
        "Maule", "Ñuble", "Biobío", "La Araucanía", "Los Ríos", "Los Lagos",
        "Aysén del General Carlos Ibáñez del Campo", "Magallanes y de la Antártica Chilena"
    )

    //<editor-fold desc="Funciones para actualizar el estado del formulario">
    fun onNombreChange(valor: String) { _formState.update { it.copy(nombre = valor) } }
    fun onApellidoChange(valor: String) { _formState.update { it.copy(apellido = valor) } }
    fun onCorreoChange(valor: String) { _formState.update { it.copy(correo = valor) } }
    fun onClaveChange(valor: String) { _formState.update { it.copy(clave = valor) } }
    fun onConfirmarClaveChange(valor: String) { _formState.update { it.copy(confirmarClave = valor) } }
    fun onRegionChange(valor: String) { _formState.update { it.copy(region = valor) } }
    fun onAceptarTerminosChange(valor: Boolean) { _formState.update { it.copy(aceptaTerminos = valor) } }
    fun onLoginCorreoChange(valor: String) { _formState.update { it.copy(loginCorreo = valor) } }
    fun onLoginClaveChange(valor: String) { _formState.update { it.copy(loginClave = valor) } }
    //</editor-fold>

    suspend fun login(): Boolean {
        if (!validarLogin()) return false
        _formState.update { it.copy(isLoading = true) }
        val success = repository.login(uiState.value.loginCorreo, uiState.value.loginClave)
        _formState.update { it.copy(isLoading = false) }

        if (!success) {
            _formState.update { it.copy(errores = it.errores.copy(errorLoginClave = "Correo o clave incorrectos")) }
        }
        return success
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    suspend fun registrarUsuario(): Boolean {
        if (!validarFormularioRegistro()) return false
        _formState.update { it.copy(isLoading = true) }

        // CORREGIDO: El modelo User no tiene 'confirmarcontrasena'
        val userParaRegistro = User(
            id = 0,
            nombre = uiState.value.nombre,
            apellido = uiState.value.apellido,
            correo = uiState.value.correo,
            contrasena = uiState.value.clave,
            fecha_registro = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            estado = true,
            region = uiState.value.region,
            rol = RolRequest(id_rol = 2)
        )

        val success = repository.register(userParaRegistro)
        _formState.update { it.copy(isLoading = false) }

        if (success) {
            limpiarFormulario()
        }
        return success
    }

    fun limpiarFormulario() {
        // Resetea solo los campos del formulario, el estado de la sesión se mantiene.
        _formState.update {
            UserScreenUiState(usuarioLogueado = it.usuarioLogueado)
        }
    }

    private fun validarLogin(): Boolean {
        // Tu lógica de validación usando 'uiState.value.loginCorreo', etc.
        val estadoActual = uiState.value
        val erroresNuevos = estadoActual.errores.copy(
            errorLoginCorreo = if (estadoActual.loginCorreo.isBlank()) "El correo es requerido" else if (!estadoActual.loginCorreo.contains("@")) "El correo debe tener '@'" else null,
            errorLoginClave = if (estadoActual.loginClave.isBlank()) "La clave es requerida" else if (estadoActual.loginClave.length < 8) "Debe tener al menos 8 caracteres" else null
        )
        val hayErrores = listOfNotNull(
            erroresNuevos.errorLoginCorreo, erroresNuevos.errorLoginClave
        ).isNotEmpty()
        _formState.update { it.copy(errores = erroresNuevos) }
        return !hayErrores
    }

    fun validarFormularioRegistro(): Boolean {
        val estadoActual = uiState.value
        // CORREGIDO: El data class UserError no tiene 'contrasena', usa 'clave'
        val erroresNuevos = UserError(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es requerido" else null,
            apellido = if (estadoActual.apellido.isBlank()) "El apellido es requerido" else null,
            correo = if (estadoActual.correo.isBlank()) "Correo es requerido" else if (!estadoActual.correo.contains("@")) "El correo debe tener '@'" else null,
            region = if (estadoActual.region.isBlank()) "La región es requerida" else null,
            contrasena = if (estadoActual.clave.isBlank()) "La clave es requerida" else if (estadoActual.clave.length < 8) "La clave debe tener al menos 8 caracteres" else null,
            confirmarContrasena = if (estadoActual.confirmarClave.isBlank()) "Debe repetir la clave" else if (estadoActual.clave != estadoActual.confirmarClave) "Las claves no coinciden" else null
        )
        val hayErrores = listOfNotNull(
            erroresNuevos.nombre, erroresNuevos.apellido, erroresNuevos.correo,
            erroresNuevos.region, erroresNuevos.contrasena, erroresNuevos.confirmarContrasena
        ).isNotEmpty()
        _formState.update { it.copy(errores = erroresNuevos) }
        return !hayErrores
    }

    fun actualizarFotoPerfil(nuevoUri: Uri?) {
        // CORREGIDO: Obtenemos el usuario de la sesión desde el estado unificado 'uiState'.
        val usuarioActual = uiState.value.usuarioLogueado
        if (usuarioActual == null) {
            Log.d(TAG, "No hay un usuario en sesión para actualizar la foto")
            return
        }

        val uriString = nuevoUri?.toString()
        viewModelScope.launch {
            // Lógica para actualizar la foto:
            // 1. Subir la imagen a un servidor y obtener la nueva URL (si es necesario).
            // 2. Crear un objeto User actualizado.
            val usuarioActualizado = usuarioActual.copy(fotopefil = uriString)
            // 3. Llamar al repositorio para que actualice la API y la base de datos local.
            // repository.updateUser(usuarioActualizado) // Suponiendo que el repo tiene un método update
            Log.d(TAG, "Lógica de actualización de foto pendiente de implementar en el repositorio.")
        }
    }
}
