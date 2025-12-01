package com.example.huertohogar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import com.example.huertohogar.model.Tip
import com.example.huertohogar.view.components.TipCard
import com.example.huertohogar.view.components.getIconForName
import org.junit.Rule
import org.junit.Test

class TipCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createSampleTip(): Tip {
        return Tip(
            id = 1,
            title = "Consejo de Riego",
            text = "Riega tus plantas temprano en la mañana",
            iconName = "ThumbUp"
        )
    }

    @Test
    fun tipCard_displaysTipTitle() {
        // Given
        val tip = createSampleTip()

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Consejo de Riego").assertIsDisplayed()
    }

    @Test
    fun tipCard_displaysTipText() {
        // Given
        val tip = createSampleTip()

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Riega tus plantas temprano en la mañana").assertIsDisplayed()
    }

    @Test
    fun tipCard_displaysIcon() {
        // Given
        val tip = createSampleTip()

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Icono del Tip").assertIsDisplayed()
    }

    @Test
    fun tipCard_displaysAllContent() {
        // Given
        val tip = createSampleTip()

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Consejo de Riego").assertIsDisplayed()
        composeTestRule.onNodeWithText("Riega tus plantas temprano en la mañana").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Icono del Tip").assertIsDisplayed()
    }

    @Test
    fun tipCard_handlesLongTitle() {
        // Given
        val tip = Tip(
            id = 1,
            title = "Este es un título muy largo que podría necesitar ser truncado",
            text = "Texto del consejo",
            iconName = "Info"
        )

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Este es un título muy largo que podría necesitar ser truncado").assertIsDisplayed()
    }

    @Test
    fun tipCard_handlesLongText() {
        // Given
        val tip = Tip(
            id = 1,
            title = "Consejo",
            text = "Este es un texto muy largo que contiene mucha información útil " +
                    "sobre cómo cuidar tus plantas de manera efectiva y obtener los mejores resultados",
            iconName = "Info"
        )

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Consejo").assertIsDisplayed()
    }

    @Test
    fun tipCard_handlesDifferentIcons() {
        // Given - Reduce la cantidad de iconos a probar
        val iconNames = listOf("LocalOffer", "ThumbUp", "Info")

        iconNames.forEach { iconName ->
            val tip = Tip(
                id = 1,
                title = "Test $iconName",
                text = "Test text",
                iconName = iconName
            )

            // When
            composeTestRule.setContent {
                TipCard(tip = tip)
            }

            composeTestRule.waitForIdle()

            // Then
            composeTestRule.onNodeWithText("Test $iconName").assertIsDisplayed()
        }
    }

    @Test
    fun tipCard_handlesEmptyTitle() {
        // Given
        val tip = Tip(
            id = 1,
            title = "",
            text = "Texto del consejo",
            iconName = "Info"
        )

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Texto del consejo").assertIsDisplayed()
    }

    @Test
    fun tipCard_handlesEmptyText() {
        // Given
        val tip = Tip(
            id = 1,
            title = "Título",
            text = "",
            iconName = "Info"
        )

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Título").assertIsDisplayed()
    }

    @Test
    fun tipCard_handlesSpecialCharacters() {
        // Given
        val tip = Tip(
            id = 1,
            title = "Consejo & Tip",
            text = "¡Importante! Riega las plantas @ 7am",
            iconName = "Info"
        )

        // When
        composeTestRule.setContent {
            TipCard(tip = tip)
        }

        // Then
        composeTestRule.onNodeWithText("Consejo & Tip").assertIsDisplayed()
        composeTestRule.onNodeWithText("¡Importante! Riega las plantas @ 7am").assertIsDisplayed()
    }

    @Test
    fun getIconForName_returnsLocalOfferIcon() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("LocalOffer")
            // Then
            assert(icon == Icons.Filled.LocalOffer)
        }
    }

    @Test
    fun getIconForName_returnsThumbUpIcon() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("ThumbUp")
            // Then
            assert(icon == Icons.Filled.ThumbUp)
        }
    }

    @Test
    fun getIconForName_returnsStorefrontIcon() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("Storefront")
            // Then
            assert(icon == Icons.Filled.Storefront)
        }
    }

    @Test
    fun getIconForName_returnsCallIcon() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("Call")
            // Then
            assert(icon == Icons.Filled.Call)
        }
    }

    @Test
    fun getIconForName_returnsErrorIcon() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("Error")
            // Then
            assert(icon == Icons.Filled.Error)
        }
    }

    @Test
    fun getIconForName_returnsDefaultInfoIcon() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("UnknownIcon")
            // Then
            assert(icon == Icons.Filled.Info)
        }
    }

    @Test
    fun getIconForName_handlesEmptyString() {
        // When
        composeTestRule.setContent {
            val icon = getIconForName("")
            // Then
            assert(icon == Icons.Filled.Info)
        }
    }
}