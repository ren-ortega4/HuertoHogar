package com.example.huertohogar.repository

import com.example.huertohogar.data.ProductDao
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductRepository(private val productDao: ProductDao) {
    
    // Obtener todos los productos
    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }
    
    // Obtener productos por categoría
    fun getProductsByCategory(category: ProductCategory): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }
    
    // Obtener todas las categorías
    fun getAllCategories(): Flow<List<ProductCategory>> {
        return productDao.getAllCategories()
    }
    
    // Obtener producto por ID
    suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)
    }
    
    // Insertar producto
    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }
    
    // Insertar múltiples productos
    suspend fun insertProducts(products: List<Product>) {
        productDao.insertProducts(products)
    }
    
    // Actualizar producto
    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }
    
    // Eliminar producto
    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }
    
    // Eliminar todos los productos
    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }

    companion object {
        fun getProductsFlow(): Flow<List<Product>> {
            // Lista de productos destacados para la pantalla principal
            val products = listOf(
                Product(id = 1, name = "Leche Natural", price = "$3.800", imagesRes = com.example.huertohogar.R.drawable.destacado2, category = ProductCategory.lacteos),
                Product(id = 2, name = "Miel Orgánica", price = "$5.000", imagesRes = com.example.huertohogar.R.drawable.destacado1, category = ProductCategory.productosOrganicos),
                Product(id = 3, name = "Platános Cavendish", price = "$800/Kg", imagesRes = com.example.huertohogar.R.drawable.destacado3, category = ProductCategory.frutas)
            )
            return flowOf(products)
        }
    }
}