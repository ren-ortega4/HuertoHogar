package com.example.huertohogar.model

data class CartRequest (
    val items: List<Item>
)
data class Item(
    val title: String,
    val quantity: Int,
    val unitPrice: Double
)