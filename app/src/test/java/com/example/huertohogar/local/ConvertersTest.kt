package com.example.huertohogar.local

import com.example.huertohogar.data.local.Converters
import com.example.huertohogar.model.ProductCategory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ConvertersTest {

    private lateinit var converters: Converters

    @BeforeEach
    fun setUp() {
        converters = Converters()
    }

    @Test
    fun `fromProductCategory should convert frutas to string`() {
        // Given
        val category = ProductCategory.frutas

        // When
        val result = converters.fromProductCategory(category)

        // Then
        assertEquals("frutas", result)
    }

    @Test
    fun `fromProductCategory should convert verduras to string`() {
        // Given
        val category = ProductCategory.verduras

        // When
        val result = converters.fromProductCategory(category)

        // Then
        assertEquals("verduras", result)
    }

    @Test
    fun `fromProductCategory should convert productosOrganicos to string`() {
        // Given
        val category = ProductCategory.productosOrganicos

        // When
        val result = converters.fromProductCategory(category)

        // Then
        assertEquals("productosOrganicos", result)
    }

    @Test
    fun `fromProductCategory should convert lacteos to string`() {
        // Given
        val category = ProductCategory.lacteos

        // When
        val result = converters.fromProductCategory(category)

        // Then
        assertEquals("lacteos", result)
    }

    @Test
    fun `fromProductCategory should convert otros to string`() {
        // Given
        val category = ProductCategory.otros

        // When
        val result = converters.fromProductCategory(category)

        // Then
        assertEquals("otros", result)
    }

    @Test
    fun `toProductCategory should convert string to frutas`() {
        // Given
        val categoryString = "frutas"

        // When
        val result = converters.toProductCategory(categoryString)

        // Then
        assertEquals(ProductCategory.frutas, result)
    }

    @Test
    fun `toProductCategory should convert string to verduras`() {
        // Given
        val categoryString = "verduras"

        // When
        val result = converters.toProductCategory(categoryString)

        // Then
        assertEquals(ProductCategory.verduras, result)
    }

    @Test
    fun `toProductCategory should convert string to productosOrganicos`() {
        // Given
        val categoryString = "productosOrganicos"

        // When
        val result = converters.toProductCategory(categoryString)

        // Then
        assertEquals(ProductCategory.productosOrganicos, result)
    }

    @Test
    fun `toProductCategory should convert string to lacteos`() {
        // Given
        val categoryString = "lacteos"

        // When
        val result = converters.toProductCategory(categoryString)

        // Then
        assertEquals(ProductCategory.lacteos, result)
    }

    @Test
    fun `toProductCategory should convert string to otros`() {
        // Given
        val categoryString = "otros"

        // When
        val result = converters.toProductCategory(categoryString)

        // Then
        assertEquals(ProductCategory.otros, result)
    }

    @Test
    fun `toProductCategory should throw exception for invalid string`() {
        // Given
        val invalidString = "categoriaInvalida"

        // When & Then
        assertThrows<IllegalArgumentException> {
            converters.toProductCategory(invalidString)
        }
    }

    @Test
    fun `conversion should be reversible for all categories`() {
        // Given
        val categories = listOf(
            ProductCategory.frutas,
            ProductCategory.verduras,
            ProductCategory.productosOrganicos,
            ProductCategory.lacteos,
            ProductCategory.otros
        )

        // When & Then
        categories.forEach { category ->
            val string = converters.fromProductCategory(category)
            val backToCategory = converters.toProductCategory(string)
            assertEquals(category, backToCategory)
        }
    }

    @Test
    fun `fromProductCategory should return category name`() {
        // Given
        val category = ProductCategory.frutas

        // When
        val result = converters.fromProductCategory(category)

        // Then
        assertEquals(category.name, result)
    }

    @Test
    fun `toProductCategory should handle case sensitive strings`() {
        // Given
        val uppercaseString = "FRUTAS"

        // When & Then
        assertThrows<IllegalArgumentException> {
            converters.toProductCategory(uppercaseString)
        }
    }

    @Test
    fun `toProductCategory should handle empty string`() {
        // Given
        val emptyString = ""

        // When & Then
        assertThrows<IllegalArgumentException> {
            converters.toProductCategory(emptyString)
        }
    }
}
