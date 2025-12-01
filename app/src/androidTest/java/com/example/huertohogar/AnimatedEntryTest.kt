package com.example.huertohogar

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import com.example.huertohogar.view.components.AnimatedEntry
import org.junit.Rule
import org.junit.Test

class AnimatedEntryTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun animatedEntry_contentNotVisibleImmediately() {
        // When
        composeTestRule.setContent {
            AnimatedEntry (delay = 500) {
                Text("Test Content")
            }
        }

        // Then - El contenido no debería estar visible inmediatamente
        composeTestRule.onNodeWithText("Test Content").assertDoesNotExist()
    }

    @Test
    fun animatedEntry_contentBecomesVisibleAfterDelay() {
        // When
        composeTestRule.setContent {
            AnimatedEntry(delay = 100) {
                Text("Visible Content")
            }
        }

        // Avanza el tiempo
        composeTestRule.mainClock.advanceTimeBy(200)
        composeTestRule.waitForIdle()

        // Then - El contenido debería estar visible después del delay
        composeTestRule.onNodeWithText("Visible Content").assertIsDisplayed()
    }

    @Test
    fun animatedEntry_withZeroDelay_showsContentQuickly() {
        // When
        composeTestRule.setContent {
            AnimatedEntry(delay = 0) {
                Text("Immediate Content")
            }
        }

        // Avanza un poco el tiempo para la animación
        composeTestRule.mainClock.advanceTimeBy(700)
        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("Immediate Content").assertIsDisplayed()
    }

    @Test
    fun animatedEntry_acceptsComposableContent() {
        // When
        composeTestRule.setContent {
            AnimatedEntry(delay = 0) {
                Text("Multiple")
                Text("Components")
            }
        }

        composeTestRule.mainClock.advanceTimeBy(700)
        composeTestRule.waitForIdle()

        // Then - Ambos componentes deberían estar presentes
        composeTestRule.onNodeWithText("Multiple").assertIsDisplayed()
        composeTestRule.onNodeWithText("Components").assertIsDisplayed()
    }

    @Test
    fun animatedEntry_defaultDelayIsZero() {
        // When - Usando el delay por defecto
        composeTestRule.setContent {
            AnimatedEntry {
                Text("Default Delay")
            }
        }

        composeTestRule.mainClock.advanceTimeBy(700)
        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("Default Delay").assertIsDisplayed()
    }
}