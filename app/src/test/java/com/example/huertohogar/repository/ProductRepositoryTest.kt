package com.example.huertohogar.repository

import com.example.huertohogar.data.local.ProductDao
import com.example.huertohogar.data.repository.ProductRepository
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

class ProductRepositoryTest {

    private lateinit var productDao: ProductDao
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        productDao = mockk(relaxed = true)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getProductsByCategory(any()) } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.searchProducts(any()) } returns flowOf(emptyList())
        productRepository = ProductRepository(productDao)
    }

    @Test
    fun `getAllProducts should return flow from dao`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )
        every { productDao.getAllProducts() } returns flowOf(products)

        // When
        val result = productRepository.getAllProducts().first()

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
        val result = productRepository.getAllProducts().first()

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
        val result = productRepository.getProductsByCategory(category).first()

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
        val result = productRepository.getProductsByCategory(category).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllCategories should return flow from dao`() = runTest {
        // Given
        val categories = listOf(ProductCategory.frutas, ProductCategory.verduras, ProductCategory.lacteos)
        every { productDao.getAllCategories() } returns flowOf(categories)

        // When
        val result = productRepository.getAllCategories().first()

        // Then
        assertEquals(3, result.size)
        assertTrue(result.contains(ProductCategory.frutas))
        assertTrue(result.contains(ProductCategory.verduras))
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
        val result = productRepository.searchProducts(query).first()

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
        val result = productRepository.searchProducts(query).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProductById should return product when exists`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.getProductById(1) } returns product

        // When
        val result = productRepository.getProductById(1)

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
        val result = productRepository.getProductById(999)

        // Then
        assertNull(result)
    }

    @Test
    fun `insertProduct should call dao insertProduct`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.insertProduct(product) } returns Unit

        // When
        productRepository.insertProduct(product)

        // Then
        coVerify(exactly = 1) { productDao.insertProduct(product) }
    }

    @Test
    fun `insertProducts should call dao insertProducts`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )
        coEvery { productDao.insertProducts(products) } returns Unit

        // When
        productRepository.insertProducts(products)

        // Then
        coVerify(exactly = 1) { productDao.insertProducts(products) }
    }

    @Test
    fun `updateProduct should call dao updateProduct`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana Premium", price = 1500.0, priceLabel = "$1.500", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.updateProduct(product) } returns Unit

        // When
        productRepository.updateProduct(product)

        // Then
        coVerify(exactly = 1) { productDao.updateProduct(product) }
    }

    @Test
    fun `deleteProduct should call dao deleteProduct`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        coEvery { productDao.deleteProduct(product) } returns Unit

        // When
        productRepository.deleteProduct(product)

        // Then
        coVerify(exactly = 1) { productDao.deleteProduct(product) }
    }

    @Test
    fun `deleteAllProducts should call dao deleteAllProducts`() = runTest {
        // Given
        coEvery { productDao.deleteAllProducts() } returns Unit

        // When
        productRepository.deleteAllProducts()

        // Then
        coVerify(exactly = 1) { productDao.deleteAllProducts() }
    }

    @Test
    fun `getProductsFlow should return 3 featured products`() = runTest {
        // When
        val result = ProductRepository.getProductsFlow().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Leche Natural", result[0].name)
        assertEquals("Miel Orgánica", result[1].name)
        assertEquals("Platános Cavendish", result[2].name)
    }

    @Test
    fun `getProductsFlow featured products should have correct categories`() = runTest {
        // When
        val result = ProductRepository.getProductsFlow().first()

        // Then
        assertEquals(ProductCategory.lacteos, result[0].category)
        assertEquals(ProductCategory.productosOrganicos, result[1].category)
        assertEquals(ProductCategory.frutas, result[2].category)
    }

    @Test
    fun `getProductsFlow featured products should have correct prices`() = runTest {
        // When
        val result = ProductRepository.getProductsFlow().first()

        // Then
        assertEquals(3800.0, result[0].price)
        assertEquals(5000.0, result[1].price)
        assertEquals(800.0, result[2].price)
    }

    @Test
    fun `getProductsFlow featured products should have price labels`() = runTest {
        // When
        val result = ProductRepository.getProductsFlow().first()

        // Then
        assertEquals("$3.800", result[0].priceLabel)
        assertEquals("$5.000", result[1].priceLabel)
        assertEquals("$800/Kg", result[2].priceLabel)
    }

    @Test
    fun `repository should delegate all calls to dao`() = runTest {
        // Given
        val product = Product(id = 1, name = "Test", price = 100.0, priceLabel = "$100", imagesRes = 1, category = ProductCategory.frutas)
        val products = listOf(product)
        
        every { productDao.getAllProducts() } returns flowOf(products)
        every { productDao.getProductsByCategory(any()) } returns flowOf(products)
        every { productDao.getAllCategories() } returns flowOf(listOf(ProductCategory.frutas))
        every { productDao.searchProducts(any()) } returns flowOf(products)
        coEvery { productDao.getProductById(any()) } returns product
        coEvery { productDao.insertProduct(any()) } returns Unit
        coEvery { productDao.insertProducts(any()) } returns Unit
        coEvery { productDao.updateProduct(any()) } returns Unit
        coEvery { productDao.deleteProduct(any()) } returns Unit
        coEvery { productDao.deleteAllProducts() } returns Unit

        // When
        productRepository.getAllProducts().first()
        productRepository.getProductsByCategory(ProductCategory.frutas).first()
        productRepository.getAllCategories().first()
        productRepository.searchProducts("test").first()
        productRepository.getProductById(1)
        productRepository.insertProduct(product)
        productRepository.insertProducts(products)
        productRepository.updateProduct(product)
        productRepository.deleteProduct(product)
        productRepository.deleteAllProducts()

        // Then - Verifica que todas las llamadas se delegaron al DAO
        coVerify(atLeast = 1) { productDao.getAllProducts() }
        coVerify(atLeast = 1) { productDao.getProductsByCategory(any()) }
        coVerify(atLeast = 1) { productDao.getAllCategories() }
        coVerify(atLeast = 1) { productDao.searchProducts(any()) }
        coVerify(exactly = 1) { productDao.getProductById(1) }
        coVerify(exactly = 1) { productDao.insertProduct(product) }
        coVerify(exactly = 1) { productDao.insertProducts(products) }
        coVerify(exactly = 1) { productDao.updateProduct(product) }
        coVerify(exactly = 1) { productDao.deleteProduct(product) }
        coVerify(exactly = 1) { productDao.deleteAllProducts() }
    }

    @Test
    fun `getProductsFlow should be accessible as static method`() = runTest {
        // When
        val result = ProductRepository.getProductsFlow().first()

        // Then
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }
}
