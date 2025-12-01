package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class ProductTest {

    @Test
    fun propiedadesProductoEstanCorrectas() {
        val product = Product(
            name = "Manzana",
            price = 350.5,
            priceLabel = "$",
            imagesRes = 123,
            category = ProductCategory.frutas
        )

        assertEquals(0, product.id)
        assertEquals("Manzana", product.name)
        assertEquals(350.5, product.price, 0.0001)
        assertEquals("$", product.priceLabel)
        assertEquals(123, product.imagesRes)
        assertEquals(ProductCategory.frutas, product.category)
        assertEquals("Frutas", product.category.displayName)
    }

    @Test
    fun igualdadYCopiaDeProducto() {
        val p1 = Product(id = 1, name = "A", price = 1.0, priceLabel = "$", imagesRes = 1, category = ProductCategory.otros)
        val p2 = Product(id = 1, name = "A", price = 1.0, priceLabel = "$", imagesRes = 1, category = ProductCategory.otros)
        assertEquals(p1, p2)

        val p3 = p1.copy(id = 2)
        assertNotEquals(p1, p3)
        assertEquals(2, p3.id)
    }

    @Test
    fun idPorDefectoEsCero() {
        val product = Product(name = "X", price = 0.0, priceLabel = "", imagesRes = 0, category = ProductCategory.otros)
        assertEquals(0, product.id)
    }
}