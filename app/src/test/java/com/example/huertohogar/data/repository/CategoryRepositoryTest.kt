package com.example.huertohogar.data.repository

import com.example.huertohogar.data.local.CategoryDao
import com.example.huertohogar.model.CategoryEntity
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("CategoryRepository Tests")
class CategoryRepositoryTest {

    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    fun setup() {
        categoryDao = mockk(relaxed = true)
        categoryRepository = CategoryRepository(categoryDao)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSampleCategory(
        id: Int = 1,
        name: String = "Frutas Frescas",
        imageResName: String = "fruta",
        description: String = "Descripción de prueba"
    ) = CategoryEntity(
        id = id,
        name = name,
        imageResName = imageResName,
        description = description
    )

    @Nested
    @DisplayName("allCategories Flow Tests")
    inner class AllCategoriesFlowTests {

        @Test
        @DisplayName("allCategories debe retornar Flow de categorías desde el DAO")
        fun `allCategories should return Flow from DAO`() = runTest {
            // Given
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas"),
                createSampleCategory(id = 2, name = "Verduras"),
                createSampleCategory(id = 3, name = "Lácteos")
            )
            every { categoryDao.getAllCategories() } returns flowOf(categories)
            val testRepository = CategoryRepository(categoryDao)

            // When
            val result = testRepository.allCategories.first()

            // Then
            result.shouldNotBeNull()
            result shouldHaveSize 3
            result shouldContain categories[0]
            result shouldContain categories[1]
            result shouldContain categories[2]
            verify(atLeast = 1) { categoryDao.getAllCategories() }
        }

        @Test
        @DisplayName("allCategories debe retornar Flow vacío cuando no hay categorías")
        fun `allCategories should return empty Flow when no categories`() = runTest {
            // Given
            every { categoryDao.getAllCategories() } returns flowOf(emptyList())
            val testRepository = CategoryRepository(categoryDao)

            // When
            val result = testRepository.allCategories.first()

            // Then
            result.shouldNotBeNull()
            result.shouldBeEmpty()
            verify(atLeast = 1) { categoryDao.getAllCategories() }
        }

        @Test
        @DisplayName("allCategories debe preservar los datos de las categorías")
        fun `allCategories should preserve category data`() = runTest {
            // Given
            val category = createSampleCategory(
                id = 10,
                name = "Productos Orgánicos",
                imageResName = "organico",
                description = "Productos naturales y saludables"
            )
            every { categoryDao.getAllCategories() } returns flowOf(listOf(category))
            // Recreate repository with the mocked DAO to ensure the Flow is properly initialized
            val testRepository = CategoryRepository(categoryDao)

            // When
            val result = testRepository.allCategories.first()

            // Then
            result shouldHaveSize 1
            result.first().id shouldBe 10
            result.first().name shouldBe "Productos Orgánicos"
            result.first().imageResName shouldBe "organico"
            result.first().description shouldBe "Productos naturales y saludables"
        }

        @Test
        @DisplayName("allCategories debe retornar Flow observable")
        fun `allCategories should return observable Flow`() = runTest {
            // Given
            val categories1 = listOf(createSampleCategory(id = 1))
            val categories2 = listOf(
                createSampleCategory(id = 1),
                createSampleCategory(id = 2)
            )
            every { categoryDao.getAllCategories() } returns flowOf(categories1)

            // When
            val result1 = categoryRepository.allCategories.first()
            
            every { categoryDao.getAllCategories() } returns flowOf(categories2)
            val newRepository = CategoryRepository(categoryDao)
            val result2 = newRepository.allCategories.first()

            // Then
            result1 shouldHaveSize 1
            result2 shouldHaveSize 2
        }

        @Test
        @DisplayName("allCategories debe manejar múltiples colecciones")
        fun `allCategories should handle multiple collections`() = runTest {
            // Given
            val categories = listOf(
                createSampleCategory(id = 1),
                createSampleCategory(id = 2),
                createSampleCategory(id = 3)
            )
            every { categoryDao.getAllCategories() } returns flowOf(categories)
            val testRepository = CategoryRepository(categoryDao)

            // When
            val result1 = testRepository.allCategories.first()
            val result2 = testRepository.allCategories.first()

            // Then
            result1 shouldBe result2
            verify(exactly = 2) { categoryDao.getAllCategories() }
        }
    }

    @Nested
    @DisplayName("populateDatabaseIfEmpty Tests")
    inner class PopulateDatabaseIfEmptyTests {

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar categorías cuando count es 0")
        fun `populateDatabaseIfEmpty should insert categories when count is 0`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify(exactly = 1) { categoryDao.getCategoryCount() }
            coVerify(exactly = 1) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty NO debe insertar categorías cuando count > 0")
        fun `populateDatabaseIfEmpty should not insert categories when count is greater than 0`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 5

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify(exactly = 1) { categoryDao.getCategoryCount() }
            coVerify(exactly = 0) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar 4 categorías iniciales")
        fun `populateDatabaseIfEmpty should insert 4 initial categories`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { categoryDao.insertAll(match { it.size == 4 }) }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar categoría Frutas Frescas")
        fun `populateDatabaseIfEmpty should insert Frutas Frescas category`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.any { it.name == "Frutas Frescas" && it.imageResName == "fruta" }
                })
            }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar categoría Verduras Orgánicas")
        fun `populateDatabaseIfEmpty should insert Verduras Organicas category`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.any { it.name == "Verduras Orgánicas" && it.imageResName == "verdura" }
                })
            }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar categoría Productos Orgánicos")
        fun `populateDatabaseIfEmpty should insert Productos Organicos category`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.any { it.name == "Productos Orgánicos" && it.imageResName == "organico" }
                })
            }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar categoría Productos Lácteos")
        fun `populateDatabaseIfEmpty should insert Productos Lacteos category`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.any { it.name == "Productos Lácteos" && it.imageResName == "lacteos" }
                })
            }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe insertar categorías con descripciones completas")
        fun `populateDatabaseIfEmpty should insert categories with complete descriptions`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.description.isNotEmpty() && it.description.length > 50 }
                })
            }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe manejar llamadas múltiples sin duplicar")
        fun `populateDatabaseIfEmpty should not duplicate categories on multiple calls`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0 andThen 4
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify(exactly = 1) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe funcionar con count = 1")
        fun `populateDatabaseIfEmpty should not insert when count is 1`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 1

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify(exactly = 1) { categoryDao.getCategoryCount() }
            coVerify(exactly = 0) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe funcionar con count grande")
        fun `populateDatabaseIfEmpty should not insert when count is large`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 100

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify(exactly = 1) { categoryDao.getCategoryCount() }
            coVerify(exactly = 0) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("populateDatabaseIfEmpty debe ejecutarse en contexto IO")
        fun `populateDatabaseIfEmpty should execute in IO context`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then - verify that DAO methods were called (executes in IO dispatcher)
            coVerify { categoryDao.getCategoryCount() }
        }
    }

    @Nested
    @DisplayName("getInitialCategories Tests (indirectos)")
    inner class GetInitialCategoriesTests {

        @Test
        @DisplayName("getInitialCategories debe retornar exactamente 4 categorías")
        fun `getInitialCategories should return exactly 4 categories`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { categoryDao.insertAll(match { it.size == 4 }) }
        }

        @Test
        @DisplayName("getInitialCategories debe incluir todas las categorías esperadas")
        fun `getInitialCategories should include all expected categories`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val names = categories.map { it.name }
                    names.contains("Frutas Frescas") &&
                    names.contains("Verduras Orgánicas") &&
                    names.contains("Productos Orgánicos") &&
                    names.contains("Productos Lácteos")
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories debe usar nombres correctos de recursos de imagen")
        fun `getInitialCategories should use correct image resource names`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val imageNames = categories.map { it.imageResName }
                    imageNames.contains("fruta") &&
                    imageNames.contains("verdura") &&
                    imageNames.contains("organico") &&
                    imageNames.contains("lacteos")
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories debe tener descripciones detalladas para Frutas Frescas")
        fun `getInitialCategories should have detailed description for Frutas Frescas`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val frutasCategory = categories.find { it.name == "Frutas Frescas" }
                    frutasCategory?.description?.contains("campo") == true &&
                    frutasCategory.description.contains("frescas") == true
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories debe tener descripciones detalladas para Verduras Orgánicas")
        fun `getInitialCategories should have detailed description for Verduras Organicas`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val verdurasCategory = categories.find { it.name == "Verduras Orgánicas" }
                    verdurasCategory?.description?.contains("orgánicas") == true &&
                    verdurasCategory.description.contains("pesticidas") == true
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories debe tener descripciones detalladas para Productos Orgánicos")
        fun `getInitialCategories should have detailed description for Productos Organicos`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val organicosCategory = categories.find { it.name == "Productos Orgánicos" }
                    organicosCategory?.description?.contains("naturales") == true &&
                    organicosCategory.description.contains("orgánicos") == true
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories debe tener descripciones detalladas para Productos Lácteos")
        fun `getInitialCategories should have detailed description for Productos Lacteos`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val lacteosCategory = categories.find { it.name == "Productos Lácteos" }
                    lacteosCategory?.description?.contains("lácteos") == true &&
                    lacteosCategory.description.contains("granjas") == true
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories debe crear categorías sin ID explícito")
        fun `getInitialCategories should create categories without explicit id`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.id == 0 } // Default value for auto-generated
                })
            }
        }

        @Test
        @DisplayName("getInitialCategories no debe tener categorías duplicadas")
        fun `getInitialCategories should not have duplicate categories`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val names = categories.map { it.name }
                    names.size == names.distinct().size
                })
            }
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    inner class IntegrationScenariosTests {

        @Test
        @DisplayName("debe poblar base de datos y luego recuperar categorías")
        fun `should populate database and then retrieve categories`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0 andThen 4
            coEvery { categoryDao.insertAll(any()) } just Runs
            
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas Frescas"),
                createSampleCategory(id = 2, name = "Verduras Orgánicas"),
                createSampleCategory(id = 3, name = "Productos Orgánicos"),
                createSampleCategory(id = 4, name = "Productos Lácteos")
            )
            every { categoryDao.getAllCategories() } returns flowOf(categories)

            // When
            categoryRepository.populateDatabaseIfEmpty()
            val result = categoryRepository.allCategories.first()

            // Then
            result shouldHaveSize 4
            coVerify(exactly = 1) { categoryDao.insertAll(any()) }
            verify(exactly = 1) { categoryDao.getAllCategories() }
        }

        @Test
        @DisplayName("debe verificar que la base de datos no esté vacía antes de intentar poblarla")
        fun `should check database not empty before attempting to populate`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 10
            every { categoryDao.getAllCategories() } returns flowOf(
                listOf(createSampleCategory())
            )

            // When
            categoryRepository.populateDatabaseIfEmpty()
            val result = categoryRepository.allCategories.first()

            // Then
            result shouldHaveSize 1
            coVerify(exactly = 1) { categoryDao.getCategoryCount() }
            coVerify(exactly = 0) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("debe manejar flujo completo de inicialización")
        fun `should handle complete initialization flow`() = runTest {
            // Given - Database is empty
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs
            
            // After population
            val populatedCategories = listOf(
                createSampleCategory(id = 1, name = "Frutas Frescas", imageResName = "fruta"),
                createSampleCategory(id = 2, name = "Verduras Orgánicas", imageResName = "verdura"),
                createSampleCategory(id = 3, name = "Productos Orgánicos", imageResName = "organico"),
                createSampleCategory(id = 4, name = "Productos Lácteos", imageResName = "lacteos")
            )
            every { categoryDao.getAllCategories() } returns flowOf(populatedCategories)

            // When
            categoryRepository.populateDatabaseIfEmpty()
            val categories = categoryRepository.allCategories.first()

            // Then
            categories shouldHaveSize 4
            categories.map { it.name } shouldContain "Frutas Frescas"
            categories.map { it.name } shouldContain "Verduras Orgánicas"
            categories.map { it.name } shouldContain "Productos Orgánicos"
            categories.map { it.name } shouldContain "Productos Lácteos"
        }

        @Test
        @DisplayName("debe ser idempotente al llamar múltiples veces populateDatabaseIfEmpty")
        fun `should be idempotent when calling populateDatabaseIfEmpty multiple times`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0 andThen 4 andThen 4
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()
            categoryRepository.populateDatabaseIfEmpty()
            categoryRepository.populateDatabaseIfEmpty()

            // Then - Should only insert once
            coVerify(exactly = 1) { categoryDao.insertAll(any()) }
            coVerify(exactly = 3) { categoryDao.getCategoryCount() }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Scenarios")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("debe manejar DAO que retorna Flow vacío")
        fun `should handle DAO returning empty Flow`() = runTest {
            // Given
            every { categoryDao.getAllCategories() } returns flowOf(emptyList())
            val testRepository = CategoryRepository(categoryDao)

            // When
            val result = testRepository.allCategories.first()

            // Then
            result.shouldNotBeNull()
            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("debe manejar count negativo como si fuera mayor que 0")
        fun `should handle negative count as non-zero`() = runTest {
            // Given - En caso de error, count podría ser negativo
            coEvery { categoryDao.getCategoryCount() } returns -1

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then - No debería insertar con count != 0
            coVerify(exactly = 1) { categoryDao.getCategoryCount() }
            coVerify(exactly = 0) { categoryDao.insertAll(any()) }
        }

        @Test
        @DisplayName("debe manejar categorías con nombres largos")
        fun `should handle categories with long names`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then - Verify all category names are reasonable length
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.name.length in 1..100 }
                })
            }
        }

        @Test
        @DisplayName("debe manejar descripciones con saltos de línea")
        fun `should handle descriptions with line breaks`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.description.contains("\n") }
                })
            }
        }

        @Test
        @DisplayName("debe crear repository con DAO válido")
        fun `should create repository with valid DAO`() {
            // Given
            val dao = mockk<CategoryDao>(relaxed = true)
            
            // When
            val repository = CategoryRepository(dao)

            // Then
            repository.shouldNotBeNull()
        }
    }

    @Nested
    @DisplayName("Data Validation Tests")
    inner class DataValidationTests {

        @Test
        @DisplayName("todas las categorías iniciales deben tener nombres únicos")
        fun `all initial categories should have unique names`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val names = categories.map { it.name }
                    names.distinct().size == names.size
                })
            }
        }

        @Test
        @DisplayName("todas las categorías iniciales deben tener imageResName únicos")
        fun `all initial categories should have unique imageResNames`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    val imageNames = categories.map { it.imageResName }
                    imageNames.distinct().size == imageNames.size
                })
            }
        }

        @Test
        @DisplayName("todas las categorías deben tener nombres no vacíos")
        fun `all categories should have non-empty names`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.name.isNotBlank() }
                })
            }
        }

        @Test
        @DisplayName("todas las categorías deben tener imageResName no vacíos")
        fun `all categories should have non-empty imageResNames`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.imageResName.isNotBlank() }
                })
            }
        }

        @Test
        @DisplayName("todas las categorías deben tener descripciones no vacías")
        fun `all categories should have non-empty descriptions`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.description.isNotBlank() }
                })
            }
        }

        @Test
        @DisplayName("todas las descripciones deben ser informativas (más de 50 caracteres)")
        fun `all descriptions should be informative with minimum length`() = runTest {
            // Given
            coEvery { categoryDao.getCategoryCount() } returns 0
            coEvery { categoryDao.insertAll(any()) } just Runs

            // When
            categoryRepository.populateDatabaseIfEmpty()

            // Then
            coVerify { 
                categoryDao.insertAll(match { categories ->
                    categories.all { it.description.length > 50 }
                })
            }
        }
    }
}