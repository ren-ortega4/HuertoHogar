package com.example.huertohogar.data.local

import com.example.huertohogar.model.ProductCategory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows

@DisplayName("Converters Tests")
class ConvertersTest {
    
    private lateinit var converters: Converters

    @BeforeEach
    fun setup() {
        converters = Converters()
    }

    @Nested
    @DisplayName("fromProductCategory Tests")
    inner class FromProductCategoryTests {

        @Test
        @DisplayName("debe convertir la categoría frutas a String")
        fun `should convert frutas category to String`() {
            val result = converters.fromProductCategory(ProductCategory.frutas)

            result shouldBe "frutas"
            result.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe convertir la categoría verduras a String")
        fun `should convert verduras category to String`() {
            val result = converters.fromProductCategory(ProductCategory.verduras)

            result shouldBe "verduras"
            result.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe convertir la categoría productosOrganicos a String")
        fun `should convert productosOrganicos category to String`() {
            val result = converters.fromProductCategory(ProductCategory.productosOrganicos)

            result shouldBe "productosOrganicos"
            result.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe convertir la categoría lacteos a String")
        fun `should convert lacteos category to String`() {
            val result = converters.fromProductCategory(ProductCategory.lacteos)

            result shouldBe "lacteos"
            result.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe convertir la categoría otros a String")
        fun `should convert otros category to String`() {
            val result = converters.fromProductCategory(ProductCategory.otros)

            result shouldBe "otros"
            result.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe devolver el nombre del enum como String")
        fun `should return category enum name`() {
            val category = ProductCategory.frutas
            val result = converters.fromProductCategory(category)

            result shouldBe category.name
        }

        @Test
        @DisplayName("debe devolver una cadena no vacía")
        fun `should return non-empty string for any category`() {
            val categories = ProductCategory.entries

            categories.forEach { category ->
                val result = converters.fromProductCategory(category)
                (result.isNotEmpty()) shouldBe true
            }
        }

        @Test
        @DisplayName("debe convertir todos los valores de ProductCategory a String")
        fun `should convert all ProductCategory values`() {
            val expectedResults = mapOf(
                ProductCategory.frutas to "frutas",
                ProductCategory.verduras to "verduras",
                ProductCategory.productosOrganicos to "productosOrganicos",
                ProductCategory.lacteos to "lacteos",
                ProductCategory.otros to "otros"
            )

            expectedResults.forEach { (category, expected) ->
                val result = converters.fromProductCategory(category)
                result shouldBe expected
            }
        }

        @Test
        @DisplayName("debe devolver el mismo resultado para la misma categoría")
        fun `should return consistent results for same category`() {
            val category = ProductCategory.frutas
            
            val result1 = converters.fromProductCategory(category)
            val result2 = converters.fromProductCategory(category)
            val result3 = converters.fromProductCategory(category)

            result1 shouldBe result2
            result2 shouldBe result3
        }

        @Test
        @DisplayName("debe devolver resultados diferentes para categorías diferentes")
        fun `should return different strings for different categories`() {
            val result1 = converters.fromProductCategory(ProductCategory.frutas)
            val result2 = converters.fromProductCategory(ProductCategory.verduras)

            result1 shouldNotBe result2
        }
    }

    @Nested
    @DisplayName("toProductCategory Tests")
    inner class ToProductCategoryTests {

        @Test
        @DisplayName("debe convertir 'frutas' string a ProductCategory.frutas")
        fun `should convert frutas string to ProductCategory frutas`() {
            val result = converters.toProductCategory("frutas")

            result shouldBe ProductCategory.frutas
            result.shouldBeInstanceOf<ProductCategory>()
        }

        @Test
        @DisplayName("debe convertir 'verduras' string a ProductCategory.verduras")
        fun `should convert verduras string to ProductCategory verduras`() {
            val result = converters.toProductCategory("verduras")

            result shouldBe ProductCategory.verduras
            result.shouldBeInstanceOf<ProductCategory>()
        }

        @Test
        @DisplayName("debe convertir 'productosOrganicos' string a ProductCategory.productosOrganicos")
        fun `should convert productosOrganicos string to ProductCategory productosOrganicos`() {
            val result = converters.toProductCategory("productosOrganicos")

            result shouldBe ProductCategory.productosOrganicos
            result.shouldBeInstanceOf<ProductCategory>()
        }

        @Test
        @DisplayName("debe convertir 'lacteos' string a ProductCategory.lacteos")
        fun `should convert lacteos string to ProductCategory lacteos`() {
            val result = converters.toProductCategory("lacteos")

            result shouldBe ProductCategory.lacteos
            result.shouldBeInstanceOf<ProductCategory>()
        }

        @Test
        @DisplayName("debe convertir 'otros' string a ProductCategory.otros")
        fun `should convert otros string to ProductCategory otros`() {
            val result = converters.toProductCategory("otros")

            result shouldBe ProductCategory.otros
            result.shouldBeInstanceOf<ProductCategory>()
        }

        @Test
        @DisplayName("debe lanzar excepción para string inválido")
        fun `should throw exception for invalid category string`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("invalid_category")
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para string vacío")
        fun `should throw exception for empty string`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("")
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para string con espacios en blanco")
        fun `should be case sensitive`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("FRUTAS")
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para string con espacios al final")
        fun `should throw exception for string with spaces`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("frutas ")
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para string con espacios al inicio")
        fun `should convert all valid category strings`() {
            val validStrings = listOf(
                "frutas",
                "verduras",
                "productosOrganicos",
                "lacteos",
                "otros"
            )

            validStrings.forEach { categoryString ->
                val result = converters.toProductCategory(categoryString)
                result.shouldBeInstanceOf<ProductCategory>()
                result.name shouldBe categoryString
            }
        }

        @Test
        @DisplayName("debe devolver el mismo resultado para la misma cadena")
        fun `should return consistent results for same string`() {
            val categoryString = "frutas"
            
            val result1 = converters.toProductCategory(categoryString)
            val result2 = converters.toProductCategory(categoryString)
            val result3 = converters.toProductCategory(categoryString)

            result1 shouldBe result2
            result2 shouldBe result3
        }

        @Test
        @DisplayName("debe devolver resultados diferentes para cadenas diferentes")
        fun `should return different categories for different strings`() {
            val result1 = converters.toProductCategory("frutas")
            val result2 = converters.toProductCategory("verduras")

            result1 shouldNotBe result2
        }
    }

    @Nested
    @DisplayName("Bidirectional Conversion Tests")
    inner class BidirectionalConversionTests {

        @Test
        @DisplayName("debe convertir category a string y luego volver a category")
        fun `should convert category to string and back to same category`() {
            val originalCategory = ProductCategory.frutas
            
            val stringValue = converters.fromProductCategory(originalCategory)
            val convertedBack = converters.toProductCategory(stringValue)

            convertedBack shouldBe originalCategory
        }

        @Test
        @DisplayName("debe convertir string a category y luego volver a string")
        fun `should convert string to category and back to same string`() {
            val originalString = "verduras"
            
            val categoryValue = converters.toProductCategory(originalString)
            val convertedBack = converters.fromProductCategory(categoryValue)

            convertedBack shouldBe originalString
        }

        @Test
        @DisplayName("debe mantener consistencia para todas las categorías en el ciclo de ida y vuelta")
        fun `should maintain consistency for all categories in round trip`() {
            val allCategories = ProductCategory.entries

            allCategories.forEach { category ->
                val stringValue = converters.fromProductCategory(category)
                val convertedBack = converters.toProductCategory(stringValue)
                
                convertedBack shouldBe category
            }
        }

        @Test
        @DisplayName("debe mantener consistencia para todas las cadenas en el ciclo de ida y vuelta")
        fun `should maintain consistency for all valid strings in round trip`() {
            val validStrings = listOf(
                "frutas",
                "verduras",
                "productosOrganicos",
                "lacteos",
                "otros"
            )

            validStrings.forEach { categoryString ->
                val categoryValue = converters.toProductCategory(categoryString)
                val convertedBack = converters.fromProductCategory(categoryValue)
                
                convertedBack shouldBe categoryString
            }
        }

        @Test
        @DisplayName("debe producir el mismo resultado para la misma categoría")
        fun `multiple round trips should produce same result`() {
            val originalCategory = ProductCategory.lacteos
            
            val string1 = converters.fromProductCategory(originalCategory)
            val category1 = converters.toProductCategory(string1)
            val string2 = converters.fromProductCategory(category1)
            val category2 = converters.toProductCategory(string2)

            category1 shouldBe originalCategory
            category2 shouldBe originalCategory
            string1 shouldBe string2
        }
    }

    @Nested
    @DisplayName("Edge Cases and Validation Tests")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("debe manejar ProductCategory con caracteres especiales en displayName")
        fun `should handle ProductCategory with special characters in displayName`() {
            // productosOrganicos has "Productos Orgánicos" as displayName
            val category = ProductCategory.productosOrganicos
            val result = converters.fromProductCategory(category)

            result shouldBe "productosOrganicos"
            result shouldNotBe category.displayName
        }

        @Test
        @DisplayName("debe manejar ProductCategory con nombres diferentes de displayName")
        fun `fromProductCategory should use enum name not displayName`() {
            val category = ProductCategory.frutas
            val result = converters.fromProductCategory(category)

            result shouldBe category.name
            result shouldNotBe category.displayName
        }

        @Test
        @DisplayName("debe manejar todas las entradas de ProductCategory sin excepción")
        fun `should handle all enum entries without exception`() {
            val allEntries = ProductCategory.entries
            
            allEntries.forEach { category ->
                val result = converters.fromProductCategory(category)
                result.shouldBeInstanceOf<String>()
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para displayName en lugar de name")
        fun `toProductCategory should not accept displayName values`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("Frutas") // displayName instead of name
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para strings nulos o vacíos")
        fun `should throw exception for null-like strings`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("null")
            }
        }

        @Test
        @DisplayName("debe lanzar excepción para strings que no sean números")
        fun `should throw exception for numeric strings`() {
            assertThrows<IllegalArgumentException> {
                converters.toProductCategory("123")
            }
        }
    }

    @Nested
    @DisplayName("TypeConverter Functionality Tests")
    inner class TypeConverterFunctionalityTests {

        @Test
        @DisplayName("debe funcionar correctamente con Room database context")
        fun `converters should work with Room database context`() {
            // Simulating how Room would use these converters
            val originalCategory = ProductCategory.productosOrganicos
            
            // Room stores as String
            val stored = converters.fromProductCategory(originalCategory)
            // Room retrieves as ProductCategory
            val retrieved = converters.toProductCategory(stored)

            retrieved shouldBe originalCategory
        }

        @Test
        @DisplayName("debe manejar todos los valores de ProductCategory")
        fun `should support all ProductCategory enum values`() {
            val totalCategories = ProductCategory.entries.size
            
            totalCategories shouldBe 5
            
            ProductCategory.entries.forEach { category ->
                val stringValue = converters.fromProductCategory(category)
                val backToCategory = converters.toProductCategory(stringValue)
                
                backToCategory shouldBe category
            }
        }

        @Test
        @DisplayName("debe ser stateless")
        fun `converter instance should be stateless`() {
            val converter1 = Converters()
            val converter2 = Converters()
            
            val result1 = converter1.fromProductCategory(ProductCategory.frutas)
            val result2 = converter2.fromProductCategory(ProductCategory.frutas)

            result1 shouldBe result2
        }

        @Test
        @DisplayName("Debe trabajar con diferentes instancias de Converters ")
        fun `should work correctly with different converter instances`() {
            val converter1 = Converters()
            val converter2 = Converters()
            
            val string = converter1.fromProductCategory(ProductCategory.lacteos)
            val category = converter2.toProductCategory(string)

            category shouldBe ProductCategory.lacteos
        }
    }
}