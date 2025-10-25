package com.example.huertohogar.model

data class Product (
    val id: Int,
    val name: String,
    val price: String,
    val imagesRes: Int,
    val category: ProductCategory
)

enum class ProductCategory(val displayName: String) {
    frutas("Frutas"),
    verduras("Verduras"),
    productosOrganicos("Productos Orgánicos"),
    lacteos("Lácteos"),
    otros("Otros")
}