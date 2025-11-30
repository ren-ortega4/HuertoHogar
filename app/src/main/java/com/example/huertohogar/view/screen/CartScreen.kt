package com.example.huertohogar.view.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogar.R
import com.example.huertohogar.model.CartItem
import com.example.huertohogar.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.huertohogar.model.CartRequest
import com.example.huertohogar.model.Item
import com.example.huertohogar.model.PreferenceResponse
import com.example.huertohogar.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel,
    showSuccessBanner: Boolean = false
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val isDark = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background

    var isLoading by remember { mutableStateOf(false) }
    var initPoint by remember { mutableStateOf("") }
    var bannerVisible by remember { mutableStateOf(showSuccessBanner) }

    LaunchedEffect(showSuccessBanner) {
        if (showSuccessBanner){
            bannerVisible = true
            viewModel.clearCart()
            // Oculta luego de 3 segundos
            kotlinx.coroutines.delay(3000)
            bannerVisible = false
        }
    }


    val context = LocalContext.current

    fun requestPreferenceAndPay() {
        isLoading = true
        val items = cartItems.map {
            Item(
                title = it.product.name,
                quantity = it.quantity,
                unitPrice = it.product.price
            )
        }
        val cartRequest = CartRequest(items = items)

        RetrofitInstance.mercadoPagoApi.createPreference(cartRequest)
            .enqueue(object : Callback<PreferenceResponse> {
                override fun onResponse(
                    call: Call<PreferenceResponse>,
                    response: Response<PreferenceResponse>
                ) {
                    isLoading = false
                    if (response.isSuccessful && response.body() != null) {
                        initPoint = response.body()!!.init_point
                        // ¡Lanza el CustomTab!
                        val intent = CustomTabsIntent.Builder().build()
                        intent.launchUrl(context, Uri.parse(initPoint))
                    } else {
                        Toast.makeText(context, "Error obteniendo link de pago", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PreferenceResponse>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

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
                .padding(16.dp)
        ) {
            if (bannerVisible) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4CAF50))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡Gracias por tu compra!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            Text(
                text = "Mi Carrito",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (cartItems.isEmpty()) {
                EmptyCartView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onIncreaseQuantity = {
                                viewModel.updateQuantity(cartItem.product.id, cartItem.quantity + 1)
                            },
                            onDecreaseQuantity = {
                                viewModel.updateQuantity(cartItem.product.id, cartItem.quantity - 1)
                            },
                            onRemove = {
                                viewModel.removeFromCart(cartItem.product.id)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }

                // Total y botón de compra
                TotalSection(
                    totalPrice = totalPrice,
                    onCheckout = {
                        requestPreferenceAndPay()
                    },
                    onClearCart = { viewModel.clearCart() }
                )
            }
        }
    }
}


@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Image(
                    painter = painterResource(cartItem.product.imagesRes),
                    contentDescription = cartItem.product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cartItem.product.priceLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Subtotal: ${formatPrice(cartItem.subtotal)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Controles de cantidad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = onDecreaseQuantity,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Disminuir",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "${cartItem.quantity}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = onIncreaseQuantity,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Aumentar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                // Botón eliminar
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        }
    }
}


@Composable
fun TotalSection(
    totalPrice: Double,
    onCheckout: () -> Unit,
    onClearCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatPrice(totalPrice),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8B57)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Proceder al Pago",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onClearCart,
                modifier = Modifier.fillMaxWidth().height(45.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red.copy(alpha = 0.7f))
            ) {
                Text(
                    text = "Vaciar Carrito",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
@Composable
fun EmptyCartView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito vacío",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agrega productos para comenzar tu compra",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            textAlign = TextAlign.Center
        )
    }
}

fun formatPrice(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(
        Locale.Builder().setLanguage("es").setRegion("CL").build()
    )
    return format.format(price).replace("CLP", "$")
}
