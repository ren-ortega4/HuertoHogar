package com.example.huertohogar.viewmodel

import android.app.Application
import com.example.huertohogar.data.local.AppDatabase
import com.example.huertohogar.data.local.ProductDao
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("ProductViewModel Tests")
class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel
    private lateinit var mockApplication: Application
    private lateinit var mockDatabase: AppDatabase
    private lateinit var mockProductDao: ProductDao
    
    private val testDispatcher = StandardTestDispatcher()
    
    // Test data
    private val testCategory1 = ProductCategory.frutas
    private val testCategory2 = ProductCategory.verduras
    private val testCategory3 = ProductCategory.lacteos
    
    private val testProduct1 = Product(
        id = 1,
        name = "Manzana",
        price = "$2.500",
        imagesRes = 0,
        category = testCategory1
    )
    
    private val testProduct2 = Product(
        id = 2,
        name = "Banana",
        price = "$1.800",
        imagesRes = 0,
        category = testCategory1
    )
    
    private val testProduct3 = Product(
        id = 3,
        name = "Lechuga",
        price = "$1.200",
        imagesRes = 0,
        category = testCategory2
    )
    
    private val testProduct4 = Product(
        id = 4,
        name = "Leche",
        price = "$3.500",
        imagesRes = 0,
        category = testCategory3
    )
    
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock Application
        mockApplication = mockk(relaxed = true)
        
        // Mock ProductDao
        mockProductDao = mockk(relaxed = true)
        
        // Mock Database
        mockDatabase = mockk(relaxed = true)
        every { mockDatabase.productDao() } returns mockProductDao
        
        // Mock AppDatabase.getDatabase to return our mock
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns mockDatabase
        
        // Setup default mock behaviors
        every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
        every { mockProductDao.getAllCategories() } returns flowOf(emptyList())
        every { mockProductDao.searchProducts(any()) } returns flowOf(emptyList())
        every { mockProductDao.getProductsByCategory(any()) } returns flowOf(emptyList())
        coEvery { mockProductDao.getProductById(any()) } returns null
        coEvery { mockProductDao.insertProduct(any()) } just Runs
        coEvery { mockProductDao.updateProduct(any()) } just Runs
        coEvery { mockProductDao.deleteProduct(any()) } just Runs
    }
    
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }
    
    @Test
    @DisplayName("ViewModel initialization creates repository and initializes flows")
    fun `should initialize viewModel with empty products and categories`() = runTest {
        // When
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // Then
        viewModel.allProducts.value.shouldBeEmpty()
        viewModel.allCategories.value.shouldBeEmpty()
        viewModel.selectedCategory.value.shouldBeNull()
        viewModel.productsByCategory.value.shouldBeEmpty()
        viewModel.searchQuery.value shouldBe ""
        
        verify { AppDatabase.getDatabase(mockApplication) }
        verify { mockDatabase.productDao() }
    }
    
    @Test
    @DisplayName("Should load all products on initialization")
    fun `should load all products on initialization`() = runTest {
        // Given
        val products = listOf(testProduct1, testProduct2, testProduct3)
        every { mockProductDao.getAllProducts() } returns flowOf(products)
        
        // When
        viewModel = ProductViewModel(mockApplication)
        
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allProducts.collect {}
        }
        
        advanceUntilIdle()
        
        // Then
        viewModel.allProducts.value shouldHaveSize 3
        viewModel.allProducts.value shouldContain testProduct1
        viewModel.allProducts.value shouldContain testProduct2
        viewModel.allProducts.value shouldContain testProduct3
        
        job.cancel()
    }
    
    @Test
    @DisplayName("Should load all categories on initialization")
    fun `should load all categories on initialization`() = runTest {
        // Given
        val categories = listOf(testCategory1, testCategory2, testCategory3)
        every { mockProductDao.getAllCategories() } returns flowOf(categories)
        
        // When
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // Then
        viewModel.allCategories.value shouldHaveSize 3
        viewModel.allCategories.value shouldContain testCategory1
        viewModel.allCategories.value shouldContain testCategory2
        viewModel.allCategories.value shouldContain testCategory3
    }
    
    @Test
    @DisplayName("Should auto-select first category when categories are loaded")
    fun `should auto-select first category when categories are loaded`() = runTest {
        // Given
        val categories = listOf(testCategory1, testCategory2, testCategory3)
        val categoryProducts = listOf(testProduct1, testProduct2)
        every { mockProductDao.getAllCategories() } returns flowOf(categories)
        every { mockProductDao.getProductsByCategory(testCategory1) } returns flowOf(categoryProducts)
        
        // When
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // Then
        viewModel.selectedCategory.value shouldBe testCategory1
        viewModel.productsByCategory.value shouldHaveSize 2
    }
    
    @Test
    @DisplayName("Should set selected category and load its products")
    fun `should set selected category and load its products`() = runTest {
        // Given
        val categories = listOf(testCategory1, testCategory2, testCategory3)
        val verdurasProducts = listOf(testProduct3)
        every { mockProductDao.getAllCategories() } returns flowOf(categories)
        every { mockProductDao.getProductsByCategory(testCategory1) } returns flowOf(emptyList())
        every { mockProductDao.getProductsByCategory(testCategory2) } returns flowOf(verdurasProducts)
        
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // When
        viewModel.setSelectedCategory(testCategory2)
        advanceUntilIdle()
        
        // Then
        viewModel.selectedCategory.value shouldBe testCategory2
        viewModel.productsByCategory.value shouldHaveSize 1
        viewModel.productsByCategory.value shouldContain testProduct3
    }
    
    @Test
    @DisplayName("Should switch between categories correctly")
    fun `should switch between categories correctly`() = runTest {
        // Given
        val categories = listOf(testCategory1, testCategory2, testCategory3)
        val frutasProducts = listOf(testProduct1, testProduct2)
        val lacteosProducts = listOf(testProduct4)
        
        every { mockProductDao.getAllCategories() } returns flowOf(categories)
        every { mockProductDao.getProductsByCategory(testCategory1) } returns flowOf(frutasProducts)
        every { mockProductDao.getProductsByCategory(testCategory3) } returns flowOf(lacteosProducts)
        
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // When - First selection
        viewModel.setSelectedCategory(testCategory1)
        advanceUntilIdle()
        
        // Then
        viewModel.selectedCategory.value shouldBe testCategory1
        viewModel.productsByCategory.value shouldHaveSize 2
        
        // When - Second selection
        viewModel.setSelectedCategory(testCategory3)
        advanceUntilIdle()
        
        // Then
        viewModel.selectedCategory.value shouldBe testCategory3
        viewModel.productsByCategory.value shouldHaveSize 1
        viewModel.productsByCategory.value shouldContain testProduct4
    }
    
    @Test
    @DisplayName("Should insert product successfully")
    fun `should insert product successfully`() = runTest {
        // Given
        viewModel = ProductViewModel(mockApplication)
        val newProduct = testProduct1.copy(id = 0)
        
        // When
        viewModel.insertProduct(newProduct)
        advanceUntilIdle()
        
        // Then
        coVerify { mockProductDao.insertProduct(newProduct) }
    }
    
    @Test
    @DisplayName("Should update product successfully")
    fun `should update product successfully`() = runTest {
        // Given
        viewModel = ProductViewModel(mockApplication)
        val updatedProduct = testProduct1.copy(name = "Manzana Verde", price = "$3.000")
        
        // When
        viewModel.updateProduct(updatedProduct)
        advanceUntilIdle()
        
        // Then
        coVerify { mockProductDao.updateProduct(updatedProduct) }
    }
    
    @Test
    @DisplayName("Should delete product successfully")
    fun `should delete product successfully`() = runTest {
        // Given
        viewModel = ProductViewModel(mockApplication)
        
        // When
        viewModel.deleteProduct(testProduct1)
        advanceUntilIdle()
        
        // Then
        coVerify { mockProductDao.deleteProduct(testProduct1) }
    }
    
    @Test
    @DisplayName("Should get product by ID successfully")
    fun `should get product by ID successfully`() = runTest {
        // Given
        viewModel = ProductViewModel(mockApplication)
        coEvery { mockProductDao.getProductById(1) } returns testProduct1
        
        // When
        val result = viewModel.getProductById(1)
        
        // Then
        result.shouldNotBeNull()
        result shouldBe testProduct1
        coVerify { mockProductDao.getProductById(1) }
    }
    
    @Test
    @DisplayName("Should return null when product ID not found")
    fun `should return null when product ID not found`() = runTest {
        // Given
        viewModel = ProductViewModel(mockApplication)
        coEvery { mockProductDao.getProductById(999) } returns null
        
        // When
        val result = viewModel.getProductById(999)
        
        // Then
        result.shouldBeNull()
        coVerify { mockProductDao.getProductById(999) }
    }
    
    @Test
    @DisplayName("Should update search query")
    fun `should update search query`() = runTest {
        // Given
        every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // When
        viewModel.onSearchQueryChange("Manzana")
        advanceUntilIdle()
        
        // Then
        viewModel.searchQuery.value shouldBe "Manzana"
    }

    @Test
    @DisplayName("Should search products when query is not blank")
    fun `should search products when query is not blank`() = runTest {
        // Given
        val allProducts = listOf(testProduct1, testProduct2, testProduct3, testProduct4)
        val searchResults = listOf(testProduct1, testProduct2)

        // Configurar el mock ANTES de crear el ViewModel
        every { mockProductDao.getAllProducts() } returns flowOf(allProducts)
        every { mockProductDao.searchProducts("Manzana") } returns flowOf(searchResults)

        // When - Crear ViewModel primero
        viewModel = ProductViewModel(mockApplication)
        
        // Crear un job para recopilar el flow (esto activa el StateFlow)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allProducts.collect {}
        }
        
        advanceUntilIdle()

        // Verificar estado inicial
        viewModel.allProducts.value shouldHaveSize 4

        // When - Realizar búsqueda
        viewModel.onSearchQueryChange("Manzana")
        advanceUntilIdle()

        // Then
        viewModel.searchQuery.value shouldBe "Manzana"
        viewModel.allProducts.value shouldHaveSize 2
        viewModel.allProducts.value shouldContain testProduct1
        viewModel.allProducts.value shouldContain testProduct2

        verify { mockProductDao.searchProducts("Manzana") }
        
        job.cancel()
    }


    @Test
    @DisplayName("Should return all products when search query is blank")
    fun `should return all products when search query is blank`() = runTest {
        // Given
        val allProducts = listOf(testProduct1, testProduct2, testProduct3, testProduct4)
        every { mockProductDao.getAllProducts() } returns flowOf(allProducts)
        every { mockProductDao.searchProducts("") } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(mockApplication)
        
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allProducts.collect {}
        }
        
        advanceUntilIdle()
        
        // When
        viewModel.onSearchQueryChange("")
        advanceUntilIdle()
        
        // Then
        viewModel.searchQuery.value shouldBe ""
        viewModel.allProducts.value shouldHaveSize 4
        
        verify { mockProductDao.getAllProducts() }
        
        job.cancel()
    }
    
    @Test
    @DisplayName("Should return all products when search query becomes blank after search")
    fun `should return all products when search query becomes blank after search`() = runTest {
        // Given
        val allProducts = listOf(testProduct1, testProduct2, testProduct3, testProduct4)
        val searchResults = listOf(testProduct1)
        
        every { mockProductDao.getAllProducts() } returns flowOf(allProducts)
        every { mockProductDao.searchProducts("Manzana") } returns flowOf(searchResults)
        
        viewModel = ProductViewModel(mockApplication)
        
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allProducts.collect {}
        }
        
        advanceUntilIdle()
        
        // First search
        viewModel.onSearchQueryChange("Manzana")
        advanceUntilIdle()
        
        // When - Clear search
        viewModel.onSearchQueryChange("")
        advanceUntilIdle()
        
        // Then
        viewModel.searchQuery.value shouldBe ""
        viewModel.allProducts.value shouldHaveSize 4
        
        job.cancel()
    }
    
    @Test
    @DisplayName("Should handle empty search results")
    fun `should handle empty search results`() = runTest {
        // Given
        every { mockProductDao.searchProducts("NoExiste") } returns flowOf(emptyList())
        every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // When
        viewModel.onSearchQueryChange("NoExiste")
        advanceUntilIdle()
        
        // Then
        viewModel.searchQuery.value shouldBe "NoExiste"
        viewModel.allProducts.value.shouldBeEmpty()
    }
    
    @Test
    @DisplayName("Should handle category with no products")
    fun `should handle category with no products`() = runTest {
        // Given
        val categories = listOf(testCategory1, testCategory2)
        every { mockProductDao.getAllCategories() } returns flowOf(categories)
        every { mockProductDao.getProductsByCategory(testCategory1) } returns flowOf(emptyList())
        every { mockProductDao.getProductsByCategory(testCategory2) } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // When
        viewModel.setSelectedCategory(testCategory2)
        advanceUntilIdle()
        
        // Then
        viewModel.selectedCategory.value shouldBe testCategory2
        viewModel.productsByCategory.value.shouldBeEmpty()
    }
    
    @Test
    @DisplayName("Should handle multiple product operations")
    fun `should handle multiple product operations`() = runTest {
        // Given
        viewModel = ProductViewModel(mockApplication)
        
        // When - Insert
        viewModel.insertProduct(testProduct1)
        advanceUntilIdle()
        
        // When - Update
        val updated = testProduct1.copy(price = "$5.000")
        viewModel.updateProduct(updated)
        advanceUntilIdle()
        
        // When - Delete
        viewModel.deleteProduct(testProduct1)
        advanceUntilIdle()
        
        // Then
        coVerify(exactly = 1) { mockProductDao.insertProduct(testProduct1) }
        coVerify(exactly = 1) { mockProductDao.updateProduct(updated) }
        coVerify(exactly = 1) { mockProductDao.deleteProduct(testProduct1) }
    }
    
    @Test
    @DisplayName("Should maintain search query state")
    fun `should maintain search query state`() = runTest {
        // Given
        every { mockProductDao.searchProducts(any()) } returns flowOf(emptyList())
        every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
        
        viewModel = ProductViewModel(mockApplication)
        advanceUntilIdle()
        
        // When
        viewModel.onSearchQueryChange("Test1")
        advanceUntilIdle()
        
        viewModel.onSearchQueryChange("Test2")
        advanceUntilIdle()
        
        viewModel.onSearchQueryChange("Test3")
        advanceUntilIdle()
        
        // Then
        viewModel.searchQuery.value shouldBe "Test3"
    }
}