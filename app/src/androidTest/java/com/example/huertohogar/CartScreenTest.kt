package com.example.huertohogar


import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.viewmodel.CartViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel

    private fun createMockProduct(id: Int = 1) = Product(
        id = id,
        name = "Test Product",
        imagesRes = 0,
        price = 1000.0,
        priceLabel = "$1.000",
        category = ProductCategory.verduras
    )

    @Before
    fun setup() {
        viewModel = CartViewModel()
    }

    @Test
    fun addToCart_AddsProductToCart() = runTest {
        // Arrange
        val product = createMockProduct()

        // Act
        viewModel.addToCart(product, 1)

        // Assert
        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(1, viewModel.totalItems.value)
    }

    @Test
    fun addToCart_ExistingProduct_UpdatesQuantity() = runTest {
        // Arrange
        val product = createMockProduct()
        viewModel.addToCart(product, 1)

        // Act
        viewModel.addToCart(product, 2)

        // Assert
        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(3, viewModel.cartItems.value.first().quantity)
    }

    @Test
    fun removeFromCart_RemovesProduct() = runTest {
        // Arrange
        val product = createMockProduct()
        viewModel.addToCart(product, 1)

        // Act
        viewModel.removeFromCart(product.id)

        // Assert
        assertEquals(0, viewModel.cartItems.value.size)
    }

    @Test
    fun updateQuantity_UpdatesProductQuantity() = runTest {
        // Arrange
        val product = createMockProduct()
        viewModel.addToCart(product, 1)

        // Act
        viewModel.updateQuantity(product.id, 5)

        // Assert
        assertEquals(5, viewModel.cartItems.value.first().quantity)
    }

    @Test
    fun updateQuantity_ZeroOrNegative_RemovesProduct() = runTest {
        // Arrange
        val product = createMockProduct()
        viewModel.addToCart(product, 1)

        // Act
        viewModel.updateQuantity(product.id, 0)

        // Assert
        assertEquals(0, viewModel.cartItems.value.size)
    }

    @Test
    fun clearCart_RemovesAllProducts() = runTest {
        // Arrange
        viewModel.addToCart(createMockProduct(1), 1)
        viewModel.addToCart(createMockProduct(2), 1)

        // Act
        viewModel.clearCart()

        // Assert
        assertEquals(0, viewModel.cartItems.value.size)
        assertEquals(0, viewModel.totalItems.value)
        assertEquals(0.0, viewModel.totalPrice.value, 0.01)
    }

    @Test
    fun totalPrice_CalculatesCorrectly() = runTest {
        // Arrange
        val product = createMockProduct()

        // Act
        viewModel.addToCart(product, 3)

        // Assert
        assertEquals(3000.0, viewModel.totalPrice.value, 0.01)
    }
}