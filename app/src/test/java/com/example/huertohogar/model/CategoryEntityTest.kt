package com.example.huertohogar.model

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("CategoryEntity Tests")
class CategoryEntityTest {
    
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
    @DisplayName("Constructor and Properties Tests")
    inner class ConstructorTests {

        @Test
        @DisplayName("should create CategoryEntity with all properties")
        fun `should create CategoryEntity with all properties`() {
            val category = CategoryEntity(
                id = 1,
                name = "Frutas",
                imageResName = "frutas_icon",
                description = "Frutas frescas"
            )

            category.id shouldBe 1
            category.name shouldBe "Frutas"
            category.imageResName shouldBe "frutas_icon"
            category.description shouldBe "Frutas frescas"
        }

        @Test
        @DisplayName("should create CategoryEntity with default id zero")
        fun `should create CategoryEntity with default id zero`() {
            val category = CategoryEntity(
                name = "Verduras",
                imageResName = "verduras_icon",
                description = "Verduras frescas"
            )

            category.id shouldBe 0
            category.name shouldBe "Verduras"
        }

        @Test
        @DisplayName("should create CategoryEntity with auto-generated id")
        fun `should create CategoryEntity with auto-generated id`() {
            val category = createSampleCategory(id = 0)

            category.id shouldBe 0
        }

        @Test
        @DisplayName("should create CategoryEntity with specific id")
        fun `should create CategoryEntity with specific id`() {
            val category = createSampleCategory(id = 100)

            category.id shouldBe 100
        }

        @Test
        @DisplayName("should store name property correctly")
        fun `should store name property correctly`() {
            val category = createSampleCategory(name = "Lácteos")

            category.name shouldBe "Lácteos"
        }

        @Test
        @DisplayName("should store imageResName property correctly")
        fun `should store imageResName property correctly`() {
            val category = createSampleCategory(imageResName = "lacteos_icon")

            category.imageResName shouldBe "lacteos_icon"
        }

        @Test
        @DisplayName("should store description property correctly")
        fun `should store description property correctly`() {
            val category = createSampleCategory(description = "Productos lácteos naturales")

            category.description shouldBe "Productos lácteos naturales"
        }
    }

    @Nested
    @DisplayName("Data Class Functionality Tests")
    inner class DataClassFunctionalityTests {

        @Test
        @DisplayName("should support copy with modified id")
        fun `should support copy with modified id`() {
            val category = createSampleCategory(id = 1)
            val copied = category.copy(id = 2)

            copied.id shouldBe 2
            copied.name shouldBe category.name
            category.id shouldBe 1 // Original unchanged
        }

        @Test
        @DisplayName("should support copy with modified name")
        fun `should support copy with modified name`() {
            val category = createSampleCategory(name = "Original")
            val copied = category.copy(name = "Modificado")

            copied.name shouldBe "Modificado"
            copied.id shouldBe category.id
            category.name shouldBe "Original" // Original unchanged
        }

        @Test
        @DisplayName("should support copy with modified imageResName")
        fun `should support copy with modified imageResName`() {
            val category = createSampleCategory(imageResName = "icon_old")
            val copied = category.copy(imageResName = "icon_new")

            copied.imageResName shouldBe "icon_new"
            category.imageResName shouldBe "icon_old" // Original unchanged
        }

        @Test
        @DisplayName("should support copy with modified description")
        fun `should support copy with modified description`() {
            val category = createSampleCategory(description = "Old description")
            val copied = category.copy(description = "New description")

            copied.description shouldBe "New description"
            category.description shouldBe "Old description" // Original unchanged
        }

        @Test
        @DisplayName("should support copy with multiple modified properties")
        fun `should support copy with multiple modified properties`() {
            val category = createSampleCategory(id = 1, name = "Original")
            val copied = category.copy(id = 10, name = "Nuevo")

            copied.id shouldBe 10
            copied.name shouldBe "Nuevo"
            category.id shouldBe 1
            category.name shouldBe "Original"
        }

        @Test
        @DisplayName("should support equals comparison")
        fun `should support equals comparison`() {
            val category1 = createSampleCategory(id = 1, name = "Frutas")
            val category2 = createSampleCategory(id = 1, name = "Frutas")

            (category1 == category2) shouldBe true
        }

        @Test
        @DisplayName("should not be equal with different id")
        fun `should not be equal with different id`() {
            val category1 = createSampleCategory(id = 1)
            val category2 = createSampleCategory(id = 2)

            (category1 == category2) shouldBe false
        }

        @Test
        @DisplayName("should not be equal with different name")
        fun `should not be equal with different name`() {
            val category1 = createSampleCategory(name = "Frutas")
            val category2 = createSampleCategory(name = "Verduras")

            (category1 == category2) shouldBe false
        }

        @Test
        @DisplayName("should not be equal with different imageResName")
        fun `should not be equal with different imageResName`() {
            val category1 = createSampleCategory(imageResName = "icon1")
            val category2 = createSampleCategory(imageResName = "icon2")

            (category1 == category2) shouldBe false
        }

        @Test
        @DisplayName("should not be equal with different description")
        fun `should not be equal with different description`() {
            val category1 = createSampleCategory(description = "Desc 1")
            val category2 = createSampleCategory(description = "Desc 2")

            (category1 == category2) shouldBe false
        }

        @Test
        @DisplayName("should support hashCode")
        fun `should support hashCode`() {
            val category1 = createSampleCategory(id = 1, name = "Frutas")
            val category2 = createSampleCategory(id = 1, name = "Frutas")

            category1.hashCode() shouldBe category2.hashCode()
        }

        @Test
        @DisplayName("should have different hashCode for different categories")
        fun `should have different hashCode for different categories`() {
            val category1 = createSampleCategory(id = 1)
            val category2 = createSampleCategory(id = 2)

            category1.hashCode() shouldNotBe category2.hashCode()
        }

        @Test
        @DisplayName("should support toString")
        fun `should support toString`() {
            val category = createSampleCategory(name = "Test Category")

            val toString = category.toString()

            toString.shouldBeInstanceOf<String>()
            toString.contains("CategoryEntity") shouldBe true
        }

        @Test
        @DisplayName("should support destructuring")
        fun `should support destructuring`() {
            val category = createSampleCategory(
                id = 5,
                name = "Test",
                imageResName = "icon",
                description = "Desc"
            )

            val (id, name, imageResName, description) = category

            id shouldBe 5
            name shouldBe "Test"
            imageResName shouldBe "icon"
            description shouldBe "Desc"
        }

        @Test
        @DisplayName("should be immutable through copy")
        fun `should be immutable through copy`() {
            val original = createSampleCategory(name = "Original")
            val modified = original.copy(name = "Modified")

            original.name shouldBe "Original"
            modified.name shouldBe "Modified"
        }
    }

    @Nested
    @DisplayName("Property Validation Tests")
    inner class PropertyValidationTests {

        @Test
        @DisplayName("should handle empty name")
        fun `should handle empty name`() {
            val category = createSampleCategory(name = "")

            category.name shouldBe ""
        }

        @Test
        @DisplayName("should handle empty imageResName")
        fun `should handle empty imageResName`() {
            val category = createSampleCategory(imageResName = "")

            category.imageResName shouldBe ""
        }

        @Test
        @DisplayName("should handle empty description")
        fun `should handle empty description`() {
            val category = createSampleCategory(description = "")

            category.description shouldBe ""
        }

        @Test
        @DisplayName("should handle name with special characters")
        fun `should handle name with special characters`() {
            val category = createSampleCategory(name = "Frutas & Verduras 100%")

            category.name shouldBe "Frutas & Verduras 100%"
        }

        @Test
        @DisplayName("should handle name with unicode characters")
        fun `should handle name with unicode characters`() {
            val category = createSampleCategory(name = "Lácteos Orgánicos")

            category.name shouldBe "Lácteos Orgánicos"
        }

        @Test
        @DisplayName("should handle name with numbers")
        fun `should handle name with numbers`() {
            val category = createSampleCategory(name = "Categoría 123")

            category.name shouldBe "Categoría 123"
        }

        @Test
        @DisplayName("should handle very long name")
        fun `should handle very long name`() {
            val longName = "Nombre de categoría muy largo ".repeat(10)
            val category = createSampleCategory(name = longName)

            category.name shouldBe longName
        }

        @Test
        @DisplayName("should handle very long description")
        fun `should handle very long description`() {
            val longDescription = "Descripción muy larga ".repeat(20)
            val category = createSampleCategory(description = longDescription)

            category.description shouldBe longDescription
        }

        @Test
        @DisplayName("should handle imageResName with underscores")
        fun `should handle imageResName with underscores`() {
            val category = createSampleCategory(imageResName = "frutas_y_verduras_icon")

            category.imageResName shouldBe "frutas_y_verduras_icon"
        }

        @Test
        @DisplayName("should handle imageResName with numbers")
        fun `should handle imageResName with numbers`() {
            val category = createSampleCategory(imageResName = "icon_123")

            category.imageResName shouldBe "icon_123"
        }

        @Test
        @DisplayName("should handle negative id")
        fun `should handle negative id`() {
            val category = createSampleCategory(id = -1)

            category.id shouldBe -1
        }

        @Test
        @DisplayName("should handle maximum integer id")
        fun `should handle maximum integer id`() {
            val category = createSampleCategory(id = Int.MAX_VALUE)

            category.id shouldBe Int.MAX_VALUE
        }

        @Test
        @DisplayName("should handle description with newlines")
        fun `should handle description with newlines`() {
            val description = "Primera línea\nSegunda línea\nTercera línea"
            val category = createSampleCategory(description = description)

            category.description shouldBe description
        }

        @Test
        @DisplayName("should handle description with special characters")
        fun `should handle description with special characters`() {
            val description = "Descripción con símbolos: @#$%^&*()"
            val category = createSampleCategory(description = description)

            category.description shouldBe description
        }
    }

    @Nested
    @DisplayName("Real-world Scenarios Tests")
    inner class RealWorldScenariosTests {

        @Test
        @DisplayName("should create Frutas category")
        fun `should create Frutas category`() {
            val category = CategoryEntity(
                id = 1,
                name = "Frutas",
                imageResName = "ic_frutas",
                description = "Frutas frescas de temporada"
            )

            category.name shouldBe "Frutas"
            category.imageResName shouldBe "ic_frutas"
        }

        @Test
        @DisplayName("should create Verduras category")
        fun `should create Verduras category`() {
            val category = CategoryEntity(
                id = 2,
                name = "Verduras",
                imageResName = "ic_verduras",
                description = "Verduras orgánicas y frescas"
            )

            category.name shouldBe "Verduras"
            category.imageResName shouldBe "ic_verduras"
        }

        @Test
        @DisplayName("should create Lácteos category")
        fun `should create Lácteos category`() {
            val category = CategoryEntity(
                id = 3,
                name = "Lácteos",
                imageResName = "ic_lacteos",
                description = "Productos lácteos naturales"
            )

            category.name shouldBe "Lácteos"
            category.description shouldBe "Productos lácteos naturales"
        }

        @Test
        @DisplayName("should create Productos Orgánicos category")
        fun `should create Productos Orgánicos category`() {
            val category = CategoryEntity(
                id = 4,
                name = "Productos Orgánicos",
                imageResName = "ic_organicos",
                description = "Productos 100% orgánicos certificados"
            )

            category.name shouldBe "Productos Orgánicos"
            category.description.contains("100%") shouldBe true
        }

        @Test
        @DisplayName("should create Otros category")
        fun `should create Otros category`() {
            val category = CategoryEntity(
                id = 5,
                name = "Otros",
                imageResName = "ic_otros",
                description = "Otros productos disponibles"
            )

            category.name shouldBe "Otros"
            category.id shouldBe 5
        }

        @Test
        @DisplayName("should create category for marketplace section")
        fun `should create category for marketplace section`() {
            val category = CategoryEntity(
                id = 0,
                name = "Productos del Huerto",
                imageResName = "ic_huerto",
                description = "Productos frescos directamente del huerto"
            )

            category.id shouldBe 0
            category.name shouldBe "Productos del Huerto"
        }

        @Test
        @DisplayName("should handle category with accent marks in Spanish")
        fun `should handle category with accent marks in Spanish`() {
            val category = CategoryEntity(
                id = 10,
                name = "Legumbres y Granos Básicos",
                imageResName = "ic_legumbres",
                description = "Lentejas, frijoles, garbanzos y más"
            )

            category.name.contains("á") shouldBe true
        }
    }

    @Nested
    @DisplayName("Room Entity Compatibility Tests")
    inner class RoomEntityCompatibilityTests {

        @Test
        @DisplayName("should have primary key id")
        fun `should have primary key id`() {
            val category = createSampleCategory(id = 42)

            category.id shouldBe 42
        }

        @Test
        @DisplayName("should support auto-generation with id = 0")
        fun `should support auto-generation with id = 0`() {
            val category = createSampleCategory(id = 0)

            category.id shouldBe 0
        }

        @Test
        @DisplayName("should work with sequential ids")
        fun `should work with sequential ids`() {
            val categories = (1..10).map { id ->
                createSampleCategory(id = id, name = "Category $id")
            }

            categories.forEachIndexed { index, category ->
                category.id shouldBe index + 1
            }
        }

        @Test
        @DisplayName("should maintain data integrity for database storage")
        fun `should maintain data integrity for database storage`() {
            val category = createSampleCategory(
                id = 100,
                name = "Test Category",
                imageResName = "test_icon",
                description = "Test Description"
            )

            // Simulate database round-trip
            val retrieved = category.copy()

            retrieved.id shouldBe category.id
            retrieved.name shouldBe category.name
            retrieved.imageResName shouldBe category.imageResName
            retrieved.description shouldBe category.description
        }

        @Test
        @DisplayName("should be suitable for Room entity operations")
        fun `should be suitable for Room entity operations`() {
            val category1 = createSampleCategory(id = 1)
            val category2 = createSampleCategory(id = 2)
            val category3 = createSampleCategory(id = 3)

            val categories = listOf(category1, category2, category3)

            categories.size shouldBe 3
            categories.map { it.id }.distinct().size shouldBe 3
        }
    }

    @Nested
    @DisplayName("Collection Operations Tests")
    inner class CollectionOperationsTests {

        @Test
        @DisplayName("should work in list of categories")
        fun `should work in list of categories`() {
            val categories = listOf(
                createSampleCategory(id = 1, name = "Cat 1"),
                createSampleCategory(id = 2, name = "Cat 2"),
                createSampleCategory(id = 3, name = "Cat 3")
            )

            categories.size shouldBe 3
            categories[0].name shouldBe "Cat 1"
            categories[2].name shouldBe "Cat 3"
        }

        @Test
        @DisplayName("should be filterable by name")
        fun `should be filterable by name`() {
            val categories = listOf(
                createSampleCategory(id = 1, name = "Frutas"),
                createSampleCategory(id = 2, name = "Verduras"),
                createSampleCategory(id = 3, name = "Frutas Tropicales")
            )

            val filtered = categories.filter { it.name.contains("Frutas") }

            filtered.size shouldBe 2
        }

        @Test
        @DisplayName("should be sortable by id")
        fun `should be sortable by id`() {
            val categories = listOf(
                createSampleCategory(id = 3),
                createSampleCategory(id = 1),
                createSampleCategory(id = 2)
            )

            val sorted = categories.sortedBy { it.id }

            sorted[0].id shouldBe 1
            sorted[1].id shouldBe 2
            sorted[2].id shouldBe 3
        }

        @Test
        @DisplayName("should be sortable by name")
        fun `should be sortable by name`() {
            val categories = listOf(
                createSampleCategory(name = "Verduras"),
                createSampleCategory(name = "Frutas"),
                createSampleCategory(name = "Lácteos")
            )

            val sorted = categories.sortedBy { it.name }

            sorted[0].name shouldBe "Frutas"
            sorted[1].name shouldBe "Lácteos"
            sorted[2].name shouldBe "Verduras"
        }

        @Test
        @DisplayName("should support finding by id")
        fun `should support finding by id`() {
            val categories = listOf(
                createSampleCategory(id = 1),
                createSampleCategory(id = 2),
                createSampleCategory(id = 3)
            )

            val found = categories.find { it.id == 2 }

            found?.id shouldBe 2
        }

        @Test
        @DisplayName("should support grouping by first letter of name")
        fun `should support grouping by first letter of name`() {
            val categories = listOf(
                createSampleCategory(name = "Frutas"),
                createSampleCategory(name = "Verduras"),
                createSampleCategory(name = "Frutos Secos")
            )

            val grouped = categories.groupBy { it.name.first() }

            grouped['F']?.size shouldBe 2
            grouped['V']?.size shouldBe 1
        }

        @Test
        @DisplayName("should support mapping to names")
        fun `should support mapping to names`() {
            val categories = listOf(
                createSampleCategory(name = "Frutas"),
                createSampleCategory(name = "Verduras")
            )

            val names = categories.map { it.name }

            names shouldBe listOf("Frutas", "Verduras")
        }

        @Test
        @DisplayName("should remove duplicates based on equality")
        fun `should remove duplicates based on equality`() {
            val category = createSampleCategory(id = 1, name = "Frutas")
            val categories = listOf(category, category.copy(), category)

            val unique = categories.distinct()

            unique.size shouldBe 1
        }
    }
}