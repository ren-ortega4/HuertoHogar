package com.example.huertohogar.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductTest {

    @Test
    fun `crear producto con todos los parametros`() {
        // Given
        val id = 1
        val name = "Manzana"
        val price = "5.50"
        val imagesRes = 12345
        val category = ProductCategory.frutas

        // When
        val product = Product(id, name, price, imagesRes, category)

        // Then
        assertEquals(id, product.id)
        assertEquals(name, product.name)
        assertEquals(price, product.price)
        assertEquals(imagesRes, product.imagesRes)
        assertEquals(category, product.category)
    }

    @Test
    fun `crear producto sin especificar id debe tener valor 0 por defecto`() {
        // Given
        val name = "Tomate"
        val price = "3.00"
        val imagesRes = 54321
        val category = ProductCategory.verduras

        // When
        val product = Product(name = name, price = price, imagesRes = imagesRes, category = category)

        // Then
        assertEquals(0, product.id)
        assertEquals(name, product.name)
        assertEquals(price, product.price)
        assertEquals(imagesRes, product.imagesRes)
        assertEquals(category, product.category)
    }

    @Test
    fun `crear producto con categoria frutas`() {
        // Given
        val product = Product(1, "Naranja", "4.00", 111, ProductCategory.frutas)

        // Then
        assertEquals(ProductCategory.frutas, product.category)
    }

    @Test
    fun `crear producto con categoria verduras`() {
        // Given
        val product = Product(2, "Lechuga", "2.50", 222, ProductCategory.verduras)

        // Then
        assertEquals(ProductCategory.verduras, product.category)
    }

    @Test
    fun `crear producto con categoria productosOrganicos`() {
        // Given
        val product = Product(3, "Quinoa", "8.00", 333, ProductCategory.productosOrganicos)

        // Then
        assertEquals(ProductCategory.productosOrganicos, product.category)
    }

    @Test
    fun `crear producto con categoria lacteos`() {
        // Given
        val product = Product(4, "Leche", "3.50", 444, ProductCategory.lacteos)

        // Then
        assertEquals(ProductCategory.lacteos, product.category)
    }

    @Test
    fun `crear producto con categoria otros`() {
        // Given
        val product = Product(5, "Pan", "2.00", 555, ProductCategory.otros)

        // Then
        assertEquals(ProductCategory.otros, product.category)
    }

    @Test
    fun `crear producto con precio decimal`() {
        // Given
        val product = Product(6, "Zanahoria", "1.99", 666, ProductCategory.verduras)

        // Then
        assertEquals("1.99", product.price)
    }

    @Test
    fun `crear producto con precio entero`() {
        // Given
        val product = Product(7, "Papa", "5", 777, ProductCategory.verduras)

        // Then
        assertEquals("5", product.price)
    }

    @Test
    fun `crear producto con precio cero`() {
        // Given
        val product = Product(8, "Muestra", "0", 888, ProductCategory.otros)

        // Then
        assertEquals("0", product.price)
    }

    @Test
    fun `crear producto con nombre largo`() {
        // Given
        val name = "Manzana roja orgánica cultivada en huerto sostenible"
        val product = Product(9, name, "10.00", 999, ProductCategory.productosOrganicos)

        // Then
        assertEquals(name, product.name)
    }

    @Test
    fun `crear producto con nombre corto`() {
        // Given
        val name = "Ajo"
        val product = Product(10, name, "1.50", 1010, ProductCategory.verduras)

        // Then
        assertEquals(name, product.name)
    }

    @Test
    fun `crear producto con id negativo`() {
        // Given
        val product = Product(-1, "Test", "5.00", 1111, ProductCategory.otros)

        // Then
        assertEquals(-1, product.id)
    }

    @Test
    fun `dos productos con los mismos valores son iguales`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)

        // Then
        assertEquals(product1, product2)
    }

    @Test
    fun `dos productos con diferentes ids no son iguales`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(2, "Manzana", "5.00", 123, ProductCategory.frutas)

        // Then
        assertNotEquals(product1, product2)
    }

    @Test
    fun `dos productos con diferentes nombres no son iguales`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(1, "Pera", "5.00", 123, ProductCategory.frutas)

        // Then
        assertNotEquals(product1, product2)
    }

    @Test
    fun `dos productos con diferentes precios no son iguales`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(1, "Manzana", "6.00", 123, ProductCategory.frutas)

        // Then
        assertNotEquals(product1, product2)
    }

    @Test
    fun `dos productos con diferentes imagenes no son iguales`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(1, "Manzana", "5.00", 456, ProductCategory.frutas)

        // Then
        assertNotEquals(product1, product2)
    }

    @Test
    fun `dos productos con diferentes categorias no son iguales`() {
        // Given
        val product1 = Product(1, "Producto", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(1, "Producto", "5.00", 123, ProductCategory.verduras)

        // Then
        assertNotEquals(product1, product2)
    }

    @Test
    fun `dos productos iguales tienen el mismo hashCode`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)
        val product2 = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)

        // Then
        assertEquals(product1.hashCode(), product2.hashCode())
    }

    @Test
    fun `toString debe contener todos los valores`() {
        // Given
        val product = Product(1, "Manzana", "5.00", 123, ProductCategory.frutas)

        // When
        val resultado = product.toString()

        // Then
        assertTrue(resultado.contains("1"))
        assertTrue(resultado.contains("Manzana"))
        assertTrue(resultado.contains("5.00"))
        assertTrue(resultado.contains("123"))
    }

    @Test
    fun `copy debe crear una nueva instancia con los mismos valores`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val copia = original.copy()

        // Then
        assertEquals(original, copia)
        assertNotSame(original, copia)
    }

    @Test
    fun `copy con cambio de id`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val modificado = original.copy(id = 99)

        // Then
        assertEquals(99, modificado.id)
        assertEquals(original.name, modificado.name)
        assertEquals(original.price, modificado.price)
        assertEquals(original.imagesRes, modificado.imagesRes)
        assertEquals(original.category, modificado.category)
    }

    @Test
    fun `copy con cambio de nombre`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val modificado = original.copy(name = "Pimiento")

        // Then
        assertEquals(original.id, modificado.id)
        assertEquals("Pimiento", modificado.name)
        assertEquals(original.price, modificado.price)
    }

    @Test
    fun `copy con cambio de precio`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val modificado = original.copy(price = "4.50")

        // Then
        assertEquals(original.id, modificado.id)
        assertEquals("4.50", modificado.price)
    }

    @Test
    fun `copy con cambio de imagen`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val modificado = original.copy(imagesRes = 999)

        // Then
        assertEquals(999, modificado.imagesRes)
    }

    @Test
    fun `copy con cambio de categoria`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val modificado = original.copy(category = ProductCategory.productosOrganicos)

        // Then
        assertEquals(ProductCategory.productosOrganicos, modificado.category)
    }

    @Test
    fun `copy con cambio de todos los parametros`() {
        // Given
        val original = Product(1, "Tomate", "3.00", 789, ProductCategory.verduras)

        // When
        val modificado = original.copy(
            id = 50,
            name = "Fresa",
            price = "7.00",
            imagesRes = 5000,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals(50, modificado.id)
        assertEquals("Fresa", modificado.name)
        assertEquals("7.00", modificado.price)
        assertEquals(5000, modificado.imagesRes)
        assertEquals(ProductCategory.frutas, modificado.category)
    }

    // Tests para ProductCategory enum

    @Test
    fun `ProductCategory frutas tiene displayName correcto`() {
        assertEquals("Frutas", ProductCategory.frutas.displayName)
    }

    @Test
    fun `ProductCategory verduras tiene displayName correcto`() {
        assertEquals("Verduras", ProductCategory.verduras.displayName)
    }

    @Test
    fun `ProductCategory productosOrganicos tiene displayName correcto`() {
        assertEquals("Productos Orgánicos", ProductCategory.productosOrganicos.displayName)
    }

    @Test
    fun `ProductCategory lacteos tiene displayName correcto`() {
        assertEquals("Lácteos", ProductCategory.lacteos.displayName)
    }

    @Test
    fun `ProductCategory otros tiene displayName correcto`() {
        assertEquals("Otros", ProductCategory.otros.displayName)
    }

    @Test
    fun `ProductCategory values debe retornar todas las categorias`() {
        // When
        val categories = ProductCategory.values()

        // Then
        assertEquals(5, categories.size)
        assertTrue(categories.contains(ProductCategory.frutas))
        assertTrue(categories.contains(ProductCategory.verduras))
        assertTrue(categories.contains(ProductCategory.productosOrganicos))
        assertTrue(categories.contains(ProductCategory.lacteos))
        assertTrue(categories.contains(ProductCategory.otros))
    }

    @Test
    fun `ProductCategory valueOf debe retornar categoria correcta`() {
        assertEquals(ProductCategory.frutas, ProductCategory.valueOf("frutas"))
        assertEquals(ProductCategory.verduras, ProductCategory.valueOf("verduras"))
        assertEquals(ProductCategory.productosOrganicos, ProductCategory.valueOf("productosOrganicos"))
        assertEquals(ProductCategory.lacteos, ProductCategory.valueOf("lacteos"))
        assertEquals(ProductCategory.otros, ProductCategory.valueOf("otros"))
    }

    @Test
    fun `ProductCategory valueOf debe lanzar excepcion con nombre invalido`() {
        assertThrows<IllegalArgumentException> {
            ProductCategory.valueOf("invalido")
        }
    }

    @Test
    fun `ProductCategory ordinal debe ser correcto`() {
        assertEquals(0, ProductCategory.frutas.ordinal)
        assertEquals(1, ProductCategory.verduras.ordinal)
        assertEquals(2, ProductCategory.productosOrganicos.ordinal)
        assertEquals(3, ProductCategory.lacteos.ordinal)
        assertEquals(4, ProductCategory.otros.ordinal)
    }

    @Test
    fun `ProductCategory name debe ser correcto`() {
        assertEquals("frutas", ProductCategory.frutas.name)
        assertEquals("verduras", ProductCategory.verduras.name)
        assertEquals("productosOrganicos", ProductCategory.productosOrganicos.name)
        assertEquals("lacteos", ProductCategory.lacteos.name)
        assertEquals("otros", ProductCategory.otros.name)
    }

    @Test
    fun `crear producto con caracteres especiales en nombre`() {
        // Given
        val name = "Piña-Colada & Coco (100%)"
        val product = Product(11, name, "8.50", 1212, ProductCategory.frutas)

        // Then
        assertEquals(name, product.name)
    }

    @Test
    fun `crear producto con precio con muchos decimales`() {
        // Given
        val product = Product(12, "Especias", "12.9999", 1313, ProductCategory.otros)

        // Then
        assertEquals("12.9999", product.price)
    }
}