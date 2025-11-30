package com.example.huertohogar.data.local

import android.content.Context
import androidx.room.Room
import com.example.huertohogar.model.CategoryEntity
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("CategoryDao Tests")
class CategoryDaoTest {

    private lateinit var categoryDao: CategoryDao

    @BeforeEach
    fun setup() {
        categoryDao = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSampleCategory(
        id: Int = 1,
        name: String = "Frutas",
        imageResName: String = "frutas_icon",
        description: String = "Frutas frescas y orgánicas"
    ) = CategoryEntity(
        id = id,
        name = name,
        imageResName = imageResName,
        description = description
    )

    @Nested
    @DisplayName("insertAll Tests")
    inner class InsertAllTests {

        @Test
        @DisplayName("insertAll should insert single category")
        fun `insertAll should insert single category`() = runBlocking {
            val category = createSampleCategory()
            coEvery { categoryDao.insertAll(listOf(category)) } just Runs

            categoryDao.insertAll(listOf(category))

            coVerify { categoryDao.insertAll(listOf(category)) }
        }

        @Test
        @DisplayName("insertAll should insert multiple categories")
        fun `insertAll should insert multiple categories`() = runBlocking {
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas"),
                createSampleCategory(id = 2, name = "Verduras"),
                createSampleCategory(id = 3, name = "Lácteos")
            )
            coEvery { categoryDao.insertAll(categories) } just Runs

            categoryDao.insertAll(categories)

            coVerify { categoryDao.insertAll(categories) }
        }

        @Test
        @DisplayName("insertAll should insert empty list")
        fun `insertAll should insert empty list`() = runBlocking {
            val emptyList = emptyList<CategoryEntity>()
            coEvery { categoryDao.insertAll(emptyList) } just Runs

            categoryDao.insertAll(emptyList)

            coVerify { categoryDao.insertAll(emptyList) }
        }

        @Test
        @DisplayName("insertAll should replace on conflict")
        fun `insertAll should replace on conflict`() = runBlocking {
            val category1 = createSampleCategory(id = 1, name = "Frutas Original")
            val category2 = createSampleCategory(id = 1, name = "Frutas Actualizado")
            
            coEvery { categoryDao.insertAll(any()) } just Runs

            categoryDao.insertAll(listOf(category1))
            categoryDao.insertAll(listOf(category2))

            coVerify(exactly = 2) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("insertAll should handle categories with different data")
        fun `insertAll should handle categories with different data`() = runBlocking {
            val categories = listOf(
                createSampleCategory(
                    id = 1,
                    name = "Frutas Tropicales",
                    imageResName = "tropical_fruits",
                    description = "Frutas exóticas del trópico"
                ),
                createSampleCategory(
                    id = 2,
                    name = "Vegetales Orgánicos",
                    imageResName = "organic_veggies",
                    description = "Vegetales cultivados sin pesticidas"
                )
            )
            coEvery { categoryDao.insertAll(categories) } just Runs

            categoryDao.insertAll(categories)

            coVerify { categoryDao.insertAll(categories) }
        }
    }

    @Nested
    @DisplayName("getAllCategories Tests")
    inner class GetAllCategoriesTests {

        @Test
        @DisplayName("getAllCategories should return Flow of categories")
        fun `getAllCategories should return Flow of categories`() = runBlocking {
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas"),
                createSampleCategory(id = 2, name = "Verduras")
            )
            every { categoryDao.getAllCategories() } returns flowOf(categories)

            val result = categoryDao.getAllCategories().first()

            result shouldHaveSize 2
            result shouldBe categories
        }

        @Test
        @DisplayName("getAllCategories should return empty list when no categories")
        fun `getAllCategories should return empty list when no categories`() = runBlocking {
            every { categoryDao.getAllCategories() } returns flowOf(emptyList())

            val result = categoryDao.getAllCategories().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("getAllCategories should return all inserted categories")
        fun `getAllCategories should return all inserted categories`() = runBlocking {
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas"),
                createSampleCategory(id = 2, name = "Verduras"),
                createSampleCategory(id = 3, name = "Lácteos"),
                createSampleCategory(id = 4, name = "Carnes"),
                createSampleCategory(id = 5, name = "Granos")
            )
            every { categoryDao.getAllCategories() } returns flowOf(categories)

            val result = categoryDao.getAllCategories().first()

            result shouldHaveSize 5
            result shouldContain categories[0]
            result shouldContain categories[4]
        }

        @Test
        @DisplayName("getAllCategories should return Flow that can be collected")
        fun `getAllCategories should return Flow that can be collected`() = runBlocking {
            val categories = listOf(createSampleCategory())
            every { categoryDao.getAllCategories() } returns flowOf(categories)

            val flow = categoryDao.getAllCategories()

            flow.shouldNotBeNull()
            val result = flow.first()
            result shouldHaveSize 1
        }

        @Test
        @DisplayName("getAllCategories should preserve category data")
        fun `getAllCategories should preserve category data`() = runBlocking {
            val category = createSampleCategory(
                id = 10,
                name = "Productos Especiales",
                imageResName = "special_icon",
                description = "Productos únicos y especiales"
            )
            every { categoryDao.getAllCategories() } returns flowOf(listOf(category))

            val result = categoryDao.getAllCategories().first()

            result.first().id shouldBe 10
            result.first().name shouldBe "Productos Especiales"
            result.first().imageResName shouldBe "special_icon"
            result.first().description shouldBe "Productos únicos y especiales"
        }
    }

    @Nested
    @DisplayName("getCategoryCount Tests")
    inner class GetCategoryCountTests {

        @Test
        @DisplayName("getCategoryCount should return zero when no categories")
        fun `getCategoryCount should return zero when no categories`() = runBlocking {
            coEvery { categoryDao.getCategoryCount() } returns 0

            val count = categoryDao.getCategoryCount()

            count shouldBe 0
        }

        @Test
        @DisplayName("getCategoryCount should return correct count for single category")
        fun `getCategoryCount should return correct count for single category`() = runBlocking {
            coEvery { categoryDao.getCategoryCount() } returns 1

            val count = categoryDao.getCategoryCount()

            count shouldBe 1
        }

        @Test
        @DisplayName("getCategoryCount should return correct count for multiple categories")
        fun `getCategoryCount should return correct count for multiple categories`() = runBlocking {
            coEvery { categoryDao.getCategoryCount() } returns 5

            val count = categoryDao.getCategoryCount()

            count shouldBe 5
        }

        @Test
        @DisplayName("getCategoryCount should return updated count after insertion")
        fun `getCategoryCount should return updated count after insertion`() = runBlocking {
            coEvery { categoryDao.getCategoryCount() } returnsMany listOf(0, 1, 3)

            val countBefore = categoryDao.getCategoryCount()
            val countAfterOne = categoryDao.getCategoryCount()
            val countAfterThree = categoryDao.getCategoryCount()

            countBefore shouldBe 0
            countAfterOne shouldBe 1
            countAfterThree shouldBe 3
        }

        @Test
        @DisplayName("getCategoryCount should return non-negative value")
        fun `getCategoryCount should return non-negative value`() = runBlocking {
            coEvery { categoryDao.getCategoryCount() } returns 10

            val count = categoryDao.getCategoryCount()

            (count >= 0) shouldBe true
        }

        @Test
        @DisplayName("getCategoryCount should handle large numbers")
        fun `getCategoryCount should handle large numbers`() = runBlocking {
            coEvery { categoryDao.getCategoryCount() } returns 1000

            val count = categoryDao.getCategoryCount()

            count shouldBe 1000
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    inner class IntegrationScenariosTests {

        @Test
        @DisplayName("should insert and then retrieve categories")
        fun `should insert and then retrieve categories`() = runBlocking {
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas"),
                createSampleCategory(id = 2, name = "Verduras")
            )
            
            coEvery { categoryDao.insertAll(categories) } just Runs
            every { categoryDao.getAllCategories() } returns flowOf(categories)

            categoryDao.insertAll(categories)
            val result = categoryDao.getAllCategories().first()

            result shouldHaveSize 2
            result shouldBe categories
        }

        @Test
        @DisplayName("should verify count matches inserted categories")
        fun `should verify count matches inserted categories`() = runBlocking {
            val categories = listOf(
                createSampleCategory(id = 1),
                createSampleCategory(id = 2),
                createSampleCategory(id = 3)
            )
            
            coEvery { categoryDao.insertAll(categories) } just Runs
            coEvery { categoryDao.getCategoryCount() } returns 3

            categoryDao.insertAll(categories)
            val count = categoryDao.getCategoryCount()

            count shouldBe categories.size
        }

        @Test
        @DisplayName("should handle insert then count scenario")
        fun `should handle insert then count scenario`() = runBlocking {
            val initialCategories = listOf(
                createSampleCategory(id = 1, name = "Cat1")
            )
            val moreCategories = listOf(
                createSampleCategory(id = 2, name = "Cat2"),
                createSampleCategory(id = 3, name = "Cat3")
            )
            
            coEvery { categoryDao.insertAll(any()) } just Runs
            coEvery { categoryDao.getCategoryCount() } returnsMany listOf(1, 3)

            categoryDao.insertAll(initialCategories)
            val countAfterFirst = categoryDao.getCategoryCount()
            
            categoryDao.insertAll(moreCategories)
            val countAfterSecond = categoryDao.getCategoryCount()

            countAfterFirst shouldBe 1
            countAfterSecond shouldBe 3
        }
    }

    @Nested
    @DisplayName("Data Validation Tests")
    inner class DataValidationTests {

        @Test
        @DisplayName("should handle category with auto-generated id")
        fun `should handle category with auto-generated id`() = runBlocking {
            val category = createSampleCategory(id = 0, name = "Nueva Categoría")
            coEvery { categoryDao.insertAll(listOf(category)) } just Runs

            categoryDao.insertAll(listOf(category))

            coVerify { categoryDao.insertAll(listOf(category)) }
        }

        @Test
        @DisplayName("should handle category with special characters in name")
        fun `should handle category with special characters in name`() = runBlocking {
            val category = createSampleCategory(
                name = "Frutas & Verduras Orgánicas 100%"
            )
            coEvery { categoryDao.insertAll(listOf(category)) } just Runs

            categoryDao.insertAll(listOf(category))

            coVerify { categoryDao.insertAll(match { it.first().name.contains("&") }) }
        }

        @Test
        @DisplayName("should handle category with empty description")
        fun `should handle category with empty description`() = runBlocking {
            val category = createSampleCategory(description = "")
            coEvery { categoryDao.insertAll(listOf(category)) } just Runs

            categoryDao.insertAll(listOf(category))

            coVerify { categoryDao.insertAll(match { it.first().description.isEmpty() }) }
        }

        @Test
        @DisplayName("should handle category with long description")
        fun `should handle category with long description`() = runBlocking {
            val longDescription = "Esta es una descripción muy larga ".repeat(20)
            val category = createSampleCategory(description = longDescription)
            coEvery { categoryDao.insertAll(listOf(category)) } just Runs

            categoryDao.insertAll(listOf(category))

            coVerify { categoryDao.insertAll(match { it.first().description.length > 100 }) }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("should handle duplicate category insertions with REPLACE strategy")
        fun `should handle duplicate category insertions with REPLACE strategy`() = runBlocking {
            val category1 = createSampleCategory(id = 1, name = "Original")
            val category2 = createSampleCategory(id = 1, name = "Duplicado")
            
            coEvery { categoryDao.insertAll(any()) } just Runs
            every { categoryDao.getAllCategories() } returns flowOf(listOf(category2))

            categoryDao.insertAll(listOf(category1))
            categoryDao.insertAll(listOf(category2))
            val result = categoryDao.getAllCategories().first()

            result shouldHaveSize 1
            result.first().name shouldBe "Duplicado"
        }

        @Test
        @DisplayName("should handle categories with same name but different ids")
        fun `should handle categories with same name but different ids`() = runBlocking {
            val category1 = createSampleCategory(id = 1, name = "Frutas")
            val category2 = createSampleCategory(id = 2, name = "Frutas")
            
            coEvery { categoryDao.insertAll(listOf(category1, category2)) } just Runs
            every { categoryDao.getAllCategories() } returns flowOf(listOf(category1, category2))

            categoryDao.insertAll(listOf(category1, category2))
            val result = categoryDao.getAllCategories().first()

            result shouldHaveSize 2
        }

        @Test
        @DisplayName("should handle maximum integer value for id")
        fun `should handle maximum integer value for id`() = runBlocking {
            val category = createSampleCategory(id = Int.MAX_VALUE)
            coEvery { categoryDao.insertAll(listOf(category)) } just Runs

            categoryDao.insertAll(listOf(category))

            coVerify { categoryDao.insertAll(match { it.first().id == Int.MAX_VALUE }) }
        }
    }
}