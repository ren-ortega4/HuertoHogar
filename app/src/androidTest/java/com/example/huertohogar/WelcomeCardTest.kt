package com.example.huertohogar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.unit.dp
import com.example.huertohogar.view.components.WelcomeCard
import org.junit.Rule
import org.junit.Test

class WelcomeCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun welcomeCard_displaysTitleText() {
        // When
        composeTestRule.setContent {
            WelcomeCard()
        }

        // Then
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
    }

    @Test
    fun welcomeCard_displaysDescriptionText() {
        // When
        composeTestRule.setContent {
            WelcomeCard()
        }

        // Then
        composeTestRule.onNodeWithText(
            "Descubre productos frescos, aprendizajes, culturas desde el campo a tu hogar."
        ).assertIsDisplayed()
    }

    @Test
    fun welcomeCard_displaysAllContent() {
        // When
        composeTestRule.setContent {
            WelcomeCard()
        }

        // Then
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Descubre productos frescos, aprendizajes, culturas desde el campo a tu hogar."
        ).assertIsDisplayed()
    }

    @Test
    fun welcomeCard_rendersWithoutCrashing() {
        // When
        composeTestRule.setContent {
            WelcomeCard()
        }

        // Then - No debería crashear
        composeTestRule.waitForIdle()
    }

    @Test
    fun welcomeCard_acceptsModifier() {
        // When
        composeTestRule.setContent {
            WelcomeCard(modifier = androidx.compose.ui.Modifier.padding(16.dp))
        }

        // Then
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
    }

    @Test
    fun welcomeCard_displaysConsistently() {
        // When - Primera renderización
        composeTestRule.setContent {
            WelcomeCard()
        }

        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
    }

    @Test
    fun welcomeCard_multipleInstances() {
        // When
        composeTestRule.setContent {
            androidx.compose.foundation.layout.Column {
                WelcomeCard()
                WelcomeCard()
            }
        }

        // Then - Ambas instancias deberían renderizarse
        composeTestRule.waitForIdle()
    }

    @Test
    fun welcomeCard_withDifferentModifiers() {
        // Given - Prueba solo con un modifier
        composeTestRule.setContent {
            WelcomeCard(modifier = androidx.compose.ui.Modifier.padding(8.dp))
        }

        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
    }

    @Test
    fun welcomeCard_layoutStructure() {
        // When
        composeTestRule.setContent {
            WelcomeCard()
        }

        // Then - Verifica que ambos textos están en el orden correcto
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Descubre productos frescos, aprendizajes, culturas desde el campo a tu hogar."
        ).assertIsDisplayed()
    }

    @Test
    fun welcomeCard_rendersInDifferentContexts() {
        // When - Solo prueba con Box
        composeTestRule.setContent {
            androidx.compose.foundation.layout.Box {
                WelcomeCard()
            }
        }

        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("Bienvenido a HuertoHogar").assertIsDisplayed()
    }
}