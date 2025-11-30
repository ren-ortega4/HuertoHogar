package com.example.huertohogar.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun LottieSplashScreen(onAnimationFinished: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("huertohogar.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    var showIndicator by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if (progress >= 1.0f) {
            showIndicator = true
            delay(2000) // Muestra el indicador por 2 segundos
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // La animación Lottie llenará toda la pantalla, actuando como el fondo.
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds // Escala para que llene los bordes
        )

        // Contenedor para el indicador de progreso en la parte inferior.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Margen inferior
            contentAlignment = Alignment.BottomCenter
        ) {
            if (showIndicator) {
                // Indicador de carga circular. Es el estándar para animaciones de carga.
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color.White,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}
