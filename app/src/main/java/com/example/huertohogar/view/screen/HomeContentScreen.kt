package com.example.huertohogar.view.screen

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.huertohogar.R
import com.example.huertohogar.viewmodel.NotificacionesViewModel

sealed class Screen(val route: String){
    object Home: Screen("home")
    object Cart: Screen("cart")
    object Account: Screen("account")
}


@Composable
fun HomeContentScreen(modifier: Modifier = Modifier) {
    MainContent(modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreenAppBar(
    notificacionesNoLeidas : Int,
    onNotificaionesClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    // Puedes ajustar el valor del top para margen superior (status bar)
    Surface(
        color = Color(0xFF2E8B57),
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                    onValueChange = { searchText = it },
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
                    ),
                    singleLine = true
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

    var startDestinationId by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(navBackStackEntry) {
        startDestinationId = runCatching {
            navController.graph.findStartDestination().id
        }.getOrNull()
    }

    // Footer
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
            .height(85.dp),
        containerColor = Color(0xFF2E8B57)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                if (currentRoute != Screen.Home.route){
                    navController.navigate(Screen.Home.route){
                        launchSingleTop = true
                        restoreState = true
                        startDestinationId?.let {
                            id -> popUpTo(id) {saveState = true}
                        }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2E8B57)
            )
        )
        NavigationBarItem(
            icon = {
                BadgedBox(badge = { Badge { Text("$cartCount") } }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
            },
            selected = currentRoute == Screen.Cart.route,
            onClick = {
                if (currentRoute != Screen.Cart.route){
                    navController.navigate(Screen.Cart.route){
                        launchSingleTop = true
                        restoreState = true
                        startDestinationId?.let { id ->
                            popUpTo(id) { saveState = true }
                        }
                    }
                }
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2E8B57)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Cuenta") },
            selected = currentRoute == Screen.Account.route,
            onClick = {
                if (currentRoute != Screen.Account.route){
                    navController.navigate(Screen.Account.route){
                        launchSingleTop = true
                        restoreState = true
                        startDestinationId?.let { id ->
                            popUpTo(id) { saveState = true }
                        }
                    }
                }
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2E8B57)
            )
        )
    }
}
