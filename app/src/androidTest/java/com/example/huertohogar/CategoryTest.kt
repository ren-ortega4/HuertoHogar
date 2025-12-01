package com.example.huertohogar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertHasClickAction
import com.example.huertohogar.R
import com.example.huertohogar.view.components.CategoryCard
import org.junit.Rule
import org.junit.Test

class CategoryCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun categoryCard_displaysCategoryName() {
        // When
        composeTestRule.setContent {
            CategoryCard(
                name = "Vegetables",
                imageRes = R.drawable.fondocall,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Vegetables").assertIsDisplayed()
    }

    @Test
    fun categoryCard_hasContentDescription() {
        // When
        composeTestRule.setContent {
            CategoryCard(
                name = "Fruits",
                imageRes = R.drawable.fondocall,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Fruits").assertIsDisplayed()
    }

    @Test
    fun categoryCard_isClickable() {
        // When
        composeTestRule.setContent {
            CategoryCard(
                name = "Herbs",
                imageRes = R.drawable.fondocall,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Herbs").assertHasClickAction()
    }

    @Test
    fun categoryCard_triggersOnClick() {
        // Given
        var clicked = false

        // When
        composeTestRule.setContent {
            CategoryCard(
                name = "Seeds",
                imageRes = R.drawable.fondocall,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText("Seeds").performClick()

        // Then
        assert(clicked)
    }

    @Test
    fun categoryCard_handlesMultipleClicks() {
        // Given
        var clickCount = 0

        // When
        composeTestRule.setContent {
            CategoryCard(
                name = "Tools",
                imageRes = R.drawable.fondocall,
                onClick = { clickCount++ }
            )
        }

        composeTestRule.onNodeWithText("Tools").apply {
            performClick()
            performClick()
            performClick()
        }

        // Then
        assert(clickCount == 3)
    }

    @Test
    fun categoryCard_handlesLongCategoryName() {
        // Given
        val longName = "Very Long Category Name That Might Wrap"

        // When
        composeTestRule.setContent {
            CategoryCard(
                name = longName,
                imageRes = R.drawable.fondocall,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(longName).assertIsDisplayed()
    }

    @Test
    fun categoryCard_handlesEmptyName() {
        // When
        composeTestRule.setContent {
            CategoryCard(
                name = "",
                imageRes = R.drawable.fondocall,
                onClick = {}
            )
        }

        // Then - No debería crashear
        composeTestRule.waitForIdle()
    }

    @Test
    fun categoryCard_handlesSpecialCharacters() {
        // Given
        val specialName = "Café & Té"

        // When
        composeTestRule.setContent {
            CategoryCard(
                name = specialName,
                imageRes = R.drawable.fondocall,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(specialName).assertIsDisplayed()
    }
}