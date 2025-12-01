package com.example.huertohogar


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertHasClickAction
import com.example.huertohogar.view.components.CallToActionCard
import org.junit.Rule
import org.junit.Test

class CallToActionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun callToActionCard_displaysTitle() {
        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "Test Title",
                buttonText = "Test Button",
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
    }

    @Test
    fun callToActionCard_displaysButtonText() {
        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "Title",
                buttonText = "Click Here",
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Click Here").assertIsDisplayed()
    }

    @Test
    fun callToActionCard_isClickable() {
        // Given
        var clicked = false

        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "Clickable Card",
                buttonText = "Button",
                onClick = { clicked = true }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Clickable Card").assertHasClickAction()
    }

    @Test
    fun callToActionCard_triggersOnClick() {
        // Given
        var clickCount = 0

        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "Click Me",
                buttonText = "Action",
                onClick = { clickCount++ }
            )
        }

        composeTestRule.onNodeWithText("Click Me").performClick()

        // Then
        assert(clickCount == 1)
    }

    @Test
    fun callToActionCard_handlesMultipleClicks() {
        // Given
        var clickCount = 0

        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "Multiple Clicks",
                buttonText = "Button",
                onClick = { clickCount++ }
            )
        }

        composeTestRule.onNodeWithText("Multiple Clicks").apply {
            performClick()
            performClick()
            performClick()
        }

        // Then
        assert(clickCount == 3)
    }

    @Test
    fun callToActionCard_displaysAllContent() {
        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "Full Title",
                buttonText = "Full Button Text",
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Full Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Full Button Text").assertIsDisplayed()
    }

    @Test
    fun callToActionCard_handlesEmptyStrings() {
        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = "",
                buttonText = "",
                onClick = {}
            )
        }

        // Then - No debería crashear
        composeTestRule.waitForIdle()
    }

    @Test
    fun callToActionCard_handlesLongText() {
        // Given
        val longTitle = "This is a very long title that might wrap to multiple lines"
        val longButton = "This is a very long button text that should also display properly"

        // When
        composeTestRule.setContent {
            CallToActionCard(
                title = longTitle,
                buttonText = longButton,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(longTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(longButton).assertIsDisplayed()
    }
}