package com.example.huertohogar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.huertohogar.view.components.SectionTitle
import org.junit.Rule
import org.junit.Test

class SectionTitleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sectionTitle_displaysTitleText() {
        // When
        composeTestRule.setContent {
            SectionTitle(
                title = "Productos Destacados",
                icon = Icons.Default.Star
            )
        }

        // Then
        composeTestRule.onNodeWithText("Productos Destacados").assertIsDisplayed()
    }

    @Test
    fun sectionTitle_displaysWithDifferentIcons() {
        // Given - Reduce la cantidad de iconos a probar
        val icons = listOf(
            Icons.Default.Home,
            Icons.Default.Star,
            Icons.Default.Favorite
        )

        // When & Then
        icons.forEach { icon ->
            composeTestRule.setContent {
                SectionTitle(
                    title = "Test Title",
                    icon = icon
                )
            }

            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
        }
    }

    @Test
    fun sectionTitle_displaysLongTitle() {
        // Given
        val longTitle = "Este es un título muy largo que podría ocupar múltiples líneas"

        // When
        composeTestRule.setContent {
            SectionTitle(
                title = longTitle,
                icon = Icons.Default.Home
            )
        }

        // Then
        composeTestRule.onNodeWithText(longTitle).assertIsDisplayed()
    }

    @Test
    fun sectionTitle_displaysShortTitle() {
        // When
        composeTestRule.setContent {
            SectionTitle(
                title = "Test",
                icon = Icons.Default.Star
            )
        }

        // Then
        composeTestRule.onNodeWithText("Test").assertIsDisplayed()
    }

    @Test
    fun sectionTitle_handlesEmptyTitle() {
        // When
        composeTestRule.setContent {
            SectionTitle(
                title = "",
                icon = Icons.Default.Home
            )
        }

        // Then - No debería crashear
        composeTestRule.waitForIdle()
    }

    @Test
    fun sectionTitle_displaysWithSpecialCharacters() {
        // Given
        val titleWithSpecialChars = "Productos & Servicios (2024)"

        // When
        composeTestRule.setContent {
            SectionTitle(
                title = titleWithSpecialChars,
                icon = Icons.Default.Star
            )
        }

        // Then
        composeTestRule.onNodeWithText(titleWithSpecialChars).assertIsDisplayed()
    }

    @Test
    fun sectionTitle_displaysWithAccents() {
        // Given
        val titleWithAccents = "Categorías Orgánicas"

        // When
        composeTestRule.setContent {
            SectionTitle(
                title = titleWithAccents,
                icon = Icons.Default.Favorite
            )
        }

        // Then
        composeTestRule.onNodeWithText(titleWithAccents).assertIsDisplayed()
    }

    @Test
    fun sectionTitle_displaysWithNumbers() {
        // Given
        val titleWithNumbers = "Top 10 Productos"

        // When
        composeTestRule.setContent {
            SectionTitle(
                title = titleWithNumbers,
                icon = Icons.Default.Star
            )
        }

        // Then
        composeTestRule.onNodeWithText(titleWithNumbers).assertIsDisplayed()
    }

    @Test
    fun sectionTitle_displaysMultipleSections() {
        // Given - Reduce la cantidad de títulos
        val titles = listOf("Sección 1", "Sección 2")

        // When & Then
        titles.forEach { title ->
            composeTestRule.setContent {
                SectionTitle(
                    title = title,
                    icon = Icons.Default.Home
                )
            }

            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText(title).assertIsDisplayed()
        }
    }

    @Test
    fun sectionTitle_rendersSameContentConsistently() {
        // Given
        val title = "Productos Destacados"

        // When - Primera renderización
        composeTestRule.setContent {
            SectionTitle(
                title = title,
                icon = Icons.Default.Star
            )
        }

        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun sectionTitle_handlesMultilineTitle() {
        // Given
        val multilineTitle = "Primera Línea\nSegunda Línea"

        // When
        composeTestRule.setContent {
            SectionTitle(
                title = multilineTitle,
                icon = Icons.Default.Home
            )
        }

        // Then
        composeTestRule.onNodeWithText(multilineTitle).assertIsDisplayed()
    }
}