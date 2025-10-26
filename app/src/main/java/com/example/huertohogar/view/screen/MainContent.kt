package com.example.huertohogar.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Star
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
import com.example.huertohogar.view.components.AnimatedEntry
import com.example.huertohogar.view.components.CategoryCard
import com.example.huertohogar.view.components.FeaturedProductsRow
import com.example.huertohogar.view.components.SectionTitle
import com.example.huertohogar.view.components.TipCard
import com.example.huertohogar.view.components.WelcomeCard
import com.example.huertohogar.viewmodel.MainViewModel
import com.example.huertohogar.viewmodel.TipViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.huertohogar.view.components.CategoryDetails
import com.example.huertohogar.viewmodel.Category





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    tipViewModel: TipViewModel = viewModel(),
    onNavigateToProducts: () -> Unit = {}
) {
    val uiState by mainViewModel.uiState.collectAsState()

    val isDark = isSystemInDarkTheme()

    val currentTip by tipViewModel.currentTip.collectAsState()

    val backgroundColor = MaterialTheme.colorScheme.background

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

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
            AnimatedEntry (100) { WelcomeCard(modifier = Modifier.padding(bottom = 16.dp)) }
            AnimatedEntry (200) {
                TipCard(
                tip = currentTip,
                modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            AnimatedEntry (300){
                Column (horizontalAlignment = Alignment.Start){
                    Spacer(Modifier.height(18.dp))
                    SectionTitle(
                        title = "Productos Destacados",
                        icon = Icons.Filled.Star,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    FeaturedProductsRow(
                        products = uiState.featuredProducts,
                        onProductClick = {
                                product ->
                            println("Producto clickeado: ${product.name}")
                        }
                    )
                }
            }

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

            AnimatedEntry(400) {
                Column(horizontalAlignment = Alignment.Start) {
                    Spacer(Modifier.height(24.dp))
                    SectionTitle(
                        title = "Categorías",
                        icon = Icons.Filled.Category,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(uiState.categories){
                                category ->
                            CategoryCard(
                                name = category.name,
                                imageRes = category.imageRes,
                                onClick = {selectedCategory = category}
                            )
                        }
                    }
                }
            }
            selectedCategory?.let { category ->
                CategoryDetails(
                    categoryName = category.name,
                    categoryImageRes = category.imageRes,
                    categoryDescription = category.description,
                    onDismiss = {selectedCategory = null}
                )
            }
        }
    }
}