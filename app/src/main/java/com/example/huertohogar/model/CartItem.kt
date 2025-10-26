package com.example.huertohogar.model

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val subtotal: Double
        get() {
            val priceString = product.price
                .replace("$", "")
                .replace(".", "")
                .split("/")[0] // Por si tiene /Kg o /bolsa
                .trim()
            return try {
                priceString.toDouble() * quantity
            } catch (e: NumberFormatException) {
                0.0
            }
        }
}
