package com.example.huertohogar.viewModel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.huertohogar.R
import com.example.huertohogar.data.local.AppDatabase
import com.example.huertohogar.data.local.CategoryDao
import com.example.huertohogar.data.repository.CategoryRepository
import com.example.huertohogar.data.repository.ProductRepository
import com.example.huertohogar.model.CategoryEntity
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.viewmodel.MainViewModel
import com.example.huertohogar.viewmodel.toCategoryModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var application: Application
    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var database: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock Application and Context
        application = mockk(relaxed = true)
        context = mockk(relaxed = true)
        resources = mockk(relaxed = true)
        database = mockk(relaxed = true)
        categoryDao = mockk(relaxed = true)
        categoryRepository = mockk(relaxed = true)

        every { application.applicationContext } returns context
        every { context.resources } returns resources
        every { context.packageName } returns "com.example.huertohogar"
        
        // Mock AppDatabase
        mockkObject(AppDatabase)
        every { AppDatabase.getDatabase(any()) } returns database
        every { database.categoryDao() } returns categoryDao
        
        // Configure default Flow returns
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        
        // Mock ProductRepository
        mockkObject(ProductRepository)
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `uiState should have initial loading state`() {
        // Given
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)

        // Then
        val initialState = viewModel.uiState.value
        assertTrue(initialState.isLoading)
        assertTrue(initialState.categories.isEmpty())
        assertTrue(initialState.featuredProducts.isEmpty())
    }

    @Test
    fun `uiState should update when categories and products are loaded`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas")
        )
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )

        every { resources.getIdentifier("frutas", "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(products)

        // When
        viewModel = MainViewModel(application)
        
        // Iniciar recolección del flow en background
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.categories.size)
        assertEquals(1, state.featuredProducts.size)
        assertEquals("Frutas", state.categories[0].name)
        assertEquals("Manzana", state.featuredProducts[0].name)
    }

    @Test
    fun `uiState should handle empty categories`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )

        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        every { ProductRepository.getProductsFlow() } returns flowOf(products)

        // When
        viewModel = MainViewModel(application)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.categories.isEmpty())
        assertEquals(0, state.featuredProducts.size)
    }

    @Test
    fun `uiState should handle empty products`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas")
        )

        every { resources.getIdentifier("frutas", "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.categories.size)
        assertTrue(state.featuredProducts.isEmpty())
    }

    @Test
    fun `uiState should handle multiple categories`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas"),
            CategoryEntity(id = 2, name = "Verduras", imageResName = "verduras", description = "Verduras frescas"),
            CategoryEntity(id = 3, name = "Lácteos", imageResName = "lacteos", description = "Productos lácteos")
        )

        every { resources.getIdentifier(any(), "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)
        
        // Iniciar recolección del flow en background
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(3, state.categories.size)
        assertEquals("Frutas", state.categories[0].name)
        assertEquals("Verduras", state.categories[1].name)
        assertEquals("Lácteos", state.categories[2].name)
    }

    @Test
    fun `uiState should handle multiple products`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras),
            Product(id = 3, name = "Leche", price = 1500.0, priceLabel = "$1.500", imagesRes = 3, category = ProductCategory.lacteos)
        )

        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        every { ProductRepository.getProductsFlow() } returns flowOf(products)

        // When
        viewModel = MainViewModel(application)
        
        // Iniciar recolección del flow en background
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(3, state.featuredProducts.size)
        assertEquals("Manzana", state.featuredProducts[0].name)
        assertEquals("Lechuga", state.featuredProducts[1].name)
        assertEquals("Leche", state.featuredProducts[2].name)
    }

    @Test
    fun `toCategoryModel should map entity correctly`() {
        // Given
        val entity = CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas")
        every { resources.getIdentifier("frutas", "drawable", "com.example.huertohogar") } returns 123

        // When
        val category = entity.toCategoryModel(context)

        // Then
        assertEquals("Frutas", category.name)
        assertEquals(123, category.imageRes)
        assertEquals("Frutas frescas", category.description)
    }

    @Test
    fun `toCategoryModel should use default image when resource not found`() {
        // Given
        val entity = CategoryEntity(id = 1, name = "Frutas", imageResName = "invalid_image", description = "Frutas frescas")
        every { resources.getIdentifier("invalid_image", "drawable", "com.example.huertohogar") } returns 0

        // When
        val category = entity.toCategoryModel(context)

        // Then
        assertEquals("Frutas", category.name)
        assertEquals(R.drawable.fondooscuro, category.imageRes)
        assertEquals("Frutas frescas", category.description)
    }

    @Test
    fun `viewModel should request database population on init`() = runTest {
        // Given
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())
        
        mockkConstructor(CategoryRepository::class)
        coEvery { anyConstructed<CategoryRepository>().populateDatabaseIfEmpty() } just Runs

        // When
        viewModel = MainViewModel(application)
        testScheduler.advanceUntilIdle()

        // Then
        coVerify { anyConstructed<CategoryRepository>().populateDatabaseIfEmpty() }
    }

    @Test
    fun `uiState should update categories when database changes`() = runTest {
        // Given
        val initialCategories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas")
        )
        val updatedCategories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas"),
            CategoryEntity(id = 2, name = "Verduras", imageResName = "verduras", description = "Verduras frescas")
        )

        every { resources.getIdentifier(any(), "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(initialCategories) andThen flowOf(updatedCategories)
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.categories.size)
    }

    @Test
    fun `MainScreenUiState should have correct default values`() {
        // When
        val state = com.example.huertohogar.viewmodel.MainScreenUiState()

        // Then
        assertTrue(state.featuredProducts.isEmpty())
        assertTrue(state.categories.isEmpty())
        assertTrue(state.isLoading)
    }

    @Test
    fun `Category data class should hold correct values`() {
        // When
        val category = com.example.huertohogar.viewmodel.Category(
            name = "Test",
            imageRes = 123,
            description = "Test description"
        )

        // Then
        assertEquals("Test", category.name)
        assertEquals(123, category.imageRes)
        assertEquals("Test description", category.description)
    }

    @Test
    fun `uiState should combine flows correctly`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas"),
            CategoryEntity(id = 2, name = "Verduras", imageResName = "verduras", description = "Verduras frescas")
        )
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas),
            Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        )

        every { resources.getIdentifier(any(), "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(products)

        // When
        viewModel = MainViewModel(application)
        
        // Iniciar recolección del flow en background
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.categories.size)
        assertEquals(2, state.featuredProducts.size)
        assertEquals("Frutas", state.categories[0].name)
        assertEquals("Verduras", state.categories[1].name)
        assertEquals("Manzana", state.featuredProducts[0].name)
        assertEquals("Lechuga", state.featuredProducts[1].name)
    }

    @Test
    fun `toCategoryModel should handle different image names`() {
        // Given
        val entities = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas_icon", description = "Desc1"),
            CategoryEntity(id = 2, name = "Verduras", imageResName = "verduras_icon", description = "Desc2")
        )

        every { resources.getIdentifier("frutas_icon", "drawable", "com.example.huertohogar") } returns 100
        every { resources.getIdentifier("verduras_icon", "drawable", "com.example.huertohogar") } returns 200

        // When
        val category1 = entities[0].toCategoryModel(context)
        val category2 = entities[1].toCategoryModel(context)

        // Then
        assertEquals(100, category1.imageRes)
        assertEquals(200, category2.imageRes)
    }

    @Test
    fun `uiState should maintain state across multiple accesses`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = "Frutas frescas")
        )
        val products = listOf(
            Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        )

        every { resources.getIdentifier("frutas", "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(products)

        // When
        viewModel = MainViewModel(application)
        testScheduler.advanceUntilIdle()

        val state1 = viewModel.uiState.value
        val state2 = viewModel.uiState.value

        // Then
        assertEquals(state1.categories.size, state2.categories.size)
        assertEquals(state1.featuredProducts.size, state2.featuredProducts.size)
        assertEquals(state1.isLoading, state2.isLoading)
    }

    @Test
    fun `viewModel should use ApplicationContext for database`() {
        // Given
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)

        // Then
        verify { AppDatabase.getDatabase(application) }
    }

    @Test
    fun `uiState should handle categories with long descriptions`() = runTest {
        // Given
        val longDescription = "A".repeat(500)
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas", description = longDescription)
        )

        every { resources.getIdentifier("frutas", "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)
        
        // Iniciar recolección del flow en background
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(longDescription, state.categories[0].description)
    }

    @Test
    fun `uiState should handle special characters in category names`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas & Verduras", imageResName = "frutas", description = "Test")
        )

        every { resources.getIdentifier("frutas", "drawable", "com.example.huertohogar") } returns R.drawable.fondooscuro
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        every { ProductRepository.getProductsFlow() } returns flowOf(emptyList())

        // When
        viewModel = MainViewModel(application)
        
        // Iniciar recolección del flow en background
        backgroundScope.launch {
            viewModel.uiState.collect {}
        }
        
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals("Frutas & Verduras", state.categories[0].name)
    }
}
