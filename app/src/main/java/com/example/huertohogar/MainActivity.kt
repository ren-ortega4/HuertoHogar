package com.example.huertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app.view.FormScreen
import com.example.huertohogar.model.AppDataBase
import com.example.huertohogar.repository.UsuarioRepository
import com.example.huertohogar.ui.theme.HuertoHogarTheme
import com.example.huertohogar.view.components.InicioSesion
import com.example.huertohogar.view.screen.BottomNavigationBar
import com.example.huertohogar.view.screen.GreenAppBar
import com.example.huertohogar.view.screen.HomeContentScreen
import com.example.huertohogar.view.screen.NotificacionesScreen
import com.example.huertohogar.view.screen.ProfileScreen
import com.example.huertohogar.view.screen.Screen
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
                val context = LocalContext.current
                val db = remember { AppDataBase.getDatabase(context) }
                val repository = remember { UsuarioRepository(db.usuarioDao()) }
                val factory = remember { UsuarioViewModelFactory(repository) }


                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val notificacionesViewModel: NotificacionesViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel(factory=factory)

                val notifs by notificacionesViewModel.notificaciones.collectAsState(initial = emptyList())
                val notificacionesNoLeidas = notifs.count { !it.leido }

                var cartCount = 0

                Scaffold(
                    topBar = {
                        if (currentRoute != "FormularioRegistro" && currentRoute != Screen.Account.route && currentRoute != "InicioSesion") { // Oculta la barra en el formulario, cuenta e inicio de sesión
                            GreenAppBar(
                                notificacionesNoLeidas = notificacionesNoLeidas,
                                onNotificaionesClick = { navController.navigate("NotificacionesScreen") }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentRoute != "FormularioRegistro" && currentRoute != "InicioSesion") { // Oculta la barra inferior solo en el formulario y en inicio de sesion
                            BottomNavigationBar(navController = navController, cartCount = cartCount)
                        }
                    }
                ) { innerPadding ->
                    val navHostModifier = if (currentRoute != Screen.Account.route && currentRoute != "InicioSesion" && currentRoute != "FormularioRegistro") {
                        Modifier.padding(innerPadding)
                    } else {
                        Modifier
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = navHostModifier
                    ) {
                        composable(Screen.Home.route) {
                            HomeContentScreen()
                        }
                        composable(Screen.Cart.route) {
                            Text("Cart Screen (placeholder)")
                        }
                        composable(Screen.Account.route) {
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
                        composable("InicioSesion") {
                            InicioSesion(navController=navController, viewModel = userViewModel)
                        }
                    }
                }
            }
        }
    }
}