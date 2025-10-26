package com.example.huertohogar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.Tip
import com.example.huertohogar.data.TipDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TipViewModel(application: Application) : AndroidViewModel(application) {

    private val tipDao = TipDatabase.getDatabase(application).tipDao()

    private val _currentTip = MutableStateFlow(Tip(iconName = "Info", title = "HuertoHogar", text = "Cargando tip..."))
    val currentTip: StateFlow<Tip> = _currentTip.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val tips = tipDao.getAllTips().first()
                if (tips.isEmpty()){
                    tipDao.insertAll(
                        listOf(
                            Tip(
                                iconName = "LocalOffer",
                                title = "Ofertas Especiales",
                                text = "Descubre descuentos únicos para tí."),
                            Tip(
                                iconName = "ThumbUp",
                                title = "Consejos",
                                text = "Los mejores consejos para crear un huerto en tu hogar."),
                            Tip(
                                iconName = "Storefront",
                                title = "Tienda",
                                text = "Productos frescos y orgánicos a tu alcance."),
                            Tip(
                                iconName = "Call",
                                title = "Comunidad",
                                text = "Tu opinión nos importa. ¡Contactános!")
                        )
                    )
                }
            }
            startTipRotation()
        }
    }

    companion object{
        const val TIP_ROTATION_DELAY_MS = 5000L
    }

    private fun startTipRotation(){
        viewModelScope.launch {
            val allTips = tipDao.getAllTips().first()
            if (allTips.isNotEmpty()){
                while (true){
                    _currentTip.value = allTips.random()
                    delay(TIP_ROTATION_DELAY_MS)
                }
            } else {
                _currentTip.value = Tip(iconName = "Error", title = "Error", text = "No hay tips disponibles")
            }
        }
    }

}