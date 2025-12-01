package com.example.huertohogar.viewModel

import android.app.Application
import com.example.huertohogar.data.local.AppDatabase
import com.example.huertohogar.data.local.ProductDao
import com.example.huertohogar.data.repository.ProductRepository
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.viewmodel.ProductViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel
    private lateinit var application: Application
    private lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mock Application
        application = mockk(relaxed = true)
        database = mockk(relaxed = true)
        productDao = mockk(relaxed = true)

        // Mock AppDatabase
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns database
        every { database.productDao() } returns productDao

        // Configure default Flow returns
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        coEvery { productDao.insertProduct(any()) } just Runs
        coEvery { productDao.updateProduct(any()) } just Runs
        coEvery { productDao.deleteProduct(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `allProducts should be empty initially`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)

        // Then
        assertTrue(viewModel.allProducts.value.isEmpty())
    }

    @Test
    fun `allCategories should be empty initially`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)

        // Then
        assertTrue(viewModel.allCategories.value.isEmpty())
    }

    @Test
    fun `selectedCategory should be null initially`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)

        // Then
        assertNull(viewModel.selectedCategory.value)
    }

    @Test
    fun `productsByCategory should be empty initially`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)

        // Then
        assertTrue(viewModel.productsByCategory.value.isEmpty())
    }

    @Test
    fun `searchQuery should be empty initially`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)

        // Then
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `allProducts should load products from repository`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )
        every { productDao.getAllProducts() } returns flowOf(products)
        every { productDao.getAllCategories() } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)
        
        backgroundScope.launch {
            viewModel.allProducts.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.allProducts.value.size)
        assertEquals("Manzana", viewModel.allProducts.value[0].name)
    }

    @Test
    fun `allCategories should load categories from repository`() = runTest {
        // Given
        val categories = listOf(ProductCategory.frutas, ProductCategory.verduras)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(categories)
        every { productDao.getProductsByCategory(any()) } returns flowOf(emptyList())

        // When
        viewModel = ProductViewModel(application)
        
        backgroundScope.launch {
            viewModel.allCategories.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(2, viewModel.allCategories.value.size)
    }

    @Test
    fun `setSelectedCategory should update selected category`() = runTest {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.getProductsByCategory(ProductCategory.frutas) } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.setSelectedCategory(ProductCategory.frutas)
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(ProductCategory.frutas, viewModel.selectedCategory.value)
    }

    @Test
    fun `setSelectedCategory should load products by category`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.getProductsByCategory(ProductCategory.frutas) } returns flowOf(products)
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.setSelectedCategory(ProductCategory.frutas)
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.productsByCategory.value.size)
        assertEquals("Manzana", viewModel.productsByCategory.value[0].name)
    }

    @Test
    fun `insertProduct should call repository insertProduct`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.insertProduct(product)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify { productDao.insertProduct(product) }
    }

    @Test
    fun `updateProduct should call repository updateProduct`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.updateProduct(product)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify { productDao.updateProduct(product) }
    }

    @Test
    fun `deleteProduct should call repository deleteProduct`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.deleteProduct(product)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify { productDao.deleteProduct(product) }
    }

    @Test
    fun `getProductById should return product from repository`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        coEvery { productDao.getProductById(1) } returns product
        
        viewModel = ProductViewModel(application)

        // When
        val result = viewModel.getProductById(1)

        // Then
        assertNotNull(result)
        assertEquals("Manzana", result?.name)
    }

    @Test
    fun `getProductById should return null for non-existent id`() = runTest {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        coEvery { productDao.getProductById(999) } returns null
        
        viewModel = ProductViewModel(application)

        // When
        val result = viewModel.getProductById(999)

        // Then
        assertNull(result)
    }

    @Test
    fun `onSearchQueryChange should update searchQuery`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.onSearchQueryChange("manzana")

        // Then
        assertEquals("manzana", viewModel.searchQuery.value)
    }

    @Test
    fun `onSearchQueryChange should trigger product search`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.searchProducts("manzana") } returns flowOf(products)
        
        viewModel = ProductViewModel(application)
        
        backgroundScope.launch {
            viewModel.allProducts.collect {}
        }

        // When
        viewModel.onSearchQueryChange("manzana")
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.allProducts.value.size)
    }

    @Test
    fun `onSearchQueryChange with empty string should show all products`() = runTest {
        // Given
        val allProducts = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )
        every { productDao.getAllProducts() } returns flowOf(allProducts)
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.searchProducts(any()) } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)
        
        backgroundScope.launch {
            viewModel.allProducts.collect {}
        }
        
        viewModel.onSearchQueryChange("test")
        testScheduler.advanceUntilIdle()

        // When
        viewModel.onSearchQueryChange("")
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(2, viewModel.allProducts.value.size)
    }

    @Test
    fun `setSelectedCategory should handle different categories`() = runTest {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.getProductsByCategory(any()) } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.setSelectedCategory(ProductCategory.verduras)
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(ProductCategory.verduras, viewModel.selectedCategory.value)
    }

    @Test
    fun `insertProduct should handle multiple products`() = runTest {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.insertProduct(product1)
        viewModel.insertProduct(product2)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { productDao.insertProduct(product1) }
        coVerify(exactly = 1) { productDao.insertProduct(product2) }
    }

    @Test
    fun `updateProduct should preserve product id`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.updateProduct(product)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify { productDao.updateProduct(match { it.id == 1 }) }
    }

    @Test
    fun `deleteProduct should handle product deletion`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.deleteProduct(product)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { productDao.deleteProduct(product) }
    }

    @Test
    fun `productsByCategory should update when category changes`() = runTest {
        // Given
        val frutasProducts = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )
        val verdurasProducts = listOf(
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.getProductsByCategory(ProductCategory.frutas) } returns flowOf(frutasProducts)
        every { productDao.getProductsByCategory(ProductCategory.verduras) } returns flowOf(verdurasProducts)
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.setSelectedCategory(ProductCategory.frutas)
        testScheduler.advanceUntilIdle()
        val firstResult = viewModel.productsByCategory.value

        viewModel.setSelectedCategory(ProductCategory.verduras)
        testScheduler.advanceUntilIdle()
        val secondResult = viewModel.productsByCategory.value

        // Then
        assertEquals("Manzana", firstResult[0].name)
        assertEquals("Lechuga", secondResult[0].name)
    }

    @Test
    fun `onSearchQueryChange should handle special characters`() {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)

        // When
        viewModel.onSearchQueryChange("manzana & pera")

        // Then
        assertEquals("manzana & pera", viewModel.searchQuery.value)
    }

    @Test
    fun `allProducts should handle empty search results`() = runTest {
        // Given
        every { productDao.getAllProducts() } returns flowOf(emptyList())
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.searchProducts("xyz") } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(application)
        
        backgroundScope.launch {
            viewModel.allProducts.collect {}
        }

        // When
        viewModel.onSearchQueryChange("xyz")
        testScheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.allProducts.value.isEmpty())
    }
}
