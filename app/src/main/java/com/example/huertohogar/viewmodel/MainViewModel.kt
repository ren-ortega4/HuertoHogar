package com.example.huertohogar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.ProductDatabase
import com.example.huertohogar.model.Product
import com.example.huertohogar.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.huertohogar.R

data class MainScreenUiState(
    val featuredProducts: List<Product> = emptyList(),
    val categories: List<Pair<String, Int>> = emptyList(),
    val isLoading: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState
    
    private val repository: ProductRepository

    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        loadData()
    }

    private fun loadData(){
        viewModelScope.launch {
            repository.getAllProducts().collect { products ->
                val categories = listOf(
                    "Verduras" to R.drawable.verdura,
                    "Orgánicos" to R.drawable.organico,
                    "Frutas" to R.drawable.fruta,
                    "Lácteos" to R.drawable.lacteos
                )

                _uiState.value = MainScreenUiState(
                    featuredProducts = products,
                    categories = categories,
                    isLoading = false
                )
            }
        }
    }

}