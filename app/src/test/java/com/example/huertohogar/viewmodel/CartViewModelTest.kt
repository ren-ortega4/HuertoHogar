package com.example.huertohogar.viewmodel

import com.example.huertohogar.model.CartItem
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("CartViewModel Tests")
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val testDispatcher = StandardTestDispatcher()

    // Productos de prueba
    private val testProduct1 = Product(
        id = 1,
        name = "Tomate",
        price = "$5000/Kg",
        imagesRes = 0,
        category = ProductCategory.verduras
    )

    private val testProduct2 = Product(
        id = 2,
        name = "Lechuga",
        price = "$3000/unidad",
        imagesRes = 0,
        category = ProductCategory.verduras
    )

    private val testProduct3 = Product(
        id = 3,
        name = "Manzana",
        price = "$8000/Kg",
        imagesRes = 0,
        category = ProductCategory.frutas
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Test de añadir productos al carrito")
    inner class AddToCartTests {

        @Test
        @DisplayName("agregar un producto nuevo al carrito")
        fun `Añadir un producto nuevo al carrito`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
            assertEquals(testProduct1.id, cartItems[0].product.id)
            assertEquals(2, cartItems[0].quantity)
        }

        @Test
        @DisplayName("Debe incrementar cantidad si el producto ya existe")
        fun `add existing product should increase quantity`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.addToCart(testProduct1, 3)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
            assertEquals(5, cartItems[0].quantity)
        }

        @Test
        @DisplayName("Debe agregar múltiples productos diferentes")
        fun `add multiple different products`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 1)
            viewModel.addToCart(testProduct2, 2)
            viewModel.addToCart(testProduct3, 3)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(3, cartItems.size)
        }

        @Test
        @DisplayName("Debe agregar con cantidad por defecto de 1")
        fun `Debe agregar cantidad por defecto`() = runTest {
            // When
            viewModel.addToCart(testProduct1)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems[0].quantity)
        }

        @Test
        @DisplayName("Debe manejar cantidades grandes correctamente")
        fun `add product with large quantity`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 100)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(100, cartItems[0].quantity)
        }
    }

    // ==================== PRUEBAS DE REMOVER DEL CARRITO ====================

    @Nested
    @DisplayName("Remove from Cart Tests")
    inner class RemoveFromCartTests {

        @Test
        @DisplayName("Debe remover un producto del carrito")
        fun `remove product from cart`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            viewModel.addToCart(testProduct2, 1)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.removeFromCart(testProduct1.id)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
            assertEquals(testProduct2.id, cartItems[0].product.id)
        }

        @Test
        @DisplayName("Debe manejar eliminación de producto inexistente")
        fun `remove non-existent product does nothing`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.removeFromCart(999)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
        }

        @Test
        @DisplayName("Debe poder remover todos los productos uno por uno")
        fun `remove all products one by one`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 1)
            viewModel.addToCart(testProduct2, 1)
            viewModel.addToCart(testProduct3, 1)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.removeFromCart(testProduct1.id)
            viewModel.removeFromCart(testProduct2.id)
            viewModel.removeFromCart(testProduct3.id)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertTrue(cartItems.isEmpty())
        }
    }

    // ==================== PRUEBAS DE ACTUALIZAR CANTIDAD ====================

    @Nested
    @DisplayName("Update Quantity Tests")
    inner class UpdateQuantityTests {

        @Test
        @DisplayName("Debe actualizar la cantidad de un producto")
        fun `update product quantity`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.updateQuantity(testProduct1.id, 5)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(5, cartItems[0].quantity)
        }

        @Test
        @DisplayName("Debe remover producto si la cantidad es 0")
        fun `update quantity to zero removes product`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.updateQuantity(testProduct1.id, 0)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertTrue(cartItems.isEmpty())
        }

        @Test
        @DisplayName("Debe remover producto si la cantidad es negativa")
        fun `update quantity to negative removes product`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.updateQuantity(testProduct1.id, -5)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertTrue(cartItems.isEmpty())
        }

        @Test
        @DisplayName("Debe manejar actualización de producto inexistente")
        fun `update non-existent product does nothing`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.updateQuantity(999, 10)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
            assertEquals(2, cartItems[0].quantity)
        }

        @Test
        @DisplayName("Debe actualizar a cantidad grande correctamente")
        fun `update to large quantity`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 1)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.updateQuantity(testProduct1.id, 1000)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1000, cartItems[0].quantity)
        }
    }

    // ==================== PRUEBAS DE LIMPIAR CARRITO ====================

    @Nested
    @DisplayName("Clear Cart Tests")
    inner class ClearCartTests {

        @Test
        @DisplayName("Debe limpiar todos los productos del carrito")
        fun `clear cart removes all products`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            viewModel.addToCart(testProduct2, 3)
            viewModel.addToCart(testProduct3, 1)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.clearCart()
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertTrue(cartItems.isEmpty())
        }

        @Test
        @DisplayName("Debe poder limpiar un carrito vacío sin errores")
        fun `clear empty cart does nothing`() = runTest {
            // When
            viewModel.clearCart()
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertTrue(cartItems.isEmpty())
        }

        @Test
        @DisplayName("Debe resetear totales después de limpiar")
        fun `clear cart resets totals`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            viewModel.addToCart(testProduct2, 3)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.clearCart()
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(0, viewModel.totalItems.first())
            assertEquals(0.0, viewModel.totalPrice.first())
        }
    }

    // ==================== PRUEBAS DE CÁLCULO DE TOTALES ====================

    @Nested
    @DisplayName("Totals Calculation Tests")
    inner class TotalsCalculationTests {

        @Test
        @DisplayName("Debe calcular total de items correctamente")
        fun `calculate total items correctly`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 2)
            viewModel.addToCart(testProduct2, 3)
            viewModel.addToCart(testProduct3, 1)
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(6, viewModel.totalItems.first())
        }

        @Test
        @DisplayName("Debe calcular precio total correctamente")
        fun `calculate total price correctly`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 2) // 2 * 5000 = 10000
            viewModel.addToCart(testProduct2, 3) // 3 * 3000 = 9000
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(19000.0, viewModel.totalPrice.first())
        }

        @Test
        @DisplayName("Debe actualizar totales al agregar productos")
        fun `totals update when adding products`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 1)
            testScheduler.advanceUntilIdle()

            val totalItems1 = viewModel.totalItems.first()
            val totalPrice1 = viewModel.totalPrice.first()

            viewModel.addToCart(testProduct2, 2)
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(1, totalItems1)
            assertEquals(5000.0, totalPrice1)
            assertEquals(3, viewModel.totalItems.first())
            assertEquals(11000.0, viewModel.totalPrice.first())
        }

        @Test
        @DisplayName("Debe actualizar totales al remover productos")
        fun `totals update when removing products`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            viewModel.addToCart(testProduct2, 3)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.removeFromCart(testProduct1.id)
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(3, viewModel.totalItems.first())
            assertEquals(9000.0, viewModel.totalPrice.first())
        }

        @Test
        @DisplayName("Debe actualizar totales al cambiar cantidades")
        fun `totals update when changing quantities`() = runTest {
            // Given
            viewModel.addToCart(testProduct1, 2)
            testScheduler.advanceUntilIdle()

            // When
            viewModel.updateQuantity(testProduct1.id, 5)
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(5, viewModel.totalItems.first())
            assertEquals(25000.0, viewModel.totalPrice.first())
        }

        @Test
        @DisplayName("Debe tener totales en cero al iniciar")
        fun `initial totals are zero`() = runTest {
            // Then
            assertEquals(0, viewModel.totalItems.first())
            assertEquals(0.0, viewModel.totalPrice.first())
        }

        @Test
        @DisplayName("Debe calcular totales con un solo producto")
        fun `calculate totals with single product`() = runTest {
            // When
            viewModel.addToCart(testProduct3, 4) // 4 * 8000 = 32000
            testScheduler.advanceUntilIdle()

            // Then
            assertEquals(4, viewModel.totalItems.first())
            assertEquals(32000.0, viewModel.totalPrice.first())
        }
    }

    // ==================== PRUEBAS DE ESTADO INICIAL ====================

    @Nested
    @DisplayName("Initial State Tests")
    inner class InitialStateTests {

        @Test
        @DisplayName("Carrito debe estar vacío al iniciar")
        fun `cart starts empty`() = runTest {
            // Then
            val cartItems = viewModel.cartItems.first()
            assertTrue(cartItems.isEmpty())
        }

        @Test
        @DisplayName("Total items debe ser cero al iniciar")
        fun `total items starts at zero`() = runTest {
            // Then
            assertEquals(0, viewModel.totalItems.first())
        }

        @Test
        @DisplayName("Precio total debe ser cero al iniciar")
        fun `total price starts at zero`() = runTest {
            // Then
            assertEquals(0.0, viewModel.totalPrice.first())
        }
    }

    // ==================== PRUEBAS DE ESCENARIOS COMPLEJOS ====================

    @Nested
    @DisplayName("Complex Scenarios Tests")
    inner class ComplexScenariosTests {

        @Test
        @DisplayName("Debe manejar flujo completo de compra")
        fun `complete shopping flow`() = runTest {
            // Agregar productos
            viewModel.addToCart(testProduct1, 2)
            viewModel.addToCart(testProduct2, 1)
            viewModel.addToCart(testProduct3, 3)
            testScheduler.advanceUntilIdle()

            assertEquals(3, viewModel.cartItems.first().size)
            assertEquals(6, viewModel.totalItems.first())

            // Actualizar cantidad
            viewModel.updateQuantity(testProduct1.id, 5)
            testScheduler.advanceUntilIdle()

            assertEquals(9, viewModel.totalItems.first())

            // Remover un producto
            viewModel.removeFromCart(testProduct2.id)
            testScheduler.advanceUntilIdle()

            assertEquals(2, viewModel.cartItems.first().size)
            assertEquals(8, viewModel.totalItems.first())

            // Agregar producto existente
            viewModel.addToCart(testProduct3, 2)
            testScheduler.advanceUntilIdle()

            assertEquals(5, viewModel.cartItems.first().find { it.product.id == testProduct3.id }?.quantity)

            // Limpiar carrito
            viewModel.clearCart()
            testScheduler.advanceUntilIdle()

            assertTrue(viewModel.cartItems.first().isEmpty())
            assertEquals(0, viewModel.totalItems.first())
            assertEquals(0.0, viewModel.totalPrice.first())
        }

        @Test
        @DisplayName("Debe mantener integridad al agregar y remover rápidamente")
        fun `maintain integrity with rapid add and remove`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 1)
            viewModel.addToCart(testProduct2, 1)
            viewModel.removeFromCart(testProduct1.id)
            viewModel.addToCart(testProduct3, 1)
            viewModel.removeFromCart(testProduct2.id)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
            assertEquals(testProduct3.id, cartItems[0].product.id)
        }

        @Test
        @DisplayName("Debe manejar múltiples actualizaciones del mismo producto")
        fun `handle multiple updates to same product`() = runTest {
            // When
            viewModel.addToCart(testProduct1, 1)
            viewModel.updateQuantity(testProduct1.id, 3)
            viewModel.updateQuantity(testProduct1.id, 7)
            viewModel.updateQuantity(testProduct1.id, 4)
            testScheduler.advanceUntilIdle()

            // Then
            val cartItems = viewModel.cartItems.first()
            assertEquals(1, cartItems.size)
            assertEquals(4, cartItems[0].quantity)
        }
    }
}
