package com.example.huertohogar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.view.components.FeaturedProductsRow
import org.junit.Rule
import org.junit.Test

class FeaturedProductsRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createSampleProducts(): List<Product> {
        return listOf(
            Product(1, "Tomate", 1500.0, "$1.500", R.drawable.fondocall, ProductCategory.verduras),
            Product(2, "Lechuga", 1200.0, "$1.200", R.drawable.fondocall, ProductCategory.verduras),
            Product(3, "Zanahoria", 800.0, "$800", R.drawable.fondocall, ProductCategory.verduras)
        )
    }

    @Test
    fun featuredProductsRow_displaysAllProducts() {
        // Given
        val products = createSampleProducts()

        // When
        composeTestRule.setContent {
            FeaturedProductsRow (products = products, onProductClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("Tomate").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lechuga").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zanahoria").assertIsDisplayed()
    }

    @Test
    fun featuredProductsRow_displaysAllPrices() {
        // Given
        val products = createSampleProducts()

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(products = products, onProductClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("$1.500").assertIsDisplayed()
        composeTestRule.onNodeWithText("$1.200").assertIsDisplayed()
        composeTestRule.onNodeWithText("$800").assertIsDisplayed()
    }

    @Test
    fun featuredProductsRow_triggersClickOnProduct() {
        // Given
        val products = createSampleProducts()
        var clickedProduct: Product? = null

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(
                products = products,
                onProductClick = { clickedProduct = it }
            )
        }

        composeTestRule.onNodeWithText("Tomate").performClick()

        // Then
        assert(clickedProduct?.name == "Tomate")
    }

    @Test
    fun featuredProductsRow_handlesEmptyList() {
        // When
        composeTestRule.setContent {
            FeaturedProductsRow(products = emptyList(), onProductClick = {})
        }

        // Then - No debería crashear
        composeTestRule.waitForIdle()
    }

    @Test
    fun featuredProductsRow_handlesSingleProduct() {
        // Given
        val products = listOf(
            Product(1, "Solo Producto", 1000.0, "$1.000", R.drawable.fondocall, ProductCategory.frutas)
        )

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(products = products, onProductClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("Solo Producto").assertIsDisplayed()
        composeTestRule.onNodeWithText("$1.000").assertIsDisplayed()
    }

    @Test
    fun featuredProductsRow_handlesMultipleClicks() {
        // Given
        val products = createSampleProducts()
        val clickedProducts = mutableListOf<Product>()

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(
                products = products,
                onProductClick = { clickedProducts.add(it) }
            )
        }

        composeTestRule.onNodeWithText("Tomate").performClick()
        composeTestRule.onNodeWithText("Lechuga").performClick()
        composeTestRule.onNodeWithText("Zanahoria").performClick()

        // Then
        assert(clickedProducts.size == 3)
        assert(clickedProducts[0].name == "Tomate")
        assert(clickedProducts[1].name == "Lechuga")
        assert(clickedProducts[2].name == "Zanahoria")
    }

    @Test
    fun featuredProductsRow_handlesLargeList() {
        // Given
        val products = (1..20).map {
            Product(
                id = it,
                name = "Producto $it",
                price = it * 1000.0,
                priceLabel = "$${it}.000",
                imagesRes = R.drawable.fondocall,
                category = ProductCategory.otros
            )
        }

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(products = products, onProductClick = {})
        }

        // Then - Al menos el primero debería estar visible
        composeTestRule.onNodeWithText("Producto 1").assertIsDisplayed()
    }

    @Test
    fun featuredProductsRow_clicksDifferentProducts() {
        // Given
        val products = createSampleProducts()
        var lastClickedProduct: Product? = null

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(
                products = products,
                onProductClick = { lastClickedProduct = it }
            )
        }

        composeTestRule.onNodeWithText("Tomate").performClick()
        assert(lastClickedProduct?.name == "Tomate")

        composeTestRule.onNodeWithText("Lechuga").performClick()
        assert(lastClickedProduct?.name == "Lechuga")
    }

    @Test
    fun featuredProductsRow_displaysProductsFromDifferentCategories() {
        // Given
        val products = listOf(
            Product(1, "Manzana", 1000.0, "$1.000", R.drawable.fondocall, ProductCategory.frutas),
            Product(2, "Tomate", 1500.0, "$1.500", R.drawable.fondocall, ProductCategory.verduras),
            Product(3, "Quinoa", 3000.0, "$3.000", R.drawable.fondocall, ProductCategory.productosOrganicos),
            Product(4, "Yogurt", 2000.0, "$2.000", R.drawable.fondocall, ProductCategory.lacteos)
        )

        // When
        composeTestRule.setContent {
            FeaturedProductsRow(products = products, onProductClick = {})
        }

        // Then - Verifica solo los primeros elementos visibles
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Manzana").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tomate").assertIsDisplayed()
    }
}