package com.example.huertohogar.repository

import com.example.huertohogar.data.ProductDao
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import kotlinx.coroutines.flow.Flow

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
}