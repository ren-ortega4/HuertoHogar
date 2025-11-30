package com.example.huertohogar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val priceLabel: String,
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