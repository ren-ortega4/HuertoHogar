package com.example.huertohogar.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.viewmodel.CartViewModel
import com.example.huertohogar.viewmodel.ProductViewModel


@Composable
fun ProductsByCategoryScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel = viewModel(),
    onProductClick: (Product) -> Unit = {}
) {
    // 1. Recolectar todos los estados necesarios del ViewModel
    val categories by productViewModel.allCategories.collectAsState()
    val productsByCategory by productViewModel.productsByCategory.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val searchedProducts by productViewModel.allProducts.collectAsState()
    val searchQuery by productViewModel.searchQuery.collectAsState()

    val isDark = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(if (isDark) R.drawable.fondooscuro else R.drawable.fondoblanco),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Definimos una base de 2 columnas
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Item 1: Título Principal (ocupa las 2 columnas) ---
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Tienda",
                    style = MaterialTheme.typography.headlineLarge, // Más grande
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
            }

            // --- Lógica condicional: ¿Mostramos Búsqueda o Categorías? ---
            if (searchQuery.isNotBlank()) {
                // --- VISTA DE BÚSQUEDA ---
                if (searchedProducts.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "No se encontraron productos para tu búsqueda.",
                            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(searchedProducts) { product ->
                        ProductCard(
                            product = product,
                            cartViewModel = cartViewModel,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            } else {
                // --- VISTA POR CATEGORÍAS ---
                // Sección de Categorías
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = { productViewModel.setSelectedCategory(category) }
                    )
                }

                // Título de la sección de productos (ocupa las 2 columnas)
                if (selectedCategory != null) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "Productos en ${selectedCategory!!.displayName}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                        )
                    }
                }

                // Sección de Productos
                if (productsByCategory.isEmpty() && selectedCategory != null) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "No hay productos en esta categoría.",
                            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(productsByCategory) { product ->
                        ProductCard(
                            product = product,
                            cartViewModel = cartViewModel,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}

// TU COMPOSABLE ProductCard (no necesita cambios)
@Composable
fun ProductCard(
    product: Product,
    cartViewModel: CartViewModel,
    onClick: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clickable(onClick = onClick),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Image(

                    painter = painterResource(product.imagesRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )

            Text(
                text = product.priceLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Aumentar o disminuir cantidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Disminuir cantidad",
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

                Text(
                    text = "$quantity",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontSize = 18.sp
                )

                IconButton(
                    onClick = { if (quantity < 99) quantity++ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Aumentar cantidad",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Boton de añadir al carrito
            Button(
                onClick = {
                    cartViewModel.addToCart(product, quantity)
                    quantity = 1 // Resetear cantidad después de agregar
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    modifier = Modifier.size(18.dp) // Corregido: tamaño de icono realista
                )
                Spacer(modifier = Modifier.width(8.dp)) // Corregido: espacio mayor
                Text(
                    text = "Añadir",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: ProductCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Asignamos un icono a cada categoría
    val icon = when (category) {
        ProductCategory.frutas -> Icons.Filled.Inventory
        ProductCategory.verduras -> Icons.Default.Inventory
        ProductCategory.productosOrganicos -> Icons.Default.Inventory
        ProductCategory.lacteos -> Icons.Default.Inventory
        // Añade más casos si tienes más categorías
        else -> Icons.Default.Inventory
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f) // Esto hace que la tarjeta sea un cuadrado perfecto.
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        // Cambiamos el color según si está seleccionada.
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.displayName,
                modifier = Modifier.size(48.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.displayName,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}