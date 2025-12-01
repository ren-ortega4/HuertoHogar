package com.example.huertohogar.viewModel

import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.viewmodel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cartItems should be empty initially`() {
        // When
        val result = viewModel.cartItems.value

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `totalItems should be zero initially`() {
        // When
        val result = viewModel.totalItems.value

        // Then
        assertEquals(0, result)
    }

    @Test
    fun `totalPrice should be zero initially`() {
        // When
        val result = viewModel.totalPrice.value

        // Then
        assertEquals(0.0, result)
    }

    @Test
    fun `showSuccessBanner should be false initially`() {
        // When
        val result = viewModel.showSuccessBanner.value

        // Then
        assertFalse(result)
    }

    @Test
    fun `addToCart should add new product to cart`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)

        // When
        viewModel.addToCart(product, 2)

        // Then
        val cartItems = viewModel.cartItems.value
        assertEquals(1, cartItems.size)
        assertEquals("Manzana", cartItems[0].product.name)
        assertEquals(2, cartItems[0].quantity)
    }

    @Test
    fun `addToCart should update quantity if product already exists`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.addToCart(product, 3)

        // Then
        val cartItems = viewModel.cartItems.value
        assertEquals(1, cartItems.size)
        assertEquals(5, cartItems[0].quantity)
    }

    @Test
    fun `addToCart should update totalItems correctly`() {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)

        // When
        viewModel.addToCart(product1, 2)
        viewModel.addToCart(product2, 3)

        // Then
        assertEquals(5, viewModel.totalItems.value)
    }

    @Test
    fun `addToCart should update totalPrice correctly`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)

        // When
        viewModel.addToCart(product, 2)

        // Then
        assertEquals(2400.0, viewModel.totalPrice.value)
    }

    @Test
    fun `removeFromCart should remove product from cart`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.removeFromCart(1)

        // Then
        assertTrue(viewModel.cartItems.value.isEmpty())
        assertEquals(0, viewModel.totalItems.value)
        assertEquals(0.0, viewModel.totalPrice.value)
    }

    @Test
    fun `removeFromCart should not affect other products`() {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        viewModel.addToCart(product1, 2)
        viewModel.addToCart(product2, 3)

        // When
        viewModel.removeFromCart(1)

        // Then
        val cartItems = viewModel.cartItems.value
        assertEquals(1, cartItems.size)
        assertEquals("Lechuga", cartItems[0].product.name)
        assertEquals(3, viewModel.totalItems.value)
    }

    @Test
    fun `updateQuantity should update product quantity`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.updateQuantity(1, 5)

        // Then
        val cartItems = viewModel.cartItems.value
        assertEquals(5, cartItems[0].quantity)
        assertEquals(5, viewModel.totalItems.value)
        assertEquals(6000.0, viewModel.totalPrice.value)
    }

    @Test
    fun `updateQuantity should remove product if quantity is zero`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.updateQuantity(1, 0)

        // Then
        assertTrue(viewModel.cartItems.value.isEmpty())
    }

    @Test
    fun `updateQuantity should remove product if quantity is negative`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.updateQuantity(1, -1)

        // Then
        assertTrue(viewModel.cartItems.value.isEmpty())
    }

    @Test
    fun `updateQuantity should do nothing if product not found`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.updateQuantity(999, 5)

        // Then
        assertEquals(2, viewModel.cartItems.value[0].quantity)
    }

    @Test
    fun `clearCart should empty the cart`() {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        viewModel.addToCart(product1, 2)
        viewModel.addToCart(product2, 3)

        // When
        viewModel.clearCart()

        // Then
        assertTrue(viewModel.cartItems.value.isEmpty())
        assertEquals(0, viewModel.totalItems.value)
        assertEquals(0.0, viewModel.totalPrice.value)
    }

    @Test
    fun `onPurchaseSuccess should clear cart and show banner`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.onPurchaseSuccess()
        testScheduler.advanceTimeBy(100) // Avanzar un poco para que se ejecute la primera parte

        // Then
        assertTrue(viewModel.cartItems.value.isEmpty())
        assertTrue(viewModel.showSuccessBanner.value)
    }

    @Test
    fun `onPurchaseSuccess should hide banner after 3 seconds`() = runTest {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        viewModel.addToCart(product, 2)

        // When
        viewModel.onPurchaseSuccess()
        advanceTimeBy(1000) // 1 segundo
        assertTrue(viewModel.showSuccessBanner.value)
        
        advanceTimeBy(2500) // 2.5 segundos más = 3.5 segundos total
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.showSuccessBanner.value)
    }

    @Test
    fun `addToCart with default quantity should add 1 item`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)

        // When
        viewModel.addToCart(product)

        // Then
        assertEquals(1, viewModel.cartItems.value[0].quantity)
        assertEquals(1, viewModel.totalItems.value)
    }

    @Test
    fun `totalPrice should calculate correctly with multiple products`() {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)
        val product3 = Product(id = 3, name = "Leche", price = 1500.0, priceLabel = "$1.500", imagesRes = 3, category = ProductCategory.lacteos)

        // When
        viewModel.addToCart(product1, 2) // 2400
        viewModel.addToCart(product2, 3) // 2700
        viewModel.addToCart(product3, 1) // 1500

        // Then
        assertEquals(6600.0, viewModel.totalPrice.value)
        assertEquals(6, viewModel.totalItems.value)
    }

    @Test
    fun `cart should handle large quantities`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)

        // When
        viewModel.addToCart(product, 100)

        // Then
        assertEquals(100, viewModel.totalItems.value)
        assertEquals(120000.0, viewModel.totalPrice.value)
    }

    @Test
    fun `subtotal should be calculated correctly for cart items`() {
        // Given
        val product = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)

        // When
        viewModel.addToCart(product, 3)

        // Then
        val cartItem = viewModel.cartItems.value[0]
        assertEquals(3600.0, cartItem.subtotal)
    }

    @Test
    fun `multiple operations should maintain correct state`() {
        // Given
        val product1 = Product(id = 1, name = "Manzana", price = 1200.0, priceLabel = "$1.200", imagesRes = 1, category = ProductCategory.frutas)
        val product2 = Product(id = 2, name = "Lechuga", price = 900.0, priceLabel = "$900", imagesRes = 2, category = ProductCategory.verduras)

        // When
        viewModel.addToCart(product1, 2)
        viewModel.addToCart(product2, 3)
        viewModel.updateQuantity(1, 5)
        viewModel.removeFromCart(2)
        viewModel.addToCart(product2, 1)

        // Then
        assertEquals(2, viewModel.cartItems.value.size)
        assertEquals(6, viewModel.totalItems.value)
        assertEquals(6900.0, viewModel.totalPrice.value)
    }
}
