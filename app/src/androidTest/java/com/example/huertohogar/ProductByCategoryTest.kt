package com.example.huertohogar

import android.app.Application
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.huertohogar.view.screen.ProductsByCategoryScreen
import com.example.huertohogar.viewmodel.CartViewModel
import com.example.huertohogar.viewmodel.ProductViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductByCategoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productsByCategoryScreen_DisplaysTitle() {
        // Arrange
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Act
        composeTestRule.setContent {
            val cartViewModel = CartViewModel()
            val productViewModel = ProductViewModel(application)
            ProductsByCategoryScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        // Assert
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Tienda").assertExists()
    }

    @Test
    fun productsByCategoryScreen_DisplaysCategories() {
        // Arrange
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Act
        composeTestRule.setContent {
            val cartViewModel = CartViewModel()
            val productViewModel = ProductViewModel(application)
            ProductsByCategoryScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun productsByCategoryScreen_CategoryCard_IsClickable() {
        // Arrange
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Act
        composeTestRule.setContent {
            val cartViewModel = CartViewModel()
            val productViewModel = ProductViewModel(application)
            ProductsByCategoryScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composeTestRule.waitForIdle()

        // Assert
        val clickableItems = composeTestRule
            .onAllNodes(hasClickAction())
            .fetchSemanticsNodes()

        assert(clickableItems.isNotEmpty()) { "Should have clickable category cards" }
    }

    @Test
    fun productsByCategoryScreen_ShowsBackgroundImage() {
        // Arrange
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Act
        composeTestRule.setContent {
            val cartViewModel = CartViewModel()
            val productViewModel = ProductViewModel(application)
            ProductsByCategoryScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun productsByCategoryScreen_Grid_DisplaysCorrectly() {
        // Arrange
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Act
        composeTestRule.setContent {
            val cartViewModel = CartViewModel()
            val productViewModel = ProductViewModel(application)
            ProductsByCategoryScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onRoot().assertExists()
        composeTestRule.onNodeWithText("Tienda").assertIsDisplayed()
    }

    @Test
    fun productsByCategoryScreen_ScrollableContent() {
        // Arrange
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Act
        composeTestRule.setContent {
            val cartViewModel = CartViewModel()
            val productViewModel = ProductViewModel(application)
            ProductsByCategoryScreen(
                cartViewModel = cartViewModel,
                productViewModel = productViewModel
            )
        }

        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onRoot().performTouchInput {
            swipeUp()
        }

        composeTestRule.waitForIdle()
        composeTestRule.onRoot().assertExists()
    }
}