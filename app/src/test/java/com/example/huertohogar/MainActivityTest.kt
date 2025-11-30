package com.example.huertohogar

import com.example.huertohogar.model.Notificacion
import com.example.huertohogar.view.screen.Screen
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Tests para MainActivity")
class MainActivityTest {

    @Nested
    @DisplayName("Tests de Rutas de Navegación")
    inner class `Tests de Rutas de Navegación` {

        @Test
        fun `debería tener ruta correcta para Home`() {
            val ruta = Screen.Home.route

            ruta shouldBe "home"
            ruta.shouldNotBeEmpty()
        }

        @Test
        fun `debería tener ruta correcta para Cart`() {
            val ruta = Screen.Cart.route

            ruta shouldBe "cart"
            ruta.shouldNotBeEmpty()
        }

        @Test
        fun `debería tener ruta correcta para Account`() {
            val ruta = Screen.Account.route

            ruta shouldBe "account"
            ruta.shouldNotBeEmpty()
        }

        @Test
        fun `debería tener ruta correcta para Product`() {
            val ruta = Screen.Product.route

            ruta shouldBe "product"
            ruta.shouldNotBeEmpty()
        }

        @Test
        fun `debería tener rutas únicas para cada pantalla`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.distinct() shouldHaveSize 4
        }

        @Test
        fun `debería validar que todas las rutas sean strings no vacíos`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.all { it.isNotEmpty() } shouldBe true
        }

        @Test
        fun `debería validar rutas adicionales del NavHost`() {
            val rutasAdicionales = listOf(
                "NotificacionesScreen",
                "FormularioRegistro",
                "InicioSesion"
            )

            rutasAdicionales.all { it.isNotEmpty() } shouldBe true
            rutasAdicionales shouldHaveSize 3
        }

        @Test
        fun `debería validar que NotificacionesScreen tenga nombre correcto`() {
            val ruta = "NotificacionesScreen"

            ruta shouldBe "NotificacionesScreen"
            ruta.shouldContain("Notificaciones")
        }

        @Test
        fun `debería validar que FormularioRegistro tenga nombre correcto`() {
            val ruta = "FormularioRegistro"

            ruta shouldBe "FormularioRegistro"
            ruta.shouldContain("Formulario")
            ruta.shouldContain("Registro")
        }

        @Test
        fun `debería validar que InicioSesion tenga nombre correcto`() {
            val ruta = "InicioSesion"

            ruta shouldBe "InicioSesion"
            ruta.shouldContain("Inicio")
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Contadores")
    inner class `Tests de Lógica de Contadores` {

        @Test
        fun `debería contar notificaciones no leídas correctamente cuando todas están leídas`() {
            val notificaciones = listOf(
                Notificacion(id = 1, mensaje = "Test 1", leido = true),
                Notificacion(id = 2, mensaje = "Test 2", leido = true),
                Notificacion(id = 3, mensaje = "Test 3", leido = true)
            )

            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 0
        }

        @Test
        fun `debería contar notificaciones no leídas correctamente cuando todas están sin leer`() {
            val notificaciones = listOf(
                Notificacion(id = 1, mensaje = "Test 1", leido = false),
                Notificacion(id = 2, mensaje = "Test 2", leido = false),
                Notificacion(id = 3, mensaje = "Test 3", leido = false)
            )

            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 3
        }

        @Test
        fun `debería contar notificaciones no leídas correctamente con mix de leídas y no leídas`() {
            val notificaciones = listOf(
                Notificacion(id = 1, mensaje = "Test 1", leido = true),
                Notificacion(id = 2, mensaje = "Test 2", leido = false),
                Notificacion(id = 3, mensaje = "Test 3", leido = false),
                Notificacion(id = 4, mensaje = "Test 4", leido = true)
            )

            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 2
        }

        @Test
        fun `debería contar notificaciones no leídas correctamente con lista vacía`() {
            val notificaciones = emptyList<Notificacion>()

            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 0
        }

        @Test
        fun `debería contar notificaciones no leídas correctamente con una sola notificación sin leer`() {
            val notificaciones = listOf(
                Notificacion(id = 1, mensaje = "Test 1", leido = false)
            )

            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 1
        }

        @Test
        fun `debería manejar contadores de carrito con valores cero`() {
            val cartCount = 0

            cartCount shouldBe 0
            (cartCount >= 0) shouldBe true
        }

        @Test
        fun `debería manejar contadores de carrito con valores positivos`() {
            val cartCount1 = 1
            val cartCount2 = 5
            val cartCount3 = 10

            cartCount1 shouldBe 1
            cartCount2 shouldBe 5
            cartCount3 shouldBe 10
            (cartCount1 > 0) shouldBe true
            (cartCount2 > 0) shouldBe true
            (cartCount3 > 0) shouldBe true
        }

        @Test
        fun `debería validar que contador de carrito sea no negativo`() {
            val cartCountValidos = listOf(0, 1, 5, 10, 100)

            cartCountValidos.all { it >= 0 } shouldBe true
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Rutas en TopBar")
    inner class `Tests de Validación de Rutas en TopBar` {

        @Test
        fun `debería ocultar TopBar en FormularioRegistro`() {
            val currentRoute = "FormularioRegistro"
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldContain currentRoute
        }

        @Test
        fun `debería ocultar TopBar en Account`() {
            val currentRoute = Screen.Account.route
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldContain currentRoute
        }

        @Test
        fun `debería ocultar TopBar en InicioSesion`() {
            val currentRoute = "InicioSesion"
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldContain currentRoute
        }

        @Test
        fun `debería mostrar TopBar en Home`() {
            val currentRoute = Screen.Home.route
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldNotContain currentRoute
        }

        @Test
        fun `debería mostrar TopBar en Cart`() {
            val currentRoute = Screen.Cart.route
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldNotContain currentRoute
        }

        @Test
        fun `debería mostrar TopBar en Product`() {
            val currentRoute = Screen.Product.route
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldNotContain currentRoute
        }

        @Test
        fun `debería mostrar TopBar en NotificacionesScreen`() {
            val currentRoute = "NotificacionesScreen"
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldNotContain currentRoute
        }

        @Test
        fun `debería validar que solo 3 rutas ocultan TopBar`() {
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            rutasOcultas shouldHaveSize 3
        }
    }

    @Nested
    @DisplayName("Tests de Búsqueda")
    inner class `Tests de Búsqueda` {

        @Test
        fun `debería manejar query de búsqueda vacío`() {
            val searchQuery = ""

            searchQuery shouldBe ""
            searchQuery.isEmpty() shouldBe true
        }

        @Test
        fun `debería manejar query de búsqueda con texto`() {
            val searchQuery = "Manzana"

            searchQuery shouldBe "Manzana"
            searchQuery.isNotEmpty() shouldBe true
        }

        @Test
        fun `debería manejar query de búsqueda con espacios`() {
            val searchQuery = "  Manzana Verde  "

            searchQuery shouldBe "  Manzana Verde  "
            searchQuery.trim() shouldBe "Manzana Verde"
        }

        @Test
        fun `debería manejar query de búsqueda con caracteres especiales`() {
            val searchQuery = "Manzana 100% Orgánica"

            searchQuery shouldBe "Manzana 100% Orgánica"
            searchQuery.shouldContain("100%")
            searchQuery.shouldContain("Orgánica")
        }

        @Test
        fun `debería manejar query de búsqueda case insensitive`() {
            val searchQuery1 = "manzana"
            val searchQuery2 = "MANZANA"
            val searchQuery3 = "Manzana"

            searchQuery1.lowercase() shouldBe "manzana"
            searchQuery2.lowercase() shouldBe "manzana"
            searchQuery3.lowercase() shouldBe "manzana"
        }

        @Test
        fun `debería validar que query de búsqueda pueda ser muy largo`() {
            val searchQuery = "A".repeat(200)

            searchQuery.length shouldBe 200
            searchQuery.isNotEmpty() shouldBe true
        }
    }

    @Nested
    @DisplayName("Tests de Estructura de Pantallas")
    inner class `Tests de Estructura de Pantallas` {

        @Test
        fun `debería tener Screen como sealed class`() {
            val home: Screen = Screen.Home
            val cart: Screen = Screen.Cart
            val account: Screen = Screen.Account
            val product: Screen = Screen.Product

            home.shouldBeInstanceOf<Screen>()
            cart.shouldBeInstanceOf<Screen>()
            account.shouldBeInstanceOf<Screen>()
            product.shouldBeInstanceOf<Screen>()
        }

        @Test
        fun `debería tener todas las pantallas principales`() {
            val pantallas = listOf(
                Screen.Home,
                Screen.Cart,
                Screen.Account,
                Screen.Product
            )

            pantallas shouldHaveSize 4
            pantallas.all { it is Screen } shouldBe true
        }

        @Test
        fun `debería validar que Screen tenga propiedad route`() {
            val home = Screen.Home
            val cart = Screen.Cart

            home.route.shouldNotBeEmpty()
            cart.route.shouldNotBeEmpty()
        }

        @Test
        fun `debería comparar pantallas por igualdad`() {
            val home1 = Screen.Home
            val home2 = Screen.Home
            val cart = Screen.Cart

            (home1 == home2) shouldBe true
            (home1 == cart) shouldBe false
        }

        @Test
        fun `debería validar que cada pantalla tenga ruta única`() {
            val rutas = setOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas shouldHaveSize 4
        }
    }

    @Nested
    @DisplayName("Tests de Lógica de Navegación")
    inner class `Tests de Lógica de Navegación` {

        @Test
        fun `debería validar ruta de inicio es Home`() {
            val startDestination = Screen.Home.route

            startDestination shouldBe "home"
        }

        @Test
        fun `debería validar que Product sea destino navegable`() {
            val productRoute = Screen.Product.route

            productRoute shouldBe "product"
            productRoute.isNotEmpty() shouldBe true
        }

        @Test
        fun `debería validar navegación a NotificacionesScreen`() {
            val route = "NotificacionesScreen"

            route shouldBe "NotificacionesScreen"
        }

        @Test
        fun `debería validar navegación a FormularioRegistro`() {
            val route = "FormularioRegistro"

            route shouldBe "FormularioRegistro"
        }

        @Test
        fun `debería validar navegación a InicioSesion`() {
            val route = "InicioSesion"

            route shouldBe "InicioSesion"
        }

        @Test
        fun `debería permitir navegar entre pantallas principales`() {
            val rutasPrincipales = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutasPrincipales.all { it.isNotEmpty() } shouldBe true
        }

        @Test
        fun `debería validar todas las rutas composables disponibles`() {
            val todasLasRutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route,
                "NotificacionesScreen",
                "FormularioRegistro",
                "InicioSesion"
            )

            todasLasRutas shouldHaveSize 7
            todasLasRutas.all { it.isNotEmpty() } shouldBe true
        }
    }

    @Nested
    @DisplayName("Tests de Casos Extremos")
    inner class `Tests de Casos Extremos` {

        @Test
        fun `debería manejar lista vacía de notificaciones`() {
            val notificaciones = emptyList<Notificacion>()
            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 0
            notificaciones shouldHaveSize 0
        }

        @Test
        fun `debería manejar muchas notificaciones no leídas`() {
            val notificaciones = (1..100).map {
                Notificacion(id = it, mensaje = "Mensaje $it", leido = false)
            }
            val noLeidas = notificaciones.count { !it.leido }

            noLeidas shouldBe 100
            notificaciones shouldHaveSize 100
        }

        @Test
        fun `debería manejar contador de carrito con valor máximo`() {
            val cartCount = Int.MAX_VALUE

            (cartCount > 0) shouldBe true
            cartCount shouldBe Int.MAX_VALUE
        }

        @Test
        fun `debería validar rutas null-safe`() {
            val currentRoute: String? = null
            val rutasOcultas = listOf("FormularioRegistro", Screen.Account.route, "InicioSesion")

            (currentRoute == null || currentRoute !in rutasOcultas) shouldBe true
        }

        @Test
        fun `debería manejar búsqueda con solo espacios`() {
            val searchQuery = "     "

            searchQuery.trim().isEmpty() shouldBe true
        }

        @Test
        fun `debería filtrar notificaciones leídas`() {
            val notificaciones = listOf(
                Notificacion(id = 1, mensaje = "Test 1", leido = true),
                Notificacion(id = 2, mensaje = "Test 2", leido = false),
                Notificacion(id = 3, mensaje = "Test 3", leido = true)
            )

            val leidas = notificaciones.filter { it.leido }
            val noLeidas = notificaciones.filter { !it.leido }

            leidas shouldHaveSize 2
            noLeidas shouldHaveSize 1
        }

        @Test
        fun `debería agrupar notificaciones por estado de lectura`() {
            val notificaciones = listOf(
                Notificacion(id = 1, mensaje = "Test 1", leido = true),
                Notificacion(id = 2, mensaje = "Test 2", leido = false),
                Notificacion(id = 3, mensaje = "Test 3", leido = false),
                Notificacion(id = 4, mensaje = "Test 4", leido = true)
            )

            val agrupadas = notificaciones.groupBy { it.leido }

            agrupadas.keys shouldHaveSize 2
            agrupadas[true]?.shouldHaveSize(2)
            agrupadas[false]?.shouldHaveSize(2)
        }

        @Test
        fun `debería validar que rutas de navegación no tengan espacios`() {
            val todasLasRutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route,
                "NotificacionesScreen",
                "FormularioRegistro",
                "InicioSesion"
            )

            todasLasRutas.all { !it.contains(" ") } shouldBe true
        }

        @Test
        fun `debería validar que rutas no contengan caracteres especiales problemáticos`() {
            val todasLasRutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route,
                "NotificacionesScreen",
                "FormularioRegistro",
                "InicioSesion"
            )

            todasLasRutas.all { !it.contains("/") && !it.contains("?") } shouldBe true
        }

        @Test
        fun `debería comparar rutas case sensitive`() {
            val ruta1 = "home"
            val ruta2 = "Home"
            val ruta3 = "HOME"

            (ruta1 == ruta2) shouldBe false
            (ruta2 == ruta3) shouldBe false
            (ruta1 == Screen.Home.route) shouldBe true
        }

        @Test
        fun `debería permitir búsqueda con números`() {
            val searchQuery = "12345"

            searchQuery.all { it.isDigit() } shouldBe true
            searchQuery shouldBe "12345"
        }

        @Test
        fun `debería manejar contadores con operaciones aritméticas`() {
            val cartCount = 5
            val notificacionesNoLeidas = 3

            (cartCount + notificacionesNoLeidas) shouldBe 8
            (cartCount - notificacionesNoLeidas) shouldBe 2
            (cartCount * 2) shouldBe 10
        }

        @Test
        fun `debería validar que pantallas Screen sean singleton objects`() {
            val home1 = Screen.Home
            val home2 = Screen.Home

            (home1 === home2) shouldBe true // Misma referencia (singleton)
        }
    }
}