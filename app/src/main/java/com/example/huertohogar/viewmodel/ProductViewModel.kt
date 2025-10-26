package com.example.huertohogar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.local.AppDatabase
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.data.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ProductRepository

    val allProducts: StateFlow<List<Product>>

    val allCategories: StateFlow<List<ProductCategory>>
    
    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()
    
    private val _productsByCategory = MutableStateFlow<List<Product>>(emptyList())
    val productsByCategory: StateFlow<List<Product>> = _productsByCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)

         allProducts = _searchQuery
            .flatMapLatest { query ->
                if (query.isBlank()){
                    repository.getAllProducts()
                } else {
                    repository.searchProducts(query)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


        // Esta inicialización también puede mejorarse
        allCategories = repository.getAllCategories().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Añadimos la lógica para seleccionar la primera categoría por defecto
        viewModelScope.launch {
            allCategories.collect { categories ->
                if (categories.isNotEmpty() && _selectedCategory.value == null) {
                    setSelectedCategory(categories.first())
                }
            }
        }
    }

    fun setSelectedCategory(category: ProductCategory) {
        _selectedCategory.value = category
        viewModelScope.launch {
            repository.getProductsByCategory(category).collect { products ->
                _productsByCategory.value = products
            }
        }
    }
    
    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }
    
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }
    
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
    
    suspend fun getProductById(productId: Int): Product? {
        return repository.getProductById(productId)
    }

    // Actualizar busqueda
    fun onSearchQueryChange(newQuery: String){
        _searchQuery.value = newQuery
    }
}
