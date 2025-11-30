package com.example.huertohogar.local

import com.example.huertohogar.data.local.CategoryDao
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

class CategoryDaoTest {

    private lateinit var categoryDao: CategoryDao

    @BeforeEach
    fun setUp() {
        categoryDao = mockk(relaxed = true)
    }

    @Test
    fun `insertAll should insert categories successfully`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas_img", description = "Categoría de frutas"),
            CategoryEntity(id = 2, name = "Verduras", imageResName = "verduras_img", description = "Categoría de verduras")
        )
        coEvery { categoryDao.insertAll(categories) } returns Unit

        // When
        categoryDao.insertAll(categories)

        // Then
        coVerify(exactly = 1) { categoryDao.insertAll(categories) }
    }

    @Test
    fun `insertAll should handle empty list`() = runTest {
        // Given
        val emptyList = emptyList<CategoryEntity>()
        coEvery { categoryDao.insertAll(emptyList) } returns Unit

        // When
        categoryDao.insertAll(emptyList)

        // Then
        coVerify(exactly = 1) { categoryDao.insertAll(emptyList) }
    }

    @Test
    fun `getAllCategories should return flow of categories`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas_img", description = "Categoría de frutas"),
            CategoryEntity(id = 2, name = "Verduras", imageResName = "verduras_img", description = "Categoría de verduras")
        )
        every { categoryDao.getAllCategories() } returns flowOf(categories)

        // When
        val result = categoryDao.getAllCategories().first()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Frutas", result[0].name)
        assertEquals("Verduras", result[1].name)
    }

    @Test
    fun `getAllCategories should return empty flow when no categories`() = runTest {
        // Given
        every { categoryDao.getAllCategories() } returns flowOf(emptyList())

        // When
        val result = categoryDao.getAllCategories().first()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getCategoryCount should return correct count`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 5

        // When
        val count = categoryDao.getCategoryCount()

        // Then
        assertEquals(5, count)
    }

    @Test
    fun `getCategoryCount should return zero when no categories`() = runTest {
        // Given
        coEvery { categoryDao.getCategoryCount() } returns 0

        // When
        val count = categoryDao.getCategoryCount()

        // Then
        assertEquals(0, count)
    }

    @Test
    fun `insertAll should replace on conflict`() = runTest {
        // Given
        val category = CategoryEntity(id = 1, name = "Frutas", imageResName = "frutas_img", description = "Descripción antigua")
        val updatedCategory = CategoryEntity(id = 1, name = "Frutas Premium", imageResName = "frutas_new_img", description = "Descripción nueva")
        
        coEvery { categoryDao.insertAll(listOf(category)) } returns Unit
        coEvery { categoryDao.insertAll(listOf(updatedCategory)) } returns Unit

        // When
        categoryDao.insertAll(listOf(category))
        categoryDao.insertAll(listOf(updatedCategory))

        // Then
        coVerify(exactly = 1) { categoryDao.insertAll(listOf(category)) }
        coVerify(exactly = 1) { categoryDao.insertAll(listOf(updatedCategory)) }
    }

    @Test
    fun `getAllCategories should return categories with correct properties`() = runTest {
        // Given
        val category = CategoryEntity(
            id = 1,
            name = "Lácteos",
            imageResName = "lacteos_img",
            description = "Productos lácteos frescos"
        )
        every { categoryDao.getAllCategories() } returns flowOf(listOf(category))

        // When
        val result = categoryDao.getAllCategories().first()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Lácteos", result[0].name)
        assertEquals("lacteos_img", result[0].imageResName)
        assertEquals("Productos lácteos frescos", result[0].description)
    }

    @Test
    fun `insertAll with multiple categories should handle all items`() = runTest {
        // Given
        val categories = listOf(
            CategoryEntity(id = 1, name = "Cat1", imageResName = "img1", description = "Desc1"),
            CategoryEntity(id = 2, name = "Cat2", imageResName = "img2", description = "Desc2"),
            CategoryEntity(id = 3, name = "Cat3", imageResName = "img3", description = "Desc3")
        )
        coEvery { categoryDao.insertAll(categories) } returns Unit

        // When
        categoryDao.insertAll(categories)

        // Then
        coVerify(exactly = 1) { categoryDao.insertAll(match { it.size == 3 }) }
    }

    @Test
    fun `getCategoryCount should handle large numbers`() = runTest {
        // Given
        val largeCount = 1000
        coEvery { categoryDao.getCategoryCount() } returns largeCount

        // When
        val count = categoryDao.getCategoryCount()

        // Then
        assertEquals(largeCount, count)
        assertTrue(count > 0)
    }
}
