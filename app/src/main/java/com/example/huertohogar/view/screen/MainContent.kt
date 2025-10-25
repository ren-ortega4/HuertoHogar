package com.example.huertohogar.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogar.R
import com.example.huertohogar.view.components.CategoryCard
import com.example.huertohogar.view.components.FeaturedProductsRow
import com.example.huertohogar.view.components.TipCard
import com.example.huertohogar.view.components.WelcomeCard
import com.example.huertohogar.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    onNavigateToProducts: () -> Unit = {}
) {
    val uiState by mainViewModel.uiState.collectAsState()

    val isDark = isSystemInDarkTheme()

    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(
                if (isDark) R.drawable.fondooscuro else R.drawable.fondoblanco
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeCard(modifier = Modifier.padding(bottom = 16.dp))
            TipCard("🌱 Tip del día: Riega tus plantas temprano para conservar humedad.", modifier = Modifier.padding(bottom = 20.dp))


            Spacer(Modifier.height(18.dp))
            Text("Productos destacados", color = Color(0xFF388E3C))
            Spacer(Modifier.height(8.dp))
            FeaturedProductsRow(
                products = uiState.featuredProducts,
                onProductClick = {
                    product ->
                    println("Producto clickeado: ${product.name}")
                }
            )

            Spacer(Modifier.height(16.dp))
            androidx.compose.material3.Button(
                onClick = onNavigateToProducts,
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF388E3C)
                )
            ) {
                androidx.compose.material3.Text(
                    "Ver Todos los Productos por Categoría",
                    color = Color.White
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("Categorías", color = Color(0xFF388E3C))
            Spacer(Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.categories){
                    category ->
                    CategoryCard(
                        name = category.first,
                        imageRes = category.second,
                        onClick = {}
                    )
                }
            }
        }
    }
}