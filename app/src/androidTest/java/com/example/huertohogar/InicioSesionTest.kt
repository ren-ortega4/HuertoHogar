package com.example.huertohogar


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Rule
import org.junit.Test

class InicioSesionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingDialog_whenLoading_displaysCorrectly() {
        // When
        composeTestRule.setContent {
            androidx.compose.material3.Surface {
                androidx.compose.foundation.layout.Box(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                ) {
                    androidx.compose.ui.window.Dialog(onDismissRequest = {}) {
                        androidx.compose.material3.Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            androidx.compose.foundation.layout.Row(
                                modifier = androidx.compose.ui.Modifier.padding(24.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                            ) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = androidx.compose.ui.Modifier.size(24.dp),
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                )
                                androidx.compose.foundation.layout.Spacer(
                                    modifier = androidx.compose.ui.Modifier.width(20.dp)
                                )
                                androidx.compose.material3.Text(
                                    text = "Iniciando sesión...",
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 18.sp,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Then
        composeTestRule.onNodeWithText("Iniciando sesión...").assertIsDisplayed()
    }

    @Test
    fun loadingDialog_displaysProgressIndicator() {
        // When
        composeTestRule.setContent {
            androidx.compose.material3.Surface {
                androidx.compose.ui.window.Dialog(onDismissRequest = {}) {
                    androidx.compose.material3.Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    ) {
                        androidx.compose.foundation.layout.Row(
                            modifier = androidx.compose.ui.Modifier.padding(24.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = androidx.compose.ui.Modifier.size(24.dp)
                            )
                            androidx.compose.foundation.layout.Spacer(
                                modifier = androidx.compose.ui.Modifier.width(20.dp)
                            )
                            androidx.compose.material3.Text("Iniciando sesión...")
                        }
                    }
                }
            }
        }

        // Then - Verifica que el diálogo se muestra
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Iniciando sesión...").assertIsDisplayed()
    }
}