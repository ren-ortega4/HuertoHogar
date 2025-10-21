package com.example.huertohogar.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CategoryCard(name: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = name,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            )
            Text(name, modifier = Modifier.padding(8.dp))
        }
    }
}