package com.example.huertohogar

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.huertohogar.view.screen.BottomNavigationBar
import com.example.huertohogar.view.screen.GreenAppBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeContentScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greenAppBar_DisplaysLogoAndSearchField() {
        // Arrange
        var searchText = ""

        // Act
        composeTestRule.setContent {
            GreenAppBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                notificacionesNoLeidas = 0,
                onNotificaionesClick = {},
                onSearchTriggered = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Logo HuertoHogar").assertExists()
        composeTestRule.onNodeWithContentDescription("Buscar").assertExists()
    }

    @Test
    fun greenAppBar_SearchFunctionality() {
        // Arrange
        var searchText = ""

        // Act
        composeTestRule.setContent {
            GreenAppBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                notificacionesNoLeidas = 0,
                onNotificaionesClick = {},
                onSearchTriggered = {}
            )
        }

        // Escribir en el campo de búsqueda
        composeTestRule.onNode(hasText("Buscar productos..."))
            .performTextInput("Tomates")

        // Assert
        assert(searchText == "Tomates")
    }

    @Test
    fun greenAppBar_NotificationBadge_ShowsCorrectCount() {
        // Act
        composeTestRule.setContent {
            GreenAppBar(
                searchText = "",
                onSearchTextChange = {},
                notificacionesNoLeidas = 5,
                onNotificaionesClick = {},
                onSearchTriggered = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("5").assertExists()
    }

    @Test
    fun bottomNavigationBar_AllItemsVisible() {
        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            BottomNavigationBar(navController = navController, cartCount = 0)
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Inicio").assertExists()
        composeTestRule.onNodeWithContentDescription("Productos").assertExists()
        composeTestRule.onNodeWithContentDescription("Carrito").assertExists()
        composeTestRule.onNodeWithContentDescription("Mapa").assertExists()
        composeTestRule.onNodeWithContentDescription("Cuenta").assertExists()
    }

    @Test
    fun bottomNavigationBar_CartBadge_ShowsItemCount() {
        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            BottomNavigationBar(navController = navController, cartCount = 3)
        }

        // Assert
        composeTestRule.onNodeWithText("3").assertExists()
    }
}