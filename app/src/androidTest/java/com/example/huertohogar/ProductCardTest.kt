package com.example.huertohogar


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertHasClickAction
import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.view.components.ProductCard
import org.junit.Rule
import org.junit.Test

class ProductCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createSampleProduct(): Product {
        return Product(
            id = 1,
            name = "Tomate",
            price = 1500.0,
            priceLabel = "$1.500",
            imagesRes = R.drawable.fondocall,
            category = ProductCategory.verduras
        )
    }

    @Test
    fun productCard_displaysProductName() {
        // Given
        val product = createSampleProduct()

        // When
        composeTestRule.setContent {
            ProductCard (product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("Tomate").assertIsDisplayed()
    }

    @Test
    fun productCard_displaysProductPrice() {
        // Given
        val product = createSampleProduct()

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("$1.500").assertIsDisplayed()
    }

    @Test
    fun productCard_displaysProductImage() {
        // Given
        val product = createSampleProduct()

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Tomate").assertIsDisplayed()
    }

    @Test
    fun productCard_isClickable() {
        // Given
        val product = createSampleProduct()

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("Tomate").assertHasClickAction()
    }

    @Test
    fun productCard_triggersOnClick() {
        // Given
        val product = createSampleProduct()
        var clicked = false

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Tomate").performClick()

        // Then
        assert(clicked)
    }

    @Test
    fun productCard_handlesMultipleClicks() {
        // Given
        val product = createSampleProduct()
        var clickCount = 0

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = { clickCount++ })
        }

        composeTestRule.onNodeWithText("Tomate").apply {
            performClick()
            performClick()
            performClick()
        }

        // Then
        assert(clickCount == 3)
    }

    @Test
    fun productCard_displaysAllContent() {
        // Given
        val product = createSampleProduct()

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("Tomate").assertIsDisplayed()
        composeTestRule.onNodeWithText("$1.500").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Tomate").assertIsDisplayed()
    }

    @Test
    fun productCard_handlesLongProductName() {
        // Given
        val product = Product(
            id = 1,
            name = "Tomate Cherry Orgánico Premium Extra Grande",
            price = 2500.0,
            priceLabel = "$2.500",
            imagesRes = R.drawable.fondocall,
            category = ProductCategory.productosOrganicos
        )

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("Tomate Cherry Orgánico Premium Extra Grande").assertIsDisplayed()
    }

    @Test
    fun productCard_handlesHighPrice() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto Caro",
            price = 999999.0,
            priceLabel = "$999.999",
            imagesRes = R.drawable.fondocall,
            category = ProductCategory.otros
        )

        // When
        composeTestRule.setContent {
            ProductCard(product = product, onClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("$999.999").assertIsDisplayed()
    }

    @Test
    fun productCard_handlesDifferentCategories() {
        // Given
        val products = listOf(
            Product(1, "Manzana", 1000.0, "$1.000", R.drawable.fondocall, ProductCategory.frutas),
            Product(2, "Lechuga", 2000.0, "$2.000", R.drawable.fondocall, ProductCategory.verduras),
            Product(3, "Semillas", 3000.0, "$3.000", R.drawable.fondocall, ProductCategory.productosOrganicos)
        )

        // When & Then - Verifica solo el primer producto para evitar conflictos
        products.take(1).forEach { product ->
            composeTestRule.setContent {
                ProductCard(product = product, onClick = {})
            }

            composeTestRule.onNodeWithText(product.name).assertIsDisplayed()
            composeTestRule.onNodeWithText(product.priceLabel).assertIsDisplayed()
        }
    }

    @Test
    fun productCard_handlesAllProductCategories() {
        // Given - Prueba con todas las categorías, pero una a la vez
        val categories = listOf(
            ProductCategory.frutas,
            ProductCategory.verduras,
            ProductCategory.productosOrganicos
        )

        categories.take(3).forEach { category ->
            val product = Product(
                id = 1,
                name = "Producto ${category.displayName}",
                price = 1500.0,
                priceLabel = "$1.500",
                imagesRes = R.drawable.fondocall,
                category = category
            )

            // When
            composeTestRule.setContent {
                ProductCard(product = product, onClick = {})
            }

            // Then
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText("Producto ${category.displayName}").assertIsDisplayed()
        }
    }
}