package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.huertohogar.data.local.TiendaDao

class StoreViewModelFactory(private val tiendaDao: TiendaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoreViewModel(tiendaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}