package com.example.huertohogar.view.screen

import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("ProductByCategoryScreen Tests")
class ProductByCategoryScreenTest {

    @Nested
    @DisplayName("ProductCategory Enum Tests")
    inner class EnumProductCategoryTests {

        @Test
        @DisplayName("debe tener categoría frutas con displayName correcto")
        fun `debe tener categoria frutas con displayName correcto`() {
            val categoria = ProductCategory.frutas

            categoria.displayName shouldBe "Frutas"
        }

        @Test
        @DisplayName("debe tener categoría verduras con displayName correcto")
        fun `debe tener categoria verduras con displayName correcto`() {
            val categoria = ProductCategory.verduras

            categoria.displayName shouldBe "Verduras"
        }

        @Test
        @DisplayName("debe tener categoría productosOrganicos con displayName correcto")
        fun `debe tener categoria productosOrganicos con displayName correcto`() {
            val categoria = ProductCategory.productosOrganicos

            categoria.displayName shouldBe "Productos Orgánicos"
        }

        @Test
        @DisplayName("debe tener categoría lacteos con displayName correcto")
        fun `debe tener categoria lacteos con displayName correcto`() {
            val categoria = ProductCategory.lacteos

            categoria.displayName shouldBe "Lácteos"
        }

        @Test
        @DisplayName("debe tener categoría otros con displayName correcto")
        fun `debe tener categoria otros con displayName correcto`() {
            val categoria = ProductCategory.otros

            categoria.displayName shouldBe "Otros"
        }

        @Test
        @DisplayName("debe verificar que todas las categorías son del tipo ProductCategory")
        fun `debe verificar que todas las categorias son del tipo ProductCategory`() {
            ProductCategory.frutas.shouldBeInstanceOf<ProductCategory>()
            ProductCategory.verduras.shouldBeInstanceOf<ProductCategory>()
            ProductCategory.productosOrganicos.shouldBeInstanceOf<ProductCategory>()
            ProductCategory.lacteos.shouldBeInstanceOf<ProductCategory>()
            ProductCategory.otros.shouldBeInstanceOf<ProductCategory>()
        }

        @Test
        @DisplayName("debe verificar que displayName es un String")
        fun `debe verificar que displayName es un String`() {
            ProductCategory.frutas.displayName.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe tener exactamente 5 categorías")
        fun `debe tener exactamente 5 categorias`() {
            val categorias = ProductCategory.entries

            categorias shouldHaveSize 5
        }

        @Test
        @DisplayName("debe contener todas las categorías esperadas")
        fun `debe contener todas las categorias esperadas`() {
            val categorias = ProductCategory.entries

            categorias shouldContain ProductCategory.frutas
            categorias shouldContain ProductCategory.verduras
            categorias shouldContain ProductCategory.productosOrganicos
            categorias shouldContain ProductCategory.lacteos
            categorias shouldContain ProductCategory.otros
        }

        @Test
        @DisplayName("debe verificar que ningún displayName está vacío")
        fun `debe verificar que ningun displayName esta vacio`() {
            val categorias = ProductCategory.entries

            categorias.forEach { categoria ->
                categoria.displayName.shouldNotBeBlank()
                categoria.displayName.isNotEmpty() shouldBe true
            }
        }

        @Test
        @DisplayName("debe verificar que cada categoría tiene un displayName único")
        fun `debe verificar que cada categoria tiene un displayName unico`() {
            val displayNames = ProductCategory.entries.map { it.displayName }
            val uniqueNames = displayNames.toSet()

            displayNames.size shouldBe uniqueNames.size
        }

        @Test
        @DisplayName("debe poder obtener categoría por nombre de enum")
        fun `debe poder obtener categoria por nombre de enum`() {
            val categoria = ProductCategory.valueOf("frutas")

            categoria shouldBe ProductCategory.frutas
        }

        @Test
        @DisplayName("debe poder comparar categorías")
        fun `debe poder comparar categorias`() {
            val categoria1 = ProductCategory.frutas
            val categoria2 = ProductCategory.frutas
            val categoria3 = ProductCategory.verduras

            categoria1 shouldBe categoria2
            categoria1 shouldNotBe categoria3
        }

        @Test
        @DisplayName("debe poder usar categorías en expresiones when")
        fun `debe poder usar categorias en expresiones when`() {
            val resultado = when (ProductCategory.frutas) {
                ProductCategory.frutas -> "Es frutas"
                ProductCategory.verduras -> "Es verduras"
                ProductCategory.productosOrganicos -> "Es orgánicos"
                ProductCategory.lacteos -> "Es lácteos"
                ProductCategory.otros -> "Es otros"
            }

            resultado shouldBe "Es frutas"
        }

        @Test
        @DisplayName("debe poder iterar sobre todas las categorías")
        fun `debe poder iterar sobre todas las categorias`() {
            val nombres = mutableListOf<String>()

            for (categoria in ProductCategory.entries) {
                nombres.add(categoria.displayName)
            }

            nombres shouldHaveSize 5
            nombres shouldContainAll listOf("Frutas", "Verduras", "Productos Orgánicos", "Lácteos", "Otros")
        }

        @Test
        @DisplayName("debe mantener el orden de declaración de las categorías")
        fun `debe mantener el orden de declaracion de las categorias`() {
            val categorias = ProductCategory.entries.toList()

            categorias[0] shouldBe ProductCategory.frutas
            categorias[1] shouldBe ProductCategory.verduras
            categorias[2] shouldBe ProductCategory.productosOrganicos
            categorias[3] shouldBe ProductCategory.lacteos
            categorias[4] shouldBe ProductCategory.otros
        }

        @Test
        @DisplayName("debe poder obtener nombre del enum como string")
        fun `debe poder obtener nombre del enum como string`() {
            val nombre = ProductCategory.frutas.name

            nombre shouldBe "frutas"
        }

        @Test
        @DisplayName("debe poder obtener ordinal de cada categoría")
        fun `debe poder obtener ordinal de cada categoria`() {
            ProductCategory.frutas.ordinal shouldBe 0
            ProductCategory.verduras.ordinal shouldBe 1
            ProductCategory.productosOrganicos.ordinal shouldBe 2
            ProductCategory.lacteos.ordinal shouldBe 3
            ProductCategory.otros.ordinal shouldBe 4
        }

        @Test
        @DisplayName("debe verificar que displayName contiene caracteres válidos")
        fun `debe verificar que displayName contiene caracteres validos`() {
            val categorias = ProductCategory.entries

            categorias.forEach { categoria ->
                categoria.displayName.matches(Regex("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) shouldBe true
            }
        }
    }

    @Nested
    @DisplayName("Product Category Logic Tests")
    inner class LogicaCategoriasProductoTests {

        @Test
        @DisplayName("debe poder filtrar productos por categoría frutas")
        fun `debe poder filtrar productos por categoria frutas`() {
            val productos = listOf(
                crearProducto(id = 1, nombre = "Manzana", categoria = ProductCategory.frutas),
                crearProducto(id = 2, nombre = "Zanahoria", categoria = ProductCategory.verduras),
                crearProducto(id = 3, nombre = "Naranja", categoria = ProductCategory.frutas)
            )

            val frutas = productos.filter { it.category == ProductCategory.frutas }

            frutas shouldHaveSize 2
            frutas.all { it.category == ProductCategory.frutas } shouldBe true
        }

        @Test
        @DisplayName("debe poder filtrar productos por categoría verduras")
        fun `debe poder filtrar productos por categoria verduras`() {
            val productos = listOf(
                crearProducto(id = 1, nombre = "Manzana", categoria = ProductCategory.frutas),
                crearProducto(id = 2, nombre = "Zanahoria", categoria = ProductCategory.verduras),
                crearProducto(id = 3, nombre = "Lechuga", categoria = ProductCategory.verduras)
            )

            val verduras = productos.filter { it.category == ProductCategory.verduras }

            verduras shouldHaveSize 2
            verduras.all { it.category == ProductCategory.verduras } shouldBe true
        }

        @Test
        @DisplayName("debe poder agrupar productos por categoría")
        fun `debe poder agrupar productos por categoria`() {
            val productos = listOf(
                crearProducto(id = 1, nombre = "Manzana", categoria = ProductCategory.frutas),
                crearProducto(id = 2, nombre = "Zanahoria", categoria = ProductCategory.verduras),
                crearProducto(id = 3, nombre = "Naranja", categoria = ProductCategory.frutas),
                crearProducto(id = 4, nombre = "Leche", categoria = ProductCategory.lacteos)
            )

            val agrupados = productos.groupBy { it.category }

            agrupados[ProductCategory.frutas]?.shouldHaveSize(2)
            agrupados[ProductCategory.verduras]?.shouldHaveSize(1)
            agrupados[ProductCategory.lacteos]?.shouldHaveSize(1)
        }

        @Test
        @DisplayName("debe poder contar productos por categoría")
        fun `debe poder contar productos por categoria`() {
            val productos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.frutas),
                crearProducto(id = 3, categoria = ProductCategory.verduras)
            )

            val countFrutas = productos.count { it.category == ProductCategory.frutas }
            val countVerduras = productos.count { it.category == ProductCategory.verduras }

            countFrutas shouldBe 2
            countVerduras shouldBe 1
        }

        @Test
        @DisplayName("debe verificar si existe algún producto en una categoría")
        fun `debe verificar si existe algun producto en una categoria`() {
            val productos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.verduras)
            )

            val hayFrutas = productos.any { it.category == ProductCategory.frutas }
            val hayLacteos = productos.any { it.category == ProductCategory.lacteos }

            hayFrutas shouldBe true
            hayLacteos shouldBe false
        }

        @Test
        @DisplayName("debe poder obtener lista de categorías únicas de productos")
        fun `debe poder obtener lista de categorias unicas de productos`() {
            val productos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.frutas),
                crearProducto(id = 3, categoria = ProductCategory.verduras),
                crearProducto(id = 4, categoria = ProductCategory.lacteos)
            )

            val categoriasUnicas = productos.map { it.category }.distinct()

            categoriasUnicas shouldHaveSize 3
            categoriasUnicas shouldContain ProductCategory.frutas
            categoriasUnicas shouldContain ProductCategory.verduras
            categoriasUnicas shouldContain ProductCategory.lacteos
        }

        @Test
        @DisplayName("debe poder ordenar productos por categoría")
        fun `debe poder ordenar productos por categoria`() {
            val productos = listOf(
                crearProducto(id = 1, nombre = "Producto 1", categoria = ProductCategory.otros),
                crearProducto(id = 2, nombre = "Producto 2", categoria = ProductCategory.frutas),
                crearProducto(id = 3, nombre = "Producto 3", categoria = ProductCategory.verduras)
            )

            val ordenados = productos.sortedBy { it.category.ordinal }

            ordenados[0].category shouldBe ProductCategory.frutas
            ordenados[1].category shouldBe ProductCategory.verduras
            ordenados[2].category shouldBe ProductCategory.otros
        }

        @Test
        @DisplayName("debe poder buscar productos por displayName de categoría")
        fun `debe poder buscar productos por displayName de categoria`() {
            val productos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.verduras)
            )

            val productosFrutas = productos.filter { 
                it.category.displayName == "Frutas" 
            }

            productosFrutas shouldHaveSize 1
            productosFrutas.first().category shouldBe ProductCategory.frutas
        }

        @Test
        @DisplayName("debe manejar lista vacía de productos")
        fun `debe manejar lista vacia de productos`() {
            val productos = emptyList<Product>()

            val frutas = productos.filter { it.category == ProductCategory.frutas }

            frutas shouldHaveSize 0
        }

        @Test
        @DisplayName("debe poder verificar si todos los productos son de la misma categoría")
        fun `debe poder verificar si todos los productos son de la misma categoria`() {
            val todosFrutas = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.frutas)
            )

            val mixtos = listOf(
                crearProducto(id = 1, categoria = ProductCategory.frutas),
                crearProducto(id = 2, categoria = ProductCategory.verduras)
            )

            todosFrutas.all { it.category == ProductCategory.frutas } shouldBe true
            mixtos.all { it.category == ProductCategory.frutas } shouldBe false
        }
    }

    @Nested
    @DisplayName("ProductCategory Display Tests")
    inner class VisualizacionCategoriasTests {

        @Test
        @DisplayName("debe tener displayName capitalizado correctamente")
        fun `debe tener displayName capitalizado correctamente`() {
            ProductCategory.frutas.displayName shouldBe "Frutas"
            ProductCategory.verduras.displayName shouldBe "Verduras"
            ProductCategory.lacteos.displayName shouldBe "Lácteos"
        }

        @Test
        @DisplayName("debe poder generar lista de opciones para UI")
        fun `debe poder generar lista de opciones para UI`() {
            val opciones = ProductCategory.entries.map { it.displayName }

            opciones shouldHaveSize 5
            opciones.shouldNotBeEmpty()
            opciones.all { it.isNotBlank() } shouldBe true
        }

        @Test
        @DisplayName("debe poder convertir displayName a enum")
        fun `debe poder convertir displayName a enum`() {
            val displayName = "Frutas"
            val categoria = ProductCategory.entries.find { it.displayName == displayName }

            categoria shouldNotBe null
            categoria shouldBe ProductCategory.frutas
        }

        @Test
        @DisplayName("debe tener nombres legibles para usuarios")
        fun `debe tener nombres legibles para usuarios`() {
            val nombres = ProductCategory.entries.map { it.displayName }

            nombres shouldContainAll listOf("Frutas", "Verduras", "Productos Orgánicos", "Lácteos", "Otros")
        }

        @Test
        @DisplayName("debe poder generar descripción de categoría")
        fun `debe poder generar descripcion de categoria`() {
            val descripcion = "Productos en ${ProductCategory.frutas.displayName}"

            descripcion shouldBe "Productos en Frutas"
        }
    }

    @Nested
    @DisplayName("ProductCategory Edge Cases Tests")
    inner class CasosBordeCategoriaTests {

        @Test
        @DisplayName("debe manejar comparación con null")
        fun `debe manejar comparacion con null`() {
            val categoria: ProductCategory? = ProductCategory.frutas
            val nula: ProductCategory? = null

            (categoria == nula) shouldBe false
            (categoria != null) shouldBe true
        }

        @Test
        @DisplayName("debe poder usar en colecciones Set")
        fun `debe poder usar en colecciones Set`() {
            val set = setOf(
                ProductCategory.frutas,
                ProductCategory.verduras,
                ProductCategory.frutas // Duplicado
            )

            set shouldHaveSize 2
            set shouldContain ProductCategory.frutas
            set shouldContain ProductCategory.verduras
        }

        @Test
        @DisplayName("debe poder usar en colecciones Map como clave")
        fun `debe poder usar en colecciones Map como clave`() {
            val mapa = mapOf(
                ProductCategory.frutas to "Sección 1",
                ProductCategory.verduras to "Sección 2",
                ProductCategory.lacteos to "Sección 3"
            )

            mapa[ProductCategory.frutas] shouldBe "Sección 1"
            mapa[ProductCategory.verduras] shouldBe "Sección 2"
        }

        @Test
        @DisplayName("debe mantener consistencia en hashCode")
        fun `debe mantener consistencia en hashCode`() {
            val categoria1 = ProductCategory.frutas
            val categoria2 = ProductCategory.frutas

            categoria1.hashCode() shouldBe categoria2.hashCode()
        }

        @Test
        @DisplayName("debe poder usar operador when exhaustivo")
        fun `debe poder usar operador when exhaustivo`() {
            fun obtenerDescripcion(categoria: ProductCategory): String = when (categoria) {
                ProductCategory.frutas -> "Frutas frescas"
                ProductCategory.verduras -> "Verduras del campo"
                ProductCategory.productosOrganicos -> "Productos orgánicos"
                ProductCategory.lacteos -> "Productos lácteos"
                ProductCategory.otros -> "Otros productos"
            }

            obtenerDescripcion(ProductCategory.frutas) shouldBe "Frutas frescas"
            obtenerDescripcion(ProductCategory.lacteos) shouldBe "Productos lácteos"
        }

        @Test
        @DisplayName("debe permitir conversión toString")
        fun `debe permitir conversion toString`() {
            val categoria = ProductCategory.frutas
            val string = categoria.toString()

            string shouldBe "frutas"
        }
    }

    // Función auxiliar para crear productos de prueba
    private fun crearProducto(
        id: Int = 1,
        nombre: String = "Producto Test",
        precio: String = "$1000",
        imageResId: Int = R.drawable.producto01f,
        categoria: ProductCategory = ProductCategory.frutas
    ) = Product(
        id = id,
        name = nombre,
        price = precio,
        imagesRes = imageResId,
        category = categoria
    )
}