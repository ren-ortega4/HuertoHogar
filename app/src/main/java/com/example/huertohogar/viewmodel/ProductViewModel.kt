package com.example.huertohogar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.ProductDatabase
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ProductRepository
    
    val allProducts: StateFlow<List<Product>>
    val allCategories: StateFlow<List<ProductCategory>>
    
    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()
    
    private val _productsByCategory = MutableStateFlow<List<Product>>(emptyList())
    val productsByCategory: StateFlow<List<Product>> = _productsByCategory.asStateFlow()
    
    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        
        allProducts = MutableStateFlow<List<Product>>(emptyList()).apply {
            viewModelScope.launch {
                repository.getAllProducts().collect { products ->
                    value = products
                }
            }
        }
        
        allCategories = MutableStateFlow<List<ProductCategory>>(emptyList()).apply {
            viewModelScope.launch {
                repository.getAllCategories().collect { categories ->
                    value = categories
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
}
