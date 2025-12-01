package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class CartItemTest {

    @Test
    fun subtotalCalculadoCorrectamente() {
        val product = Product(name = "Manzana", price = 100.0, priceLabel = "$100", imagesRes = 0, category = ProductCategory.frutas)
        val cartItem = CartItem(product = product, quantity = 2)

        assertEquals(200.0, cartItem.subtotal, 0.0001)
    }

    @Test
    fun subtotalConMilesYUnidad() {
        val product = Product(name = "Papa", price = 1000.0, priceLabel = "$1.000/kg", imagesRes = 0, category = ProductCategory.verduras)
        val cartItem = CartItem(product = product, quantity = 3)

        assertEquals(3000.0, cartItem.subtotal, 0.0001)
    }

    @Test
    fun subtotalCuandoPrecioInvalidoDevuelveCero() {
        val product = Product(name = "X", price = 0.0, priceLabel = "precio desconocido", imagesRes = 0, category = ProductCategory.otros)
        val cartItem = CartItem(product = product, quantity = 5)

        assertEquals(0.0, cartItem.subtotal, 0.0001)
    }

    @Test
    fun subtotalConCantidadCeroEsCero() {
        val product = Product(name = "Z", price = 10.0, priceLabel = "$10", imagesRes = 0, category = ProductCategory.otros)
        val cartItem = CartItem(product = product, quantity = 0)

        assertEquals(0.0, cartItem.subtotal, 0.0001)
    }
}