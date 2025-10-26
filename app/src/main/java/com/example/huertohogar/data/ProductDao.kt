package com.example.huertohogar.data

import androidx.room.*
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: ProductCategory): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
    
    @Query("SELECT DISTINCT category FROM products ORDER BY category")
    fun getAllCategories(): Flow<List<ProductCategory>>
}
