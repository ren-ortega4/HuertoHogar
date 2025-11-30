package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class CategoryEntityTest {

    @Test
    fun propiedadesCategoryEstanCorrectas() {
        val category = CategoryEntity(
            name = "Frutas",
            imageResName = "frutas.png",
            description = "Categoría de frutas"
        )

        assertEquals(0, category.id)
        assertEquals("Frutas", category.name)
        assertEquals("frutas.png", category.imageResName)
        assertEquals("Categoría de frutas", category.description)
    }

    @Test
    fun igualdadYCopiaDeCategory() {
        val c1 = CategoryEntity(id = 1, name = "A", imageResName = "i.png", description = "D")
        val c2 = CategoryEntity(id = 1, name = "A", imageResName = "i.png", description = "D")
        assertEquals(c1, c2)

        val c3 = c1.copy(id = 2)
        assertNotEquals(c1, c3)
        assertEquals(2, c3.id)
    }

    @Test
    fun idPorDefectoEsCero() {
        val cat = CategoryEntity(name = "X", imageResName = "y.png", description = "desc")
        assertEquals(0, cat.id)
    }
}