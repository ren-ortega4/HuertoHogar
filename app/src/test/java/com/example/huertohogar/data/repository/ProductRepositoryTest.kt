package com.example.huertohogar.data.repository

import com.example.huertohogar.R
import com.example.huertohogar.data.local.ProductDao
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("Pruebas unitarias de ProductRepository")
class ProductRepositoryTest {

    private lateinit var productDao: ProductDao
    private lateinit var productRepository: ProductRepository

    private val productoTest1 = Product(
        id = 1,
        name = "Tomate Cherry",
        price = "$2.500/Kg",
        imagesRes = R.drawable.destacado1,
        category = ProductCategory.verduras
    )

    private val productoTest2 = Product(
        id = 2,
        name = "Manzana Fuji",
        price = "$1.800/Kg",
        imagesRes = R.drawable.destacado2,
        category = ProductCategory.frutas
    )

    private val productoTest3 = Product(
        id = 3,
        name = "Leche Orgánica",
        price = "$4.200",
        imagesRes = R.drawable.destacado3,
        category = ProductCategory.lacteos
    )

    private val productoTest4 = Product(
        id = 4,
        name = "Naranja Valencia",
        price = "$1.200/Kg",
        imagesRes = R.drawable.destacado1,
        category = ProductCategory.frutas
    )

    @BeforeEach
    fun setUp() {
        productDao = mockk()
        productRepository = ProductRepository(productDao)
    }

    @Nested
    @DisplayName("Pruebas de obtención de productos")
    inner class ObtenerProductos {

        @Test
        @DisplayName("Debe obtener todos los productos correctamente")
        fun `getAllProducts debe retornar flow con todos los productos`() = runTest {
            // Given
            val listaProductos = listOf(productoTest1, productoTest2, productoTest3)
            every { productDao.getAllProducts() } returns flowOf(listaProductos)

            // When
            val resultado = productRepository.getAllProducts().first()

            // Then
            resultado shouldNotBe null
            resultado shouldHaveSize 3
            resultado shouldContain productoTest1
            resultado shouldContain productoTest2
            resultado shouldContain productoTest3
            verify(exactly = 1) { productDao.getAllProducts() }
        }

        @Test
        @DisplayName("Debe retornar flow vacío cuando no hay productos")
        fun `getAllProducts debe retornar flow vacio cuando no hay productos`() = runTest {
            // Given
            every { productDao.getAllProducts() } returns flowOf(emptyList())

            // When
            val resultado = productRepository.getAllProducts().first()

            // Then
            resultado.shouldNotBeNull()
            resultado.shouldBeEmpty()
            verify(exactly = 1) { productDao.getAllProducts() }
        }

        @Test
        @DisplayName("Debe obtener productos por categoría correctamente")
        fun `getProductsByCategory debe retornar productos de la categoria especificada`() = runTest {
            // Given
            val categoria = ProductCategory.frutas
            val productosFrutas = listOf(productoTest2, productoTest4)
            every { productDao.getProductsByCategory(categoria) } returns flowOf(productosFrutas)

            // When
            val resultado = productRepository.getProductsByCategory(categoria).first()

            // Then
            resultado.shouldNotBeNull()
            resultado shouldHaveSize 2
            resultado.all { it.category == ProductCategory.frutas } shouldBe true
            verify(exactly = 1) { productDao.getProductsByCategory(categoria) }
        }

        @Test
        @DisplayName("Debe retornar flow vacío cuando no hay productos de la categoría")
        fun `getProductsByCategory debe retornar vacio cuando no hay productos de esa categoria`() = runTest {
            // Given
            val categoria = ProductCategory.otros
            every { productDao.getProductsByCategory(categoria) } returns flowOf(emptyList())

            // When
            val resultado = productRepository.getProductsByCategory(categoria).first()

            // Then
            resultado.shouldNotBeNull()
            resultado.shouldBeEmpty()
            verify(exactly = 1) { productDao.getProductsByCategory(categoria) }
        }

        @Test
        @DisplayName("Debe obtener producto por ID correctamente")
        fun `getProductById debe retornar el producto con el ID especificado`() = runTest {
            // Given
            val productId = 1
            coEvery { productDao.getProductById(productId) } returns productoTest1

            // When
            val resultado = productRepository.getProductById(productId)

            // Then
            resultado.shouldNotBeNull()
            resultado.id shouldBe productId
            resultado.name shouldBe "Tomate Cherry"
            coVerify(exactly = 1) { productDao.getProductById(productId) }
        }

        @Test
        @DisplayName("Debe retornar null cuando el producto no existe")
        fun `getProductById debe retornar null cuando el producto no existe`() = runTest {
            // Given
            val productIdInexistente = 999
            coEvery { productDao.getProductById(productIdInexistente) } returns null

            // When
            val resultado = productRepository.getProductById(productIdInexistente)

            // Then
            resultado.shouldBeNull()
            coVerify(exactly = 1) { productDao.getProductById(productIdInexistente) }
        }
    }

    @Nested
    @DisplayName("Pruebas de búsqueda de productos")
    inner class BusquedaProductos {

        @Test
        @DisplayName("Debe buscar productos por nombre correctamente")
        fun `searchProducts debe retornar productos que coinciden con la busqueda`() = runTest {
            // Given
            val query = "Tomate"
            every { productDao.searchProducts(query) } returns flowOf(listOf(productoTest1))

            // When
            val resultado = productRepository.searchProducts(query).first()

            // Then
            resultado.shouldNotBeNull()
            resultado shouldHaveSize 1
            resultado[0].name shouldBe "Tomate Cherry"
            verify(exactly = 1) { productDao.searchProducts(query) }
        }

        @Test
        @DisplayName("Debe retornar múltiples productos que coinciden con la búsqueda")
        fun `searchProducts debe retornar multiples productos coincidentes`() = runTest {
            // Given
            val query = "a"
            val productosConA = listOf(productoTest2, productoTest4) // Manzana, Naranja
            every { productDao.searchProducts(query) } returns flowOf(productosConA)

            // When
            val resultado = productRepository.searchProducts(query).first()

            // Then
            resultado.shouldNotBeNull()
            resultado shouldHaveSize 2
            verify(exactly = 1) { productDao.searchProducts(query) }
        }

        @Test
        @DisplayName("Debe retornar flow vacío cuando no hay coincidencias")
        fun `searchProducts debe retornar vacio cuando no hay coincidencias`() = runTest {
            // Given
            val query = "ProductoInexistente"
            every { productDao.searchProducts(query) } returns flowOf(emptyList())

            // When
            val resultado = productRepository.searchProducts(query).first()

            // Then
            resultado.shouldNotBeNull()
            resultado.shouldBeEmpty()
            verify(exactly = 1) { productDao.searchProducts(query) }
        }

        @Test
        @DisplayName("Debe manejar búsqueda con string vacío")
        fun `searchProducts debe manejar busqueda con string vacio`() = runTest {
            // Given
            val query = ""
            val todosLosProductos = listOf(productoTest1, productoTest2, productoTest3)
            every { productDao.searchProducts(query) } returns flowOf(todosLosProductos)

            // When
            val resultado = productRepository.searchProducts(query).first()

            // Then
            resultado.shouldNotBeNull()
            resultado shouldHaveSize 3
            verify(exactly = 1) { productDao.searchProducts(query) }
        }
    }

    @Nested
    @DisplayName("Pruebas de categorías")
    inner class Categorias {

        @Test
        @DisplayName("Debe obtener todas las categorías correctamente")
        fun `getAllCategories debe retornar todas las categorias disponibles`() = runTest {
            // Given
            val categorias = listOf(
                ProductCategory.frutas,
                ProductCategory.verduras,
                ProductCategory.lacteos
            )
            every { productDao.getAllCategories() } returns flowOf(categorias)

            // When
            val resultado = productRepository.getAllCategories().first()

            // Then
            resultado.shouldNotBeNull()
            resultado shouldHaveSize 3
            resultado shouldContain ProductCategory.frutas
            resultado shouldContain ProductCategory.verduras
            resultado shouldContain ProductCategory.lacteos
            verify(exactly = 1) { productDao.getAllCategories() }
        }

        @Test
        @DisplayName("Debe retornar flow vacío cuando no hay categorías")
        fun `getAllCategories debe retornar vacio cuando no hay categorias`() = runTest {
            // Given
            every { productDao.getAllCategories() } returns flowOf(emptyList())

            // When
            val resultado = productRepository.getAllCategories().first()

            // Then
            resultado.shouldNotBeNull()
            resultado.shouldBeEmpty()
            verify(exactly = 1) { productDao.getAllCategories() }
        }
    }

    @Nested
    @DisplayName("Pruebas de inserción de productos")
    inner class InsercionProductos {

        @Test
        @DisplayName("Debe insertar un producto correctamente")
        fun `insertProduct debe llamar al dao con el producto correcto`() = runTest {
            // Given
            coEvery { productDao.insertProduct(any()) } returns Unit

            // When
            productRepository.insertProduct(productoTest1)

            // Then
            coVerify(exactly = 1) { productDao.insertProduct(productoTest1) }
        }

        @Test
        @DisplayName("Debe insertar múltiples productos individualmente")
        fun `insertProduct debe permitir insertar varios productos`() = runTest {
            // Given
            coEvery { productDao.insertProduct(any()) } returns Unit

            // When
            productRepository.insertProduct(productoTest1)
            productRepository.insertProduct(productoTest2)
            productRepository.insertProduct(productoTest3)

            // Then
            coVerify(exactly = 1) { productDao.insertProduct(productoTest1) }
            coVerify(exactly = 1) { productDao.insertProduct(productoTest2) }
            coVerify(exactly = 1) { productDao.insertProduct(productoTest3) }
        }

        @Test
        @DisplayName("Debe insertar lista de productos correctamente")
        fun `insertProducts debe insertar lista de productos`() = runTest {
            // Given
            val listaProductos = listOf(productoTest1, productoTest2, productoTest3)
            coEvery { productDao.insertProducts(any()) } returns Unit

            // When
            productRepository.insertProducts(listaProductos)

            // Then
            coVerify(exactly = 1) { productDao.insertProducts(listaProductos) }
        }

        @Test
        @DisplayName("Debe manejar inserción de lista vacía")
        fun `insertProducts debe manejar lista vacia`() = runTest {
            // Given
            val listaVacia = emptyList<Product>()
            coEvery { productDao.insertProducts(any()) } returns Unit

            // When
            productRepository.insertProducts(listaVacia)

            // Then
            coVerify(exactly = 1) { productDao.insertProducts(listaVacia) }
        }
    }

    @Nested
    @DisplayName("Pruebas de actualización de productos")
    inner class ActualizacionProductos {

        @Test
        @DisplayName("Debe actualizar un producto correctamente")
        fun `updateProduct debe llamar al dao con el producto actualizado`() = runTest {
            // Given
            val productoActualizado = productoTest1.copy(price = "$3.000/Kg")
            coEvery { productDao.updateProduct(any()) } returns Unit

            // When
            productRepository.updateProduct(productoActualizado)

            // Then
            coVerify(exactly = 1) { productDao.updateProduct(productoActualizado) }
        }

        @Test
        @DisplayName("Debe actualizar múltiples productos")
        fun `updateProduct debe permitir actualizar varios productos`() = runTest {
            // Given
            val producto1Actualizado = productoTest1.copy(price = "$3.000/Kg")
            val producto2Actualizado = productoTest2.copy(name = "Manzana Verde")
            coEvery { productDao.updateProduct(any()) } returns Unit

            // When
            productRepository.updateProduct(producto1Actualizado)
            productRepository.updateProduct(producto2Actualizado)

            // Then
            coVerify(exactly = 1) { productDao.updateProduct(producto1Actualizado) }
            coVerify(exactly = 1) { productDao.updateProduct(producto2Actualizado) }
        }
    }

    @Nested
    @DisplayName("Pruebas de eliminación de productos")
    inner class EliminacionProductos {

        @Test
        @DisplayName("Debe eliminar un producto correctamente")
        fun `deleteProduct debe llamar al dao con el producto correcto`() = runTest {
            // Given
            coEvery { productDao.deleteProduct(any()) } returns Unit

            // When
            productRepository.deleteProduct(productoTest1)

            // Then
            coVerify(exactly = 1) { productDao.deleteProduct(productoTest1) }
        }

        @Test
        @DisplayName("Debe eliminar múltiples productos")
        fun `deleteProduct debe permitir eliminar varios productos`() = runTest {
            // Given
            coEvery { productDao.deleteProduct(any()) } returns Unit

            // When
            productRepository.deleteProduct(productoTest1)
            productRepository.deleteProduct(productoTest2)

            // Then
            coVerify(exactly = 1) { productDao.deleteProduct(productoTest1) }
            coVerify(exactly = 1) { productDao.deleteProduct(productoTest2) }
        }

        @Test
        @DisplayName("Debe eliminar todos los productos correctamente")
        fun `deleteAllProducts debe llamar al dao para eliminar todos`() = runTest {
            // Given
            coEvery { productDao.deleteAllProducts() } returns Unit

            // When
            productRepository.deleteAllProducts()

            // Then
            coVerify(exactly = 1) { productDao.deleteAllProducts() }
        }
    }

    @Nested
    @DisplayName("Pruebas del companion object")
    inner class CompanionObjectTests {

        @Test
        @DisplayName("Debe retornar flow con productos destacados")
        fun `getProductsFlow debe retornar productos destacados`() = runTest {
            // When
            val resultado = ProductRepository.getProductsFlow().first()

            // Then
            resultado.shouldNotBeNull()
            resultado shouldHaveSize 3
            resultado[0].name shouldBe "Leche Natural"
            resultado[0].category shouldBe ProductCategory.lacteos
            resultado[1].name shouldBe "Miel Orgánica"
            resultado[1].category shouldBe ProductCategory.productosOrganicos
            resultado[2].name shouldBe "Platános Cavendish"
            resultado[2].category shouldBe ProductCategory.frutas
        }

        @Test
        @DisplayName("Los productos destacados deben tener precios válidos")
        fun `getProductsFlow productos deben tener precios validos`() = runTest {
            // When
            val resultado = ProductRepository.getProductsFlow().first()

            // Then
            resultado.all { it.price.isNotEmpty() } shouldBe true
            resultado[0].price shouldBe "$3.800"
            resultado[1].price shouldBe "$5.000"
            resultado[2].price shouldBe "$800/Kg"
        }

        @Test
        @DisplayName("Los productos destacados deben tener recursos de imagen válidos")
        fun `getProductsFlow productos deben tener imagenes validas`() = runTest {
            // When
            val resultado = ProductRepository.getProductsFlow().first()

            // Then
            resultado.all { it.imagesRes > 0 } shouldBe true
        }
    }
}