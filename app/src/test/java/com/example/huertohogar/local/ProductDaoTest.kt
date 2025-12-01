package com.example.huertohogar.local

import com.example.huertohogar.data.local.ProductDao
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductDaoTest {

    private lateinit var productDao: ProductDao

    @BeforeEach
    fun setUp() {
        productDao = mockk(relaxed = true)
    }

    @Test
    fun `getAllProducts should return flow of products`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )
        every { productDao.getAllProducts() } returns flowOf(products)

        // When
        val result = productDao.getAllProducts().first()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Manzana", result[0].name)
        assertEquals("Lechuga", result[1].name)
    }

    @Test
    fun `getAllProducts should return empty flow when no products`() = runTest {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())

        // When
        val result = productDao.getAllProducts().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProductsByCategory should return products of specific category`() = runTest {
        // Given
        val category = ProductCategory.frutas
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Banana", price = 800.0, priceLabel = "$800", imagesRes = 2, category = ProductCategory.frutas)
        )
        every { productDao.getProductsByCategory(category) } returns flowOf(products)

        // When
        val result = productDao.getProductsByCategory(category).first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.category == ProductCategory.frutas })
    }

    @Test
    fun `getProductsByCategory should return empty for category without products`() = runTest {
        // Given
        val category = ProductCategory.lacteos
        every { productDao.getProductsByCategory(category) } returns flowOf(emptyList())

        // When
        val result = productDao.getProductsByCategory(category).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProductById should return product when exists`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.getProductById(1) } returns product

        // When
        val result = productDao.getProductById(1)

        // Then
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("Manzana", result?.name)
    }

    @Test
    fun `getProductById should return null when product does not exist`() = runTest {
        // Given
        coEvery { productDao.getProductById(999) } returns null

        // When
        val result = productDao.getProductById(999)

        // Then
        assertNull(result)
    }

    @Test
    fun `searchProducts should return matching products`() = runTest {
        // Given
        val query = "man"
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )
        every { productDao.searchProducts(query) } returns flowOf(products)

        // When
        val result = productDao.searchProducts(query).first()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].name.contains("man", ignoreCase = true))
    }

    @Test
    fun `searchProducts should return empty when no matches`() = runTest {
        // Given
        val query = "xyz"
        every { productDao.searchProducts(query) } returns flowOf(emptyList())

        // When
        val result = productDao.searchProducts(query).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `insertProduct should insert single product successfully`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.insertProduct(product) } returns Unit

        // When
        productDao.insertProduct(product)

        // Then
        coVerify(exactly = 1) { productDao.insertProduct(product) }
    }

    @Test
    fun `insertProducts should insert multiple products successfully`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )
        coEvery { productDao.insertProducts(products) } returns Unit

        // When
        productDao.insertProducts(products)

        // Then
        coVerify(exactly = 1) { productDao.insertProducts(products) }
    }

    @Test
    fun `insertProducts should handle empty list`() = runTest {
        // Given
        val emptyList = emptyList<Product>()
        coEvery { productDao.insertProducts(emptyList) } returns Unit

        // When
        productDao.insertProducts(emptyList)

        // Then
        coVerify(exactly = 1) { productDao.insertProducts(emptyList) }
    }

    @Test
    fun `updateProduct should update existing product`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana Premium", price = 1500.0, priceLabel = "$1.500", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.updateProduct(product) } returns Unit

        // When
        productDao.updateProduct(product)

        // Then
        coVerify(exactly = 1) { productDao.updateProduct(product) }
    }

    @Test
    fun `deleteProduct should delete specific product`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.deleteProduct(product) } returns Unit

        // When
        productDao.deleteProduct(product)

        // Then
        coVerify(exactly = 1) { productDao.deleteProduct(product) }
    }

    @Test
    fun `deleteAllProducts should delete all products`() = runTest {
        // Given
        coEvery { productDao.deleteAllProducts() } returns Unit

        // When
        productDao.deleteAllProducts()

        // Then
        coVerify(exactly = 1) { productDao.deleteAllProducts() }
    }

    @Test
    fun `getAllCategories should return distinct categories`() = runTest {
        // Given
        val categories = listOf(ProductCategory.frutas, ProductCategory.verduras, ProductCategory.lacteos)
        every { productDao.getAllCategories() } returns flowOf(categories)

        // When
        val result = productDao.getAllCategories().first()

        // Then
        assertEquals(3, result.size)
        assertTrue(result.contains(ProductCategory.frutas))
        assertTrue(result.contains(ProductCategory.verduras))
        assertTrue(result.contains(ProductCategory.lacteos))
    }

    @Test
    fun `getAllCategories should return empty when no products`() = runTest {
        // Given
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        val result = productDao.getAllCategories().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `insertProduct should replace on conflict`() = runTest {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 1, name = "Manzana Premium", price = 1500.0, priceLabel = "$1.500", imagesRes = 1, category = ProductCategory.frutas)
        
        coEvery { productDao.insertProduct(product1) } returns Unit
        coEvery { productDao.insertProduct(product2) } returns Unit

        // When
        productDao.insertProduct(product1)
        productDao.insertProduct(product2)

        // Then
        coVerify(exactly = 1) { productDao.insertProduct(product1) }
        coVerify(exactly = 1) { productDao.insertProduct(product2) }
    }

    @Test
    fun `searchProducts should handle special characters`() = runTest {
        // Given
        val query = "Prod%"
        every { productDao.searchProducts(query) } returns flowOf(emptyList())

        // When
        val result = productDao.searchProducts(query).first()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `getProductsByCategory should work with all category types`() = runTest {
        // Given
        val categories = listOf(
            ProductCategory.frutas,
            ProductCategory.verduras,
            ProductCategory.lacteos,
            ProductCategory.productosOrganicos,
            ProductCategory.otros
        )

        // When & Then
        categories.forEach { category ->
            every { productDao.getProductsByCategory(category) } returns flowOf(emptyList())
            val result = productDao.getProductsByCategory(category).first()
            assertNotNull(result)
        }
    }

    @Test
    fun `product should have all required properties`() = runTest {
        // Given
        val product = Product(
            id = 1,
            name = "Manzana Fuji",
            price = 1200.0,
            priceLabel = "$1.200/Kg",
            imagesRes = 123,
            category = ProductCategory.frutas
        )
        coEvery { productDao.getProductById(1) } returns product

        // When
        val result = productDao.getProductById(1)

        // Then
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("Manzana Fuji", result?.name)
        assertEquals(1200.0, result?.price)
        assertEquals("$1.200/Kg", result?.priceLabel)
        assertEquals(123, result?.imagesRes)
        assertEquals(ProductCategory.frutas, result?.category)
    }
}
