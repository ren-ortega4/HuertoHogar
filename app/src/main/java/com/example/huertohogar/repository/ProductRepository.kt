package com.example.huertohogar.repository

import com.example.huertohogar.model.Product
import com.example.huertohogar.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object ProductRepository {
    private val products = listOf(
        Product(id = 1, name = "Leche Natural", price = "$3.800", imagesRes = R.drawable.destacado2),
        Product(id = 2, name = "Miel Orgánica", price = "$5.000", imagesRes = R.drawable.destacado1),
        Product(id = 3, name = "Platános Cavendish", price = "$800/Kg", imagesRes = R.drawable.destacado3)
    )

    fun getProductsFlow(): Flow<List<Product>>{
        return flowOf(products)
    }
}