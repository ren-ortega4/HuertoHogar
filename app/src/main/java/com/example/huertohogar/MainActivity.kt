package com.example.huertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.huertohogar.model.AppDataBase
import com.example.app.view.FormScreen
import com.example.huertohogar.repository.UsuarioRepository
import com.example.huertohogar.ui.theme.HuertoHogarTheme
import com.example.huertohogar.view.components.InicioSesion
import com.example.huertohogar.view.screen.BottomNavigationBar
import com.example.huertohogar.view.screen.CartScreen
import com.example.huertohogar.view.screen.GreenAppBar
import com.example.huertohogar.view.screen.HomeContentScreen
import com.example.huertohogar.view.screen.NotificacionesScreen
import com.example.huertohogar.view.screen.ProductsByCategoryScreen
import com.example.huertohogar.view.screen.ProfileScreen
import com.example.huertohogar.view.screen.Screen
import com.example.huertohogar.viewmodel.CartViewModel
import com.example.huertohogar.viewmodel.NotificacionesViewModel
import com.example.huertohogar.viewmodel.ProfileViewModel
import com.example.huertohogar.viewmodel.UserViewModel
import com.example.huertohogar.viewmodel.UsuarioViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        setTheme(R.style.Theme_HuertoHogar_Launcher)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HuertoHogarTheme {
                val navController = rememberNavController()
                val notificacionesViewModel: NotificacionesViewModel = viewModel()
                val context = LocalContext.current
                val db = remember { AppDataBase.getDatabase(context) }
                val repository = remember { UsuarioRepository(db.usuarioDao()) }
                val factory = remember { UsuarioViewModelFactory(repository) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val profileViewModel: ProfileViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()

                val notifs by notificacionesViewModel.notificaciones.collectAsState(initial = emptyList())
                val notificacionesNoLeidas = notifs.count{ !it.leido}
                val userViewModel: UserViewModel = viewModel(factory=factory)

                val cartCount by cartViewModel.totalItems.collectAsState()

                Scaffold (
                    topBar = {
                        if (currentRoute != "FormularioRegistro" && currentRoute != Screen.Account.route && currentRoute != "InicioSesion") { // Oculta la barra en el formulario, cuenta e inicio de sesión
                            GreenAppBar(
                                notificacionesNoLeidas = notificacionesNoLeidas,
                                onNotificaionesClick = { navController.navigate("NotificacionesScreen") }
                            )
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController, cartCount = cartCount)
                    }
                ){ innerPadding ->
                    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
                        composable(Screen.Home.route) {
                            HomeContentScreen(
                                onNavigateToProducts = {
                                    navController.navigate(Screen.Product.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                    }
                                }
                            )
                        }
                        composable(Screen.Cart.route){
                            CartScreen(viewModel = cartViewModel)
                        }
                        composable(Screen.Account.route){
                            ProfileScreen(
                                viewModel = userViewModel,
                                navController = navController
                            )
                        }
                        composable("NotificacionesScreen") {
                            NotificacionesScreen(
                                viewModel = notificacionesViewModel,
                                onClose = { navController.popBackStack() }
                            )
                        }
                        composable("FormularioRegistro") {
                            FormScreen(
                                navController=navController,
                                viewModel = userViewModel
                            )
                        }
                        composable(Screen.Product.route) {
                            ProductsByCategoryScreen(
                                cartViewModel = cartViewModel,
                                onProductClick = { product ->
                                })
                        }
                        composable("InicioSesion") {
                            InicioSesion(navController=navController, viewModel = userViewModel)
                        }
                    }
                }
            }
        }
    }
}

