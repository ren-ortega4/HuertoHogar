package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.model.CartItem
import com.example.huertohogar.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _showSuccessBanner = MutableStateFlow(false)
    val showSuccessBanner: StateFlow<Boolean> = _showSuccessBanner.asStateFlow()


    init {
        updateTotals()
    }

    fun onPurchaseSuccess() {
        viewModelScope.launch {
            clearCart()
            _showSuccessBanner.value = true
            delay(3000)
            _showSuccessBanner.value = false
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.product.id == product.id }

        if (existingItem != null) {
            // Si el producto ya existe, actualizar la cantidad
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            // Si no existe, agregar nuevo item
            currentItems.add(CartItem(product, quantity))
        }

        _cartItems.value = currentItems
        updateTotals()
    }
    fun removeFromCart(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
        updateTotals()
    }
    fun updateQuantity(productId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == productId }

        if (index != -1) {
            currentItems[index] = currentItems[index].copy(quantity = newQuantity)
            _cartItems.value = currentItems
            updateTotals()
        }
    }
    fun clearCart() {
        _cartItems.value = emptyList()
        updateTotals()
    }
    private fun updateTotals() {
        val items = _cartItems.value
        _totalItems.value = items.sumOf { it.quantity }
        _totalPrice.value = items.sumOf { it.subtotal }
    }
}