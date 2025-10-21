package com.example.huertohogar.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(24.dp)) {
            Text(
                "Bienvenido a HuertoHogar",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
            Text(
                "Descubre productos frescos, aprendizajes, culturas desde el campo a tu hogar.",
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
    }
}