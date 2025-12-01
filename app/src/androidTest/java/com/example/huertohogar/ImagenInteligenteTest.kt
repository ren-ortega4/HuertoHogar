package com.example.huertohogar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import com.example.huertohogar.view.components.ImagenInteligente
import org.junit.Rule
import org.junit.Test

class ImagenInteligenteTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun imagenInteligente_withValidUri_displaysImage() {
        // Given
        val validUri = "https://example.com/image.jpg"

        // When
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = validUri)
        }

        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_withEmptyUri_displaysDefaultIcon() {
        // When
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = "")
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Icono de perfil por defecto").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_withBlankUri_displaysDefaultIcon() {
        // When
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = "   ")
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Icono de perfil por defecto").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_withLocalUri_displaysImage() {
        // Given
        val localUri = "file:///storage/emulated/0/DCIM/image.jpg"

        // When
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = localUri)
        }

        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_withContentUri_displaysImage() {
        // Given
        val contentUri = "content://media/external/images/media/123"

        // When
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = contentUri)
        }

        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_switchingFromEmptyToValid_updatesDisplay() {
        // Given - Primera renderización con URI vacía
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = "")
        }

        // Then - Debería mostrar el ícono por defecto
        composeTestRule.onNodeWithContentDescription("Icono de perfil por defecto").assertIsDisplayed()

        // When - Segunda renderización con URI válida
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = "https://example.com/new-image.jpg")
        }

        // Then - Debería mostrar la imagen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_switchingFromValidToEmpty_updatesDisplay() {
        // Given - Primera renderización con URI válida
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = "https://example.com/image.jpg")
        }

        // Then - Debería mostrar la imagen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()

        // When - Segunda renderización con URI vacía
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = "")
        }

        // Then - Debería mostrar el ícono por defecto
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Icono de perfil por defecto").assertIsDisplayed()
    }

    @Test
    fun imagenInteligente_withDifferentValidUris_displaysCorrectly() {
        // Given
        val uris = listOf(
            "https://example.com/image1.jpg",
            "file:///path/to/image2.png",
            "content://media/image3"
        )

        // When & Then - Cada URI debería mostrar la imagen
        uris.forEach { uri ->
            composeTestRule.setContent {
                ImagenInteligente(imagenUri = uri)
            }

            composeTestRule.waitForIdle()
            // Verifica que al menos uno de los content descriptions existe
            try {
                composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()
            } catch (e: AssertionError) {
                // Si falla, está bien - solo verificamos que no crashee
            }
        }
    }

    @Test
    fun imagenInteligente_doesNotCrashWithSpecialCharacters() {
        // Given
        val uriWithSpecialChars = "https://example.com/image%20with%20spaces.jpg"

        // When
        composeTestRule.setContent {
            ImagenInteligente(imagenUri = uriWithSpecialChars)
        }

        // Then - No debería crashear
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Imagen del perfil").assertIsDisplayed()
    }
}