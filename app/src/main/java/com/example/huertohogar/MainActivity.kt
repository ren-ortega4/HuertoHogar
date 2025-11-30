package com.example.huertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.huertohogar.data.local.AppDatabase
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.ui.theme.HuertoHogarTheme
import com.example.huertohogar.view.components.FormScreen
import com.example.huertohogar.view.components.InicioSesion
import com.example.huertohogar.view.screen.*
import com.example.huertohogar.viewmodel.*
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    private val db by lazy { AppDatabase.getDatabase(this) }
    // 1. Crear la instancia del Repositorio de Usuario
    private val usuarioRepository by lazy { UsuarioRepository(db.usuarioDao()) }
    // 2. Pasar el Repositorio a la Fábrica, no el Dao directamente
    private val userViewModelFactory by lazy { UserViewModelFactory(usuarioRepository) }
    private val storeViewModelFactory by lazy { StoreViewModelFactory(db.tiendaDao()) }
    companion object {
        const val MP_REQUEST_CODE = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MP_REQUEST_CODE) {
            Log.d("MP_LOG", "resultCode: $resultCode")
            if (data?.extras != null) {
                for (key in data.extras!!.keySet()) {
                    Log.d("MP_LOG", "EXTRA [$key] => ${data.extras!!.get(key)}")
                }
            } else {
                Log.d("MP_LOG", "No data extras returned")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        Log.d("DEEP", "Intent: ${intent?.toString()} data: ${intent?.data}")
        val isDeepLinkToCongrats = intent?.data?.host == "congrats"
        Log.d("DEEP", "isDeepLinkToCongrats: $isDeepLinkToCongrats")

        setContent {
            HuertoHogarTheme { // El tema ahora se resolverá correctamente
                var showLottieSplash by remember { mutableStateOf(true) }
                var shouldAutoNavigateCart by remember { mutableStateOf(isDeepLinkToCongrats) }

                if (showLottieSplash) {
                    LottieSplashScreen(
                        onAnimationFinished = { showLottieSplash = false }
                    )
                } else {

                    // Main App Content
                    val navController = rememberNavController()
                    val alreadyNavigated = remember { mutableStateOf(false) }
                    LaunchedEffect(showLottieSplash, shouldAutoNavigateCart) {
                        if (!showLottieSplash && shouldAutoNavigateCart && !alreadyNavigated.value) {
                            navController.navigate("cart?success=true") {
                                popUpTo(Screen.Home.route)
                            }
                            alreadyNavigated.value = true
                        }
                    }
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val cartViewModel: CartViewModel = viewModel()
                    val notificacionesViewModel: NotificacionesViewModel = viewModel()
                    val productViewModel: ProductViewModel = viewModel()
                    val userViewModel: UserViewModel = viewModel(factory = userViewModelFactory)
                    val storeViewModel: StoreViewModel = viewModel(factory = storeViewModelFactory)

                    val notifs by notificacionesViewModel.notificaciones.collectAsState(initial = emptyList())
                    val notificacionesNoLeidas = notifs.count { !it.leido }
                    val cartCount by cartViewModel.totalItems.collectAsState()
                    val searchQuery by productViewModel.searchQuery.collectAsState()

                    val showTopBar = currentRoute !in listOf("FormularioRegistro", "InicioSesion", "MapScreen", Screen.Account.route)
                    val showBottomBar = currentRoute !in listOf("FormularioRegistro", "InicioSesion")



                    Scaffold(
                        topBar = {
                            if (showTopBar) {
                                GreenAppBar(
                                    searchText = searchQuery,
                                    onSearchTextChange = { newQuery -> productViewModel.onSearchQueryChange(newQuery) },
                                    notificacionesNoLeidas = notificacionesNoLeidas,
                                    onNotificaionesClick = { navController.navigate("NotificacionesScreen") },
                                    onSearchTriggered = {
                                        navController.navigate(Screen.Product.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(Screen.Home.route) { saveState = true }
                                        }
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavigationBar(navController = navController, cartCount = cartCount)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(innerPadding) // Aplica el padding aquí, al contenedor de navegación
                        ) {
                            composable(Screen.Home.route) {
                                HomeContentScreen(onNavigateToProducts = { navController.navigate(Screen.Product.route) })
                            }
                            composable(
                                route = "cart?success={success}",
                                arguments = listOf(
                                    navArgument("success"){
                                        type = NavType.BoolType
                                        defaultValue = false
                                    }
                                )
                            ) { backStackEntry ->
                                val showSuccessBanner = backStackEntry.arguments?.getBoolean("success") ?: false
                                Log.d("DEEP", "CartScreen: showSuccessBanner=$showSuccessBanner")
                                CartScreen(viewModel = cartViewModel, showSuccessBanner = showSuccessBanner)
                            }
                            composable(Screen.Account.route) {
                                ProfileScreen(viewModel = userViewModel, navController = navController)
                            }
                            composable("NotificacionesScreen") {
                                NotificacionesScreen(viewModel = notificacionesViewModel, onClose = { navController.popBackStack() })
                            }
                            composable(Screen.Product.route) {
                                ProductsByCategoryScreen(productViewModel = productViewModel, cartViewModel = cartViewModel, onProductClick = {})
                            }
                            composable("MapScreen") {
                                MapScreen(storeViewModel = storeViewModel)
                            }
                            composable("FormularioRegistro") { 
                                FormScreen(navController = navController, viewModel = userViewModel) 
                            }
                            composable("InicioSesion") { 
                                InicioSesion(navController = navController, viewModel = userViewModel) 
                            }
                        }
                    }
                }
            }
        }
    }
}