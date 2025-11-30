package com.example.huertohogar.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.huertohogar.model.Product

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
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
            Text(product.priceLabel, modifier = Modifier.padding(bottom = 8.dp))
        }
    }
}

@Composable
fun FeaturedProductsRow(
    products: List<Product>,
    onProductClick: (Product) -> Unit
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(products){
                product -> ProductCard(product = product, onClick = { onProductClick(product)})
        }
    }
}