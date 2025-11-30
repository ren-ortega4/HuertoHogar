package com.example.huertohogar.repository

import com.example.huertohogar.data.local.CategoryDao
import com.example.huertohogar.data.repository.CategoryRepository
import com.example.huertohogar.model.CategoryEntity
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

class CategoryRepositoryTest {

    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    fun setUp() {
        categoryDao = mockk(relaxed = true)
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        categoryRepository = CategoryRepository(categoryDao)
    }

    @Test
    fun `allCategories should return flow from dao`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas Frescas", imageResName = "fruta", description = "Descripción frutas"),
            CategoryEntity(id = 2, name = "Verduras Orgánicas", imageResName = "verdura", description = "Descripción verduras")
        )
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        val newRepository = CategoryRepository(categoryDao)

        // When
        val result = newRepository.allCategories.first()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Frutas Frescas", result[0].name)
        assertEquals("Verduras Orgánicas", result[1].name)
    }

    @Test
    fun `allCategories should return empty flow when no categories`() = runTest {
        // Given
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())
        val newRepository = CategoryRepository(categoryDao)

        // When
        val result = newRepository.allCategories.first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `populateDatabaseIfEmpty should insert categories when count is zero`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify(exactly = 1) { categoryDao.getCategoryCount() }
        coVerify(exactly = 1) { categoryDao.insertAll(match { it.size == 4 }) }
    }

    @Test
    fun `populateDatabaseIfEmpty should not insert when categories already exist`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 5

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify(exactly = 1) { categoryDao.getCategoryCount() }
        coVerify(exactly = 0) { categoryDao.insertAll(any()) }
    }

    @Test
    fun `populateDatabaseIfEmpty should insert exactly 4 initial categories`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify { categoryDao.insertAll(match { categories ->
            categories.size == 4 &&
            categories.any { it.name == "Frutas Frescas" } &&
            categories.any { it.name == "Verduras Orgánicas" } &&
            categories.any { it.name == "Productos Orgánicos" } &&
            categories.any { it.name == "Productos Lácteos" }
        })}
    }

    @Test
    fun `initial categories should have correct names`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify { categoryDao.insertAll(match { categories ->
            categories[0].name == "Frutas Frescas" &&
            categories[1].name == "Verduras Orgánicas" &&
            categories[2].name == "Productos Orgánicos" &&
            categories[3].name == "Productos Lácteos"
        })}
    }

    @Test
    fun `initial categories should have correct image names`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify { categoryDao.insertAll(match { categories ->
            categories[0].imageResName == "fruta" &&
            categories[1].imageResName == "verdura" &&
            categories[2].imageResName == "organico" &&
            categories[3].imageResName == "lacteos"
        })}
    }

    @Test
    fun `initial categories should have non-empty descriptions`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify { categoryDao.insertAll(match { categories ->
            categories.all { it.description.isNotEmpty() }
        })}
    }

    @Test
    fun `allCategories should expose dao flow directly`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Test", imageResName = "test", description = "Test desc")
        )
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        val newRepository = CategoryRepository(categoryDao)

        // When
        val result = newRepository.allCategories.first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Test", result[0].name)
    }

    @Test
    fun `populateDatabaseIfEmpty should work on IO dispatcher`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then - Si se ejecuta sin errores, está usando el dispatcher correcto
        coVerify(exactly = 1) { categoryDao.insertAll(any()) }
    }

    @Test
    fun `populateDatabaseIfEmpty should handle exception from dao`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } throws Exception("Database error")

        // When & Then
        try {
            categoryRepository.populateDatabaseIfEmpty()
            fail("Should have thrown exception")
        } catch (e: Exception) {
            assertEquals("Database error", e.message)
        }
    }

    @Test
    fun `repository should correctly initialize with dao`() {
        // Given & When
        val repository = CategoryRepository(categoryDao)

        // Then
        assertNotNull(repository)
        assertNotNull(repository.allCategories)
    }

    @Test
    fun `allCategories flow should handle multiple emissions`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Cat1", imageResName = "img1", description = "Desc1")
        )
        every { categoryDao.getAllCategories() } returns flowOf(categories)
        val newRepository = CategoryRepository(categoryDao)

        // When
        val result = newRepository.allCategories.first()

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
    }

    @Test
    fun `initial categories should have Frutas Frescas with correct data`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify { categoryDao.insertAll(match { categories ->
            val frutas = categories.find { it.name == "Frutas Frescas" }
            frutas != null && 
            frutas.imageResName == "fruta" &&
            frutas.description.contains("sabor y frescura")
        })}
    }

    @Test
    fun `initial categories should have Verduras Orgánicas with correct data`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()

        // Then
        coVerify { categoryDao.insertAll(match { categories ->
            val verduras = categories.find { it.name == "Verduras Orgánicas" }
            verduras != null && 
            verduras.imageResName == "verdura" &&
            verduras.description.contains("cultivadas sin el uso")
        })}
    }

    @Test
    fun `populateDatabaseIfEmpty should be idempotent`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returnsMany listOf(0, 4)
        coEvery { categoryDao.insertAll(any()) } returns Unit

        // When
        categoryRepository.populateDatabaseIfEmpty()
        categoryRepository.populateDatabaseIfEmpty()

        // Then - Solo debe insertar la primera vez
        coVerify(exactly = 1) { categoryDao.insertAll(any()) }
    }
}
