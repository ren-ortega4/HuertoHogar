package com.example.huertohogar

import com.example.huertohogar.view.screen.LottieSplashScreen
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class LottieSplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lottieSplashScreen_rendersSuccessfully() {
        // Given
        var animationFinished = false

        // When
        composeTestRule.setContent {
            LottieSplashScreen(
                onAnimationFinished = { animationFinished = true }
            )
        }

        // Then - Solo espera a que se renderice
        composeTestRule.waitForIdle()
    }

    @Test
    fun lottieSplashScreen_doesNotCallCallbackImmediately() {
        // Given
        var callbackCalled = false

        // When
        composeTestRule.setContent {
            LottieSplashScreen(
                onAnimationFinished = { callbackCalled = true }
            )
        }

        composeTestRule.waitForIdle()

        // Then - El callback no debería llamarse inmediatamente
        assert(!callbackCalled)
    }

    @Test
    fun lottieSplashScreen_acceptsCallback() {
        // Given
        val callback: () -> Unit = { }

        // When
        composeTestRule.setContent {
            LottieSplashScreen(onAnimationFinished = callback)
        }

        // Then - No debería lanzar excepciones
        composeTestRule.waitForIdle()
    }
}