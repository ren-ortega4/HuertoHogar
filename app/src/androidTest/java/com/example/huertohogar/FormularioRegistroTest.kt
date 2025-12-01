package com.example.huertohogar


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.huertohogar.view.components.LoadingDialog
import org.junit.Rule
import org.junit.Test

class FormularioRegistroTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingDialog_displaysTitle() {
        // When
        composeTestRule.setContent {
            LoadingDialog(dialogMessage = "Test message")
        }

        // Then
        composeTestRule.onNodeWithText("Procesando registro").assertIsDisplayed()
    }

    @Test
    fun loadingDialog_displaysMessage() {
        // When
        composeTestRule.setContent {
            LoadingDialog(dialogMessage = "Guardando datos...")
        }

        // Then
        composeTestRule.onNodeWithText("Guardando datos...").assertIsDisplayed()
    }

    @Test
    fun loadingDialog_displaysCustomMessage() {
        // Given
        val customMessage = "Cargando información del usuario"

        // When
        composeTestRule.setContent {
            LoadingDialog(dialogMessage = customMessage)
        }

        // Then
        composeTestRule.onNodeWithText(customMessage).assertIsDisplayed()
    }

    @Test
    fun loadingDialog_displaysEmptyMessage() {
        // When
        composeTestRule.setContent {
            LoadingDialog(dialogMessage = "")
        }

        // Then - No debería crashear
        composeTestRule.onNodeWithText("Procesando registro").assertIsDisplayed()
    }

    @Test
    fun loadingDialog_displaysLongMessage() {
        // Given
        val longMessage = "Este es un mensaje muy largo que podría ocupar múltiples líneas en el diálogo"

        // When
        composeTestRule.setContent {
            LoadingDialog(dialogMessage = longMessage)
        }

        // Then
        composeTestRule.onNodeWithText(longMessage).assertIsDisplayed()
    }
}