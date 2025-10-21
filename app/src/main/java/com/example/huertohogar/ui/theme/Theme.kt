package com.example.huertohogar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF80C683),
    background = Color(0xFF1B1B1B),
    surface = Color(0xFF232323),
    onPrimary = Color.Black
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF388E3C),
    background = Color(0xFFF8FFF7),
    surface = Color.White,
    onPrimary = Color.White
)

@Composable
fun HuertoHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )

}

