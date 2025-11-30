package com.example.huertohogar.view.screen

import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Tests para ProductScreen")
class ProductScreenTest {

    @Nested
    @DisplayName("Tests del Data Class Product")
    inner class `Tests del Data Class Product` {

        @Test
        fun `debería crear un producto con todos los campos requeridos`() {
            val producto = Product(
                id = 1,
                name = "Manzana",
                price = "$1.500",
                imagesRes = 123,
                category = ProductCategory.frutas
            )

            producto.id shouldBe 1
            producto.name shouldBe "Manzana"
            producto.price shouldBe "$1.500"
            producto.imagesRes shouldBe 123
            producto.category shouldBe ProductCategory.frutas
        }

        @Test
        fun `debería crear un producto con id autogenerado en 0`() {
            val producto = Product(
                name = "Lechuga",
                price = "$800",
                imagesRes = 456,
                category = ProductCategory.verduras
            )

            producto.id shouldBe 0
        }

        @Test
        fun `debería permitir diferentes categorías de productos`() {
            val producto1 = Product(name = "Naranja", price = "$2.000", imagesRes = 1, category = ProductCategory.frutas)
            val producto2 = Product(name = "Tomate", price = "$1.200", imagesRes = 2, category = ProductCategory.verduras)
            val producto3 = Product(name = "Leche", price = "$1.500", imagesRes = 3, category = ProductCategory.lacteos)
            val producto4 = Product(name = "Quinoa", price = "$3.000", imagesRes = 4, category = ProductCategory.productosOrganicos)
            val producto5 = Product(name = "Mermelada", price = "$2.500", imagesRes = 5, category = ProductCategory.otros)

            producto1.category shouldBe ProductCategory.frutas
            producto2.category shouldBe ProductCategory.verduras
            producto3.category shouldBe ProductCategory.lacteos
            producto4.category shouldBe ProductCategory.productosOrganicos
            producto5.category shouldBe ProductCategory.otros
        }

        @Test
        fun `debería manejar nombres de productos con caracteres especiales`() {
            val producto = Product(
                name = "Manzana Roja Orgánica 100% Natural",
                price = "$2.500",
                imagesRes = 789,
                category = ProductCategory.productosOrganicos
            )

            producto.name shouldBe "Manzana Roja Orgánica 100% Natural"
            producto.name.shouldContain("Orgánica")
            producto.name.shouldContain("100%")
        }

        @Test
        fun `debería manejar precios con formato chileno`() {
            val producto1 = Product(name = "Producto1", price = "$1.500", imagesRes = 1, category = ProductCategory.frutas)
            val producto2 = Product(name = "Producto2", price = "$10.000", imagesRes = 2, category = ProductCategory.verduras)
            val producto3 = Product(name = "Producto3", price = "$999", imagesRes = 3, category = ProductCategory.otros)

            producto1.price shouldBe "$1.500"
            producto2.price shouldBe "$10.000"
            producto3.price shouldBe "$999"
        }

        @Test
        fun `debería permitir precios con decimales`() {
            val producto = Product(
                name = "Miel",
                price = "$4.990",
                imagesRes = 100,
                category = ProductCategory.otros
            )

            producto.price shouldBe "$4.990"
        }

        @Test
        fun `debería soportar recursos de imagen diferentes`() {
            val producto1 = Product(name = "Producto1", price = "$1.000", imagesRes = 100, category = ProductCategory.frutas)
            val producto2 = Product(name = "Producto2", price = "$1.000", imagesRes = 200, category = ProductCategory.verduras)

            producto1.imagesRes shouldBe 100
            producto2.imagesRes shouldBe 200
            producto1.imagesRes shouldNotBe producto2.imagesRes
        }
    }

    @Nested
    @DisplayName("Tests de Lógica del Negocio")
    inner class `Tests de Lógica del Negocio` {

        @Test
        fun `debería comparar productos por igualdad correctamente`() {
            val producto1 = Product(id = 1, name = "Manzana", price = "$1.500", imagesRes = 123, category = ProductCategory.frutas)
            val producto2 = Product(id = 1, name = "Manzana", price = "$1.500", imagesRes = 123, category = ProductCategory.frutas)
            val producto3 = Product(id = 2, name = "Manzana", price = "$1.500", imagesRes = 123, category = ProductCategory.frutas)

            (producto1 == producto2) shouldBe true
            (producto1 == producto3) shouldBe false
        }

        @Test
        fun `debería generar hashCode consistente para productos iguales`() {
            val producto1 = Product(id = 1, name = "Pera", price = "$1.800", imagesRes = 456, category = ProductCategory.frutas)
            val producto2 = Product(id = 1, name = "Pera", price = "$1.800", imagesRes = 456, category = ProductCategory.frutas)

            producto1.hashCode() shouldBe producto2.hashCode()
        }

        @Test
        fun `debería generar toString informativo`() {
            val producto = Product(id = 5, name = "Zanahoria", price = "$900", imagesRes = 789, category = ProductCategory.verduras)
            val resultado = producto.toString()

            resultado.shouldContain("Product")
            resultado.shouldContain("id=5")
            resultado.shouldContain("name=Zanahoria")
            resultado.shouldContain("price=\$900")
            resultado.shouldContain("imagesRes=789")
            resultado.shouldContain("verduras")
        }

        @Test
        fun `debería permitir copiar producto con cambios`() {
            val producto = Product(id = 1, name = "Original", price = "$1.000", imagesRes = 100, category = ProductCategory.frutas)
            val productoCopia = producto.copy(name = "Modificado", price = "$2.000")

            productoCopia.id shouldBe 1
            productoCopia.name shouldBe "Modificado"
            productoCopia.price shouldBe "$2.000"
            productoCopia.imagesRes shouldBe 100
            productoCopia.category shouldBe ProductCategory.frutas
        }

        @Test
        fun `debería permitir filtrar lista de productos por categoría`() {
            val productos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.verduras),
                crearProducto(id = 3, categoria = ProductCategory.frutas),
                crearProducto(id = 4, categoria = ProductCategory.lacteos)
            )

            val frutas = productos.filter { it.category == ProductCategory.frutas }

            frutas shouldHaveSize 2
            frutas.all { it.category == ProductCategory.frutas } shouldBe true
        }

        @Test
        fun `debería permitir ordenar productos por nombre`() {
            val productos = listOf(
                crearProducto(nombre = "Zanahoria"),
                crearProducto(nombre = "Manzana"),
                crearProducto(nombre = "Lechuga")
            )

            val ordenados = productos.sortedBy { it.name }

            ordenados[0].name shouldBe "Lechuga"
            ordenados[1].name shouldBe "Manzana"
            ordenados[2].name shouldBe "Zanahoria"
        }

        @Test
        fun `debería permitir ordenar productos por precio`() {
            val productos = listOf(
                crearProducto(precio = "$3.000"),
                crearProducto(precio = "$1.000"),
                crearProducto(precio = "$2.000")
            )

            val ordenados = productos.sortedBy { it.price }

            ordenados[0].price shouldBe "$1.000"
            ordenados[1].price shouldBe "$2.000"
            ordenados[2].price shouldBe "$3.000"
        }

        @Test
        fun `debería permitir agrupar productos por categoría`() {
            val productos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.frutas),
                crearProducto(id = 3, categoria = ProductCategory.verduras)
            )

            val agrupados = productos.groupBy { it.category }

            agrupados.keys shouldHaveSize 2
            agrupados[ProductCategory.frutas]?.shouldHaveSize(2)
            agrupados[ProductCategory.verduras]?.shouldHaveSize(1)
        }

        @Test
        fun `debería permitir buscar productos por nombre`() {
            val productos = listOf(
                crearProducto(nombre = "Manzana Roja"),
                crearProducto(nombre = "Manzana Verde"),
                crearProducto(nombre = "Pera")
            )

            val encontrados = productos.filter { it.name.contains("Manzana", ignoreCase = true) }

            encontrados shouldHaveSize 2
            encontrados.all { it.name.contains("Manzana") } shouldBe true
        }

        @Test
        fun `debería permitir contar productos por categoría`() {
            val productos = listOf(
                crearProducto(categoria = ProductCategory.frutas),
                crearProducto(categoria = ProductCategory.frutas),
                crearProducto(categoria = ProductCategory.frutas),
                crearProducto(categoria = ProductCategory.verduras)
            )

            val frutasCount = productos.count { it.category == ProductCategory.frutas }
            val verdurasCount = productos.count { it.category == ProductCategory.verduras }

            frutasCount shouldBe 3
            verdurasCount shouldBe 1
        }

        @Test
        fun `debería validar que el nombre del producto no esté vacío`() {
            val producto = crearProducto(nombre = "Producto Válido")

            producto.name.shouldNotBeEmpty()
        }

        @Test
        fun `debería validar que el precio del producto no esté vacío`() {
            val producto = crearProducto(precio = "$1.500")

            producto.price.shouldNotBeEmpty()
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Datos")
    inner class `Tests de Validación de Datos` {

        @Test
        fun `debería manejar productos con nombres muy largos`() {
            val nombreLargo = "A".repeat(200)
            val producto = Product(
                name = nombreLargo,
                price = "$1.000",
                imagesRes = 123,
                category = ProductCategory.frutas
            )

            producto.name shouldBe nombreLargo
            producto.name.length shouldBe 200
        }

        @Test
        fun `debería manejar productos con precios muy altos`() {
            val producto = Product(
                name = "Producto Premium",
                price = "$999.999.999",
                imagesRes = 123,
                category = ProductCategory.otros
            )

            producto.price shouldBe "$999.999.999"
        }

        @Test
        fun `debería manejar productos con id negativos`() {
            val producto = Product(
                id = -1,
                name = "Producto Test",
                price = "$1.000",
                imagesRes = 123,
                category = ProductCategory.frutas
            )

            producto.id shouldBe -1
        }

        @Test
        fun `debería manejar productos con imagesRes negativos`() {
            val producto = Product(
                name = "Producto Sin Imagen",
                price = "$1.000",
                imagesRes = -1,
                category = ProductCategory.frutas
            )

            producto.imagesRes shouldBe -1
        }

        @Test
        fun `debería permitir crear múltiples productos con mismo nombre pero diferentes ids`() {
            val producto1 = Product(id = 1, name = "Manzana", price = "$1.500", imagesRes = 123, category = ProductCategory.frutas)
            val producto2 = Product(id = 2, name = "Manzana", price = "$1.500", imagesRes = 123, category = ProductCategory.frutas)

            producto1.name shouldBe producto2.name
            producto1.id shouldNotBe producto2.id
        }

        @Test
        fun `debería permitir cambiar categoría usando copy`() {
            val producto = crearProducto(categoria = ProductCategory.frutas)
            val productoModificado = producto.copy(category = ProductCategory.verduras)

            producto.category shouldBe ProductCategory.frutas
            productoModificado.category shouldBe ProductCategory.verduras
        }

        @Test
        fun `debería permitir cambiar precio usando copy`() {
            val producto = crearProducto(precio = "$1.000")
            val productoModificado = producto.copy(price = "$2.000")

            producto.price shouldBe "$1.000"
            productoModificado.price shouldBe "$2.000"
        }

        @Test
        fun `debería permitir cambiar imagen usando copy`() {
            val producto = crearProducto(imagenRes = 100)
            val productoModificado = producto.copy(imagesRes = 200)

            producto.imagesRes shouldBe 100
            productoModificado.imagesRes shouldBe 200
        }
    }

    @Nested
    @DisplayName("Tests de Casos Extremos")
    inner class `Tests de Casos Extremos` {

        @Test
        fun `debería manejar lista vacía de productos`() {
            val productos = emptyList<Product>()

            productos shouldHaveSize 0
            productos.isEmpty() shouldBe true
        }

        @Test
        fun `debería manejar lista con un solo producto`() {
            val productos = listOf(crearProducto())

            productos shouldHaveSize 1
            productos.first().name shouldBe "Producto Test"
        }

        @Test
        fun `debería manejar lista con muchos productos`() {
            val productos = (1..1000).map { crearProducto(id = it) }

            productos shouldHaveSize 1000
            productos.first().id shouldBe 1
            productos.last().id shouldBe 1000
        }

        @Test
        fun `debería permitir productos con todos los campos iguales excepto id`() {
            val producto1 = Product(id = 1, name = "Igual", price = "$1.000", imagesRes = 123, category = ProductCategory.frutas)
            val producto2 = Product(id = 2, name = "Igual", price = "$1.000", imagesRes = 123, category = ProductCategory.frutas)

            producto1.name shouldBe producto2.name
            producto1.price shouldBe producto2.price
            producto1.imagesRes shouldBe producto2.imagesRes
            producto1.category shouldBe producto2.category
            producto1.id shouldNotBe producto2.id
        }

        @Test
        fun `debería manejar búsqueda en lista vacía`() {
            val productos = emptyList<Product>()
            val resultado = productos.filter { it.category == ProductCategory.frutas }

            resultado shouldHaveSize 0
        }

        @Test
        fun `debería manejar productos con nombres con espacios múltiples`() {
            val producto = Product(
                name = "Manzana    Verde    Orgánica",
                price = "$2.000",
                imagesRes = 123,
                category = ProductCategory.productosOrganicos
            )

            producto.name shouldBe "Manzana    Verde    Orgánica"
        }

        @Test
        fun `debería manejar productos con precios sin símbolo de pesos`() {
            val producto = Product(
                name = "Producto",
                price = "1500",
                imagesRes = 123,
                category = ProductCategory.frutas
            )

            producto.price shouldBe "1500"
        }

        @Test
        fun `debería permitir crear set de productos sin duplicados`() {
            val producto = crearProducto(id = 1)
            val productos = setOf(producto, producto, producto)

            productos shouldHaveSize 1
        }

        @Test
        fun `debería permitir verificar existencia de producto en colección`() {
            val producto1 = crearProducto(id = 1, nombre = "Manzana")
            val producto2 = crearProducto(id = 2, nombre = "Pera")
            val productos = listOf(producto1, producto2)

            productos shouldContain producto1
            productos shouldContain producto2
        }

        @Test
        fun `debería permitir verificar no existencia de producto en colección`() {
            val producto1 = crearProducto(id = 1)
            val producto2 = crearProducto(id = 2)
            val productos = listOf(producto1)

            productos shouldNotContain producto2
        }

        @Test
        fun `debería manejar null safety en operaciones con colecciones`() {
            val productos = listOf(
                crearProducto(id = 1, nombre = "Manzana"),
                crearProducto(id = 2, nombre = "Pera")
            )

            val encontrado = productos.find { it.name == "Manzana" }
            val noEncontrado = productos.find { it.name == "Inexistente" }

            encontrado shouldNotBe null
            encontrado?.name shouldBe "Manzana"
            noEncontrado shouldBe null
        }

        @Test
        fun `debería permitir mapear productos a sus nombres`() {
            val productos = listOf(
                crearProducto(nombre = "Manzana"),
                crearProducto(nombre = "Pera"),
                crearProducto(nombre = "Naranja")
            )

            val nombres = productos.map { it.name }

            nombres shouldHaveSize 3
            nombres shouldContain "Manzana"
            nombres shouldContain "Pera"
            nombres shouldContain "Naranja"
        }

        @Test
        fun `debería permitir filtrar productos por rango de precio`() {
            val productos = listOf(
                crearProducto(precio = "$500"),
                crearProducto(precio = "$1.500"),
                crearProducto(precio = "$3.000")
            )

            val filtrados = productos.filter { 
                val precioNumerico = it.price.replace("$", "").replace(".", "").toIntOrNull() ?: 0
                precioNumerico in 1000..2000
            }

            filtrados shouldHaveSize 1
            filtrados.first().price shouldBe "$1.500"
        }

        @Test
        fun `debería permitir obtener categorías únicas de productos`() {
            val productos = listOf(
                crearProducto(categoria = ProductCategory.frutas),
                crearProducto(categoria = ProductCategory.frutas),
                crearProducto(categoria = ProductCategory.verduras),
                crearProducto(categoria = ProductCategory.lacteos)
            )

            val categoriasUnicas = productos.map { it.category }.distinct()

            categoriasUnicas shouldHaveSize 3
            categoriasUnicas shouldContain ProductCategory.frutas
            categoriasUnicas shouldContain ProductCategory.verduras
            categoriasUnicas shouldContain ProductCategory.lacteos
        }

        @Test
        fun `debería manejar productos con imagesRes 0`() {
            val producto = Product(
                name = "Producto Sin Imagen",
                price = "$1.000",
                imagesRes = 0,
                category = ProductCategory.frutas
            )

            producto.imagesRes shouldBe 0
        }
    }

    // Función auxiliar para crear productos de prueba
    private fun crearProducto(
        id: Int = 0,
        nombre: String = "Producto Test",
        precio: String = "$1.500",
        imagenRes: Int = 123,
        categoria: ProductCategory = ProductCategory.frutas
    ): Product {
        return Product(
            id = id,
            name = nombre,
            price = precio,
            imagesRes = imagenRes,
            category = categoria
        )
    }
}