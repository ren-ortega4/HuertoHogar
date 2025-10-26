package com.example.huertohogar.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.huertohogar.model.Product

@Composable
fun ProductScreen(product: Product, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick)
    ) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(product.imagesRes),
                contentDescription = product.name,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
            )
            Text(product.name, modifier = Modifier.padding(top = 8.dp))
            Text(product.price, modifier = Modifier.padding(bottom = 8.dp))
        }
    }
}