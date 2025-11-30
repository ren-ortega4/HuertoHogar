package com.example.huertohogar.view.screen

import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.viewmodel.Category
import com.example.huertohogar.viewmodel.MainScreenUiState
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("MainContent Tests")
class MainContentTest {

    @Nested
    @DisplayName("Category Data Class Tests")
    inner class ClaseCategoriaDatosTests {

        @Test
        @DisplayName("debe crear categoría con todos los parámetros")
        fun `debe crear categoria con todos los parametros`() {
            val categoria = Category(
                name = "Frutas",
                imageRes = R.drawable.fruta,
                description = "Frutas frescas"
            )

            categoria.name shouldBe "Frutas"
            categoria.imageRes shouldBe R.drawable.fruta
            categoria.description shouldBe "Frutas frescas"
        }

        @Test
        @DisplayName("debe verificar que name es un String")
        fun `debe verificar que name es un String`() {
            val categoria = Category("Verduras", R.drawable.verdura, "Descripción")

            categoria.name.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe verificar que imageRes es un Int")
        fun `debe verificar que imageRes es un Int`() {
            val categoria = Category("Lácteos", R.drawable.lacteos, "Descripción")

            categoria.imageRes.shouldBeInstanceOf<Int>()
        }

        @Test
        @DisplayName("debe verificar que description es un String")
        fun `debe verificar que description es un String`() {
            val categoria = Category("Carnes", R.drawable.logotipo, "Descripción")

            categoria.description.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe crear dos categorías con los mismos valores como iguales")
        fun `debe crear dos categorias con los mismos valores como iguales`() {
            val categoria1 = Category("Frutas", R.drawable.fruta, "Descripción 1")
            val categoria2 = Category("Frutas", R.drawable.fruta, "Descripción 1")

            categoria1 shouldBe categoria2
        }

        @Test
        @DisplayName("debe crear dos categorías con valores diferentes como no iguales")
        fun `debe crear dos categorias con valores diferentes como no iguales`() {
            val categoria1 = Category("Frutas", R.drawable.fruta, "Descripción 1")
            val categoria2 = Category("Verduras", R.drawable.verdura, "Descripción 2")

            categoria1 shouldNotBe categoria2
        }

        @Test
        @DisplayName("debe permitir copiar categoría con método copy")
        fun `debe permitir copiar categoria con metodo copy`() {
            val original = Category("Frutas", R.drawable.fruta, "Original")
            val copia = original.copy()

            copia shouldBe original
            copia.name shouldBe original.name
        }

        @Test
        @DisplayName("debe permitir copiar categoría modificando solo el nombre")
        fun `debe permitir copiar categoria modificando solo el nombre`() {
            val original = Category("Frutas", R.drawable.fruta, "Descripción")
            val modificada = original.copy(name = "Verduras")

            modificada.name shouldBe "Verduras"
            modificada.imageRes shouldBe original.imageRes
            modificada.description shouldBe original.description
        }

        @Test
        @DisplayName("debe permitir copiar categoría modificando solo la descripción")
        fun `debe permitir copiar categoria modificando solo la descripcion`() {
            val original = Category("Frutas", R.drawable.fruta, "Descripción original")
            val modificada = original.copy(description = "Nueva descripción")

            modificada.name shouldBe original.name
            modificada.imageRes shouldBe original.imageRes
            modificada.description shouldBe "Nueva descripción"
        }

        @Test
        @DisplayName("debe permitir copiar categoría modificando solo el recurso de imagen")
        fun `debe permitir copiar categoria modificando solo el recurso de imagen`() {
            val original = Category("Frutas", R.drawable.fruta, "Descripción")
            val modificada = original.copy(imageRes = R.drawable.verdura)

            modificada.name shouldBe original.name
            modificada.imageRes shouldBe R.drawable.verdura
            modificada.description shouldBe original.description
        }

        @Test
        @DisplayName("debe manejar nombres de categoría vacíos")
        fun `debe manejar nombres de categoria vacios`() {
            val categoria = Category("", R.drawable.logotipo, "Descripción")

            categoria.name shouldBe ""
            categoria.name.isEmpty() shouldBe true
        }

        @Test
        @DisplayName("debe manejar descripciones vacías")
        fun `debe manejar descripciones vacias`() {
            val categoria = Category("Frutas", R.drawable.fruta, "")

            categoria.description shouldBe ""
            categoria.description.isEmpty() shouldBe true
        }

        @Test
        @DisplayName("debe manejar nombres con espacios")
        fun `debe manejar nombres con espacios`() {
            val categoria = Category("Frutas Tropicales", R.drawable.fruta, "Descripción")

            categoria.name shouldBe "Frutas Tropicales"
            categoria.name.contains(" ") shouldBe true
        }

        @Test
        @DisplayName("debe manejar descripciones largas")
        fun `debe manejar descripciones largas`() {
            val descripcionLarga = "Esta es una descripción muy larga de la categoría ".repeat(5)
            val categoria = Category("Frutas", R.drawable.fruta, descripcionLarga)

            categoria.description shouldBe descripcionLarga
            categoria.description.length shouldNotBe 0
        }

        @Test
        @DisplayName("debe manejar nombres con caracteres especiales")
        fun `debe manejar nombres con caracteres especiales`() {
            val categoria = Category("Frutas & Verduras 100%", R.drawable.organico, "Descripción")

            categoria.name shouldBe "Frutas & Verduras 100%"
            categoria.name.contains("&") shouldBe true
        }

        @Test
        @DisplayName("debe poder crear lista de categorías")
        fun `debe poder crear lista de categorias`() {
            val categorias = listOf(
                Category("Frutas", R.drawable.fruta, "Desc 1"),
                Category("Verduras", R.drawable.verdura, "Desc 2"),
                Category("Lácteos", R.drawable.lacteos, "Desc 3")
            )

            categorias shouldHaveSize 3
            categorias.first().name shouldBe "Frutas"
            categorias.last().name shouldBe "Lácteos"
        }

        @Test
        @DisplayName("debe verificar que dos categorías con mismo nombre pero diferente descripción no son iguales")
        fun `debe verificar que dos categorias con mismo nombre pero diferente descripcion no son iguales`() {
            val categoria1 = Category("Frutas", R.drawable.fruta, "Descripción 1")
            val categoria2 = Category("Frutas", R.drawable.fruta, "Descripción 2")

            categoria1 shouldNotBe categoria2
        }

        @Test
        @DisplayName("debe generar hashCode consistente para mismos valores")
        fun `debe generar hashCode consistente para mismos valores`() {
            val categoria1 = Category("Frutas", R.drawable.fruta, "Descripción")
            val categoria2 = Category("Frutas", R.drawable.fruta, "Descripción")

            categoria1.hashCode() shouldBe categoria2.hashCode()
        }

        @Test
        @DisplayName("debe generar toString con todos los campos")
        fun `debe generar toString con todos los campos`() {
            val categoria = Category("Frutas", R.drawable.fruta, "Descripción test")

            val resultado = categoria.toString()

            resultado shouldContain "Frutas"
            resultado shouldContain "Descripción test"
        }
    }

    @Nested
    @DisplayName("MainScreenUiState Data Class Tests")
    inner class ClaseMainScreenUiStateDatosTests {

        @Test
        @DisplayName("debe crear estado inicial con valores por defecto")
        fun `debe crear estado inicial con valores por defecto`() {
            val estado = MainScreenUiState()

            estado.featuredProducts.shouldBeEmpty()
            estado.categories.shouldBeEmpty()
            estado.isLoading shouldBe true
        }

        @Test
        @DisplayName("debe crear estado con productos destacados")
        fun `debe crear estado con productos destacados`() {
            val productos = listOf(
                crearProductoMuestra(id = 1, nombre = "Producto 1"),
                crearProductoMuestra(id = 2, nombre = "Producto 2")
            )
            val estado = MainScreenUiState(featuredProducts = productos)

            estado.featuredProducts shouldHaveSize 2
            estado.featuredProducts shouldContain productos[0]
        }

        @Test
        @DisplayName("debe crear estado con categorías")
        fun `debe crear estado con categorias`() {
            val categorias = listOf(
                Category("Frutas", R.drawable.fruta, "Desc 1"),
                Category("Verduras", R.drawable.verdura, "Desc 2")
            )
            val estado = MainScreenUiState(categories = categorias)

            estado.categories shouldHaveSize 2
            estado.categories.first().name shouldBe "Frutas"
        }

        @Test
        @DisplayName("debe crear estado con isLoading en false")
        fun `debe crear estado con isLoading en false`() {
            val estado = MainScreenUiState(isLoading = false)

            estado.isLoading shouldBe false
        }

        @Test
        @DisplayName("debe crear estado completo con todos los parámetros")
        fun `debe crear estado completo con todos los parametros`() {
            val productos = listOf(crearProductoMuestra())
            val categorias = listOf(Category("Frutas", R.drawable.fruta, "Desc"))
            val estado = MainScreenUiState(
                featuredProducts = productos,
                categories = categorias,
                isLoading = false
            )

            estado.featuredProducts shouldHaveSize 1
            estado.categories shouldHaveSize 1
            estado.isLoading shouldBe false
        }

        @Test
        @DisplayName("debe permitir copiar estado con método copy")
        fun `debe permitir copiar estado con metodo copy`() {
            val original = MainScreenUiState(isLoading = true)
            val copia = original.copy()

            copia shouldBe original
        }

        @Test
        @DisplayName("debe permitir copiar estado modificando solo isLoading")
        fun `debe permitir copiar estado modificando solo isLoading`() {
            val original = MainScreenUiState(isLoading = true)
            val modificado = original.copy(isLoading = false)

            modificado.isLoading shouldBe false
            modificado.featuredProducts shouldBe original.featuredProducts
            modificado.categories shouldBe original.categories
        }

        @Test
        @DisplayName("debe permitir copiar estado modificando solo productos")
        fun `debe permitir copiar estado modificando solo productos`() {
            val original = MainScreenUiState()
            val nuevosProductos = listOf(crearProductoMuestra())
            val modificado = original.copy(featuredProducts = nuevosProductos)

            modificado.featuredProducts shouldHaveSize 1
            modificado.categories shouldBe original.categories
            modificado.isLoading shouldBe original.isLoading
        }

        @Test
        @DisplayName("debe permitir copiar estado modificando solo categorías")
        fun `debe permitir copiar estado modificando solo categorias`() {
            val original = MainScreenUiState()
            val nuevasCategorias = listOf(Category("Frutas", R.drawable.fruta, "Desc"))
            val modificado = original.copy(categories = nuevasCategorias)

            modificado.categories shouldHaveSize 1
            modificado.featuredProducts shouldBe original.featuredProducts
            modificado.isLoading shouldBe original.isLoading
        }

        @Test
        @DisplayName("debe verificar que featuredProducts es una lista")
        fun `debe verificar que featuredProducts es una lista`() {
            val estado = MainScreenUiState()

            estado.featuredProducts.shouldBeInstanceOf<List<Product>>()
        }

        @Test
        @DisplayName("debe verificar que categories es una lista")
        fun `debe verificar que categories es una lista`() {
            val estado = MainScreenUiState()

            estado.categories.shouldBeInstanceOf<List<Category>>()
        }

        @Test
        @DisplayName("debe verificar que isLoading es un Boolean")
        fun `debe verificar que isLoading es un Boolean`() {
            val estado = MainScreenUiState()

            estado.isLoading.shouldBeInstanceOf<Boolean>()
        }

        @Test
        @DisplayName("debe manejar listas vacías de productos")
        fun `debe manejar listas vacias de productos`() {
            val estado = MainScreenUiState(featuredProducts = emptyList())

            estado.featuredProducts.shouldBeEmpty()
            estado.featuredProducts shouldHaveSize 0
        }

        @Test
        @DisplayName("debe manejar listas vacías de categorías")
        fun `debe manejar listas vacias de categorias`() {
            val estado = MainScreenUiState(categories = emptyList())

            estado.categories.shouldBeEmpty()
            estado.categories shouldHaveSize 0
        }

        @Test
        @DisplayName("debe manejar múltiples productos destacados")
        fun `debe manejar multiples productos destacados`() {
            val productos = List(10) { index ->
                crearProductoMuestra(id = index, nombre = "Producto $index")
            }
            val estado = MainScreenUiState(featuredProducts = productos)

            estado.featuredProducts shouldHaveSize 10
            estado.featuredProducts.shouldNotBeEmpty()
        }

        @Test
        @DisplayName("debe manejar múltiples categorías")
        fun `debe manejar multiples categorias`() {
            val categorias = List(5) { index ->
                Category("Categoría $index", R.drawable.logotipo, "Desc $index")
            }
            val estado = MainScreenUiState(categories = categorias)

            estado.categories shouldHaveSize 5
            estado.categories.shouldNotBeEmpty()
        }

        @Test
        @DisplayName("debe crear dos estados con mismos valores como iguales")
        fun `debe crear dos estados con mismos valores como iguales`() {
            val estado1 = MainScreenUiState(isLoading = true)
            val estado2 = MainScreenUiState(isLoading = true)

            estado1 shouldBe estado2
        }

        @Test
        @DisplayName("debe crear dos estados con valores diferentes como no iguales")
        fun `debe crear dos estados con valores diferentes como no iguales`() {
            val estado1 = MainScreenUiState(isLoading = true)
            val estado2 = MainScreenUiState(isLoading = false)

            estado1 shouldNotBe estado2
        }

        @Test
        @DisplayName("debe generar hashCode consistente para mismos valores")
        fun `debe generar hashCode consistente para mismos valores`() {
            val estado1 = MainScreenUiState(isLoading = false)
            val estado2 = MainScreenUiState(isLoading = false)

            estado1.hashCode() shouldBe estado2.hashCode()
        }

        @Test
        @DisplayName("debe verificar transición de estado de carga a cargado")
        fun `debe verificar transicion de estado de carga a cargado`() {
            val estadoCargando = MainScreenUiState(isLoading = true)
            val productos = listOf(crearProductoMuestra())
            val categorias = listOf(Category("Frutas", R.drawable.fruta, "Desc"))
            val estadoCargado = estadoCargando.copy(
                featuredProducts = productos,
                categories = categorias,
                isLoading = false
            )

            estadoCargado.isLoading shouldBe false
            estadoCargado.featuredProducts.shouldNotBeEmpty()
            estadoCargado.categories.shouldNotBeEmpty()
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class PruebasIntegracionTests {

        @Test
        @DisplayName("debe crear estado completo con categorías y productos")
        fun `debe crear estado completo con categorias y productos`() {
            val categorias = listOf(
                Category("Frutas", R.drawable.fruta, "Frutas frescas"),
                Category("Verduras", R.drawable.verdura, "Verduras orgánicas")
            )
            val productos = listOf(
                crearProductoMuestra(id = 1, nombre = "Manzana"),
                crearProductoMuestra(id = 2, nombre = "Zanahoria")
            )
            val estado = MainScreenUiState(
                featuredProducts = productos,
                categories = categorias,
                isLoading = false
            )

            estado.featuredProducts shouldHaveSize 2
            estado.categories shouldHaveSize 2
            estado.isLoading shouldBe false
        }

        @Test
        @DisplayName("debe verificar que categoría puede ser usada en MainScreenUiState")
        fun `debe verificar que categoria puede ser usada en MainScreenUiState`() {
            val categoria = Category("Lácteos", R.drawable.lacteos, "Productos lácteos")
            val estado = MainScreenUiState(categories = listOf(categoria))

            estado.categories.first() shouldBe categoria
            estado.categories.first().name shouldBe "Lácteos"
        }

        @Test
        @DisplayName("debe poder filtrar categorías por nombre")
        fun `debe poder filtrar categorias por nombre`() {
            val categorias = listOf(
                Category("Frutas", R.drawable.fruta, "Desc 1"),
                Category("Verduras", R.drawable.verdura, "Desc 2"),
                Category("Frutas Tropicales", R.drawable.fruta, "Desc 3")
            )
            val estado = MainScreenUiState(categories = categorias)

            val categoriasFrutas = estado.categories.filter { it.name.contains("Frutas") }

            categoriasFrutas shouldHaveSize 2
        }
    }

    // Función auxiliar para crear productos de muestra
    private fun crearProductoMuestra(
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