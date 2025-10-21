package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huertohogar.model.Notificacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificacionesViewModel : ViewModel(){
    private val _notificaciones = MutableStateFlow(
        listOf(
            Notificacion(1, "Bienvenido a HuertoHogar", false),
            Notificacion(2, "Nueva promoción disponible.", false),
            Notificacion(3, "Tu pedido fue enviado.", true)
        )
    )

    val notificaciones : StateFlow<List<Notificacion>> = _notificaciones

    fun marcarComoLeida(id: Int){
        _notificaciones.value = _notificaciones.value.map {
            if (it.id == id) it.copy(leido = true) else it
        }
    }
}