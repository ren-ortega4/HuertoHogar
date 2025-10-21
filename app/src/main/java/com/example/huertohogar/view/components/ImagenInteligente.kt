package com.example.huertohogar.view.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImagenInteligente(imagenUri: Uri?){
    if (imagenUri != null){
        Image(
            painter = rememberAsyncImagePainter(model = imagenUri),
            contentDescription = "Imagen del perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
    } else {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Icono de perfil por defecto",
            tint = Color.Gray,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
    }
}