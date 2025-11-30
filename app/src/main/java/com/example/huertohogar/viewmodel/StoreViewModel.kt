package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.huertohogar.data.local.TiendaDao
import com.example.huertohogar.model.Tienda

class StoreViewModel(private val dao: TiendaDao) :  ViewModel() {

    private val _stores = MutableStateFlow<List<Tienda>>(emptyList())
    val stores: StateFlow<List<Tienda>> get() = _stores

    init {
        fetchStores()
    }

    private fun fetchStores() {
        viewModelScope.launch {
            _stores.value = dao.getAllTiendas()
        }
    }

    fun addStore(store: Tienda) {
        viewModelScope.launch {
            // La función insertAll espera una lista, no un solo objeto.
            dao.insertAll(listOf(store))
            fetchStores()
        }
    }

}