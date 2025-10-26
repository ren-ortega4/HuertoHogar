package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huertohogar.model.CartItem
import com.example.huertohogar.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    val totalItems: StateFlow<Int> = MutableStateFlow(0)
    val totalPrice: StateFlow<Double> = MutableStateFlow(0.0)

    init {
        updateTotals()
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
        (totalItems as MutableStateFlow).value = items.sumOf { it.quantity }
        (totalPrice as MutableStateFlow).value = items.sumOf { it.subtotal }
    }
}
