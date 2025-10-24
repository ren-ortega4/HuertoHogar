package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huertohogar.data.AppPreference
import com.example.huertohogar.model.UserError
import com.example.huertohogar.model.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class UserViewModel(private val appPreference: AppPreference) : ViewModel(){
    private val _estado = MutableStateFlow(UserUiState())
    val estado : StateFlow<UserUiState> = _estado

    init {
        // al iniciar el viewmodel, lee el valor gaurdado y actualiza el estado
        val rememberUser = appPreference.getRememberUser()
        _estado.update { it.copy(recordarUsuario = rememberUser) }
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

    fun onNombreChange (valor : String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange (valor : String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange (valor : String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onConfirmarClaveChange (valor : String) {
        _estado.update { it.copy(confirmarClave = valor, errores = it.errores.copy(confirmarClave = null)) }
    }
    fun onDireccionChange (valor : String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onRegionChange(valor:String){
        _estado.update { it.copy(region = valor, errores = it.errores.copy(region = null)) }
    }

    fun onRecordarUsuarioChange(valor:Boolean) {
        // Actualiza el estado y guarda la preferencia
        _estado.update { it.copy(recordarUsuario = valor) }
        appPreference.saveRememberUser(valor)
    }


    fun onAceptarTerminosChange(valor : Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun limpiarFormulario() {
        _estado.value = UserUiState(recordarUsuario = _estado.value.recordarUsuario)
    }



    fun Validarformulario(): Boolean{
        val estadoActual=_estado.value
        val errores = UserError(
            nombre = if (estadoActual.nombre.isBlank())"El nombre es requerido" else null,
            correo = if (estadoActual.correo.isBlank())"El correo es requerido" else null,
            clave = if (estadoActual.clave.isBlank())"La clave es requerida" else null,
            confirmarClave = if (estadoActual.confirmarClave.isBlank()) "Confirme la clave" else if (estadoActual.clave != estadoActual.confirmarClave) "Las claves no coinciden" else null,
            direccion = if (estadoActual.direccion.isBlank())"La direccion es requerida" else null,
            region = if(estadoActual.region.isBlank())"la region es requerida" else null

        )
        val hayErrores= listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.confirmarClave,
            errores.direccion,
            errores.region
        ).isNotEmpty()
        _estado.update { it.copy(errores=errores) }
        return !hayErrores
    }

    fun validarLogin(): Boolean {
        val estadoActual = _estado.value
        val errores = UserError(
            correo = if (estadoActual.correo.isBlank()) "El correo es requerido" else null,
            clave = if (estadoActual.clave.isBlank()) "La clave es requerida" else null
        )
        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

}
