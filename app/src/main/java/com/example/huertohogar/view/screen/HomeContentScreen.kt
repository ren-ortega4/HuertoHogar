package com.example.huertohogar.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.huertohogar.R


sealed class Screen(val route: String){
    object Home: Screen("home")
    object Cart: Screen("cart")
    object Account: Screen("account")

    object Product: Screen("product")
}


@Composable
fun HomeContentScreen(
    modifier: Modifier = Modifier,
    onNavigateToProducts: () -> Unit
    ) {
    MainContent(
        modifier,
        onNavigateToProducts = onNavigateToProducts
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreenAppBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    notificacionesNoLeidas : Int,
    onNotificaionesClick: () -> Unit,
    onSearchTriggered: () -> Unit
) {

    // Puedes ajustar el valor del top para margen superior (status bar)
    Surface(
        color = Color.Transparent,
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2E8B57))
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.logotipo),
                    contentDescription = "Logo HuertoHogar",
                    modifier = Modifier
                        .size(40.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                //Buscador
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearchTriggered()
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color(0xFF388E3C)
                        )
                    },
                    placeholder = {
                        Text(
                            "Buscar productos...",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .shadow(1.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF388E3C),
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Notificaciones
                IconButton(onClick = onNotificaionesClick) {
                    BadgedBox(
                        badge = {
                            if (notificacionesNoLeidas > 0) Badge { Text("${notificacionesNoLeidas}") }
                        }
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavController, cartCount: Int) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Footer
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
            .height(85.dp),
        containerColor = Color(0xFF2E8B57)
    ) {
        // --- ITEM: INICIO (HOME) ---
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    // Saca todo del historial hasta la pantalla de inicio (home).
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Evita relanzar la misma pantalla si ya estás en ella.
                    launchSingleTop = true
                    // Restaura el estado al volver a una pantalla.
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF388E3C)
            )
        )

        // --- ITEM: PRODUCTOS (MENU) ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Inventory, contentDescription = "Productos") },
            selected = currentRoute == Screen.Product.route,
            onClick = {
                navController.navigate(Screen.Product.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF388E3C)
            )
        )

        // --- ITEM: CARRITO (SHOPPING_CART) ---
        NavigationBarItem(
            icon = {
                BadgedBox(badge = { if(cartCount > 0) Badge { Text("$cartCount") } }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
            },
            selected = currentRoute == Screen.Cart.route,
            onClick = {
                navController.navigate(Screen.Cart.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF388E3C)
            )
        )

        // --- ITEM: MAPA ---
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Map, contentDescription = "Mapa") },
            selected = currentRoute == "MapScreen",
            onClick = {
                navController.navigate("MapScreen") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF388E3C)
            )
        )

        // --- ITEM: CUENTA (PERSON) ---
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Cuenta") },
            selected = currentRoute == Screen.Account.route,
            onClick = {
                navController.navigate(Screen.Account.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF388E3C)
            )
        )
    }
}


