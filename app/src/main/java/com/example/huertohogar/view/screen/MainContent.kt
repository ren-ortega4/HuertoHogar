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
import com.example.huertohogar.viewmodel.TipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    tipViewModel: TipViewModel = viewModel()
) {
    val uiState by mainViewModel.uiState.collectAsState()

    val isDark = isSystemInDarkTheme()

    val currentTip by tipViewModel.currentTip.collectAsState()

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
            TipCard(
                tip = currentTip,
                modifier = Modifier.padding(bottom = 24.dp)
            )


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