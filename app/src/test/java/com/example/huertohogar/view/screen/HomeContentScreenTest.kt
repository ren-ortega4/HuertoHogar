package com.example.huertohogar.view.screen

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("HomeContentScreen Tests")
class HomeContentScreenTest {

    @Nested
    @DisplayName("Screen Sealed Class Tests")
    inner class ClaseSelladaScreenTests {

        @Test
        @DisplayName("debe tener ruta correcta para Home")
        fun `debe tener ruta correcta para Home`() {
            val screen = Screen.Home

            screen.route shouldBe "home"
        }

        @Test
        @DisplayName("debe tener ruta correcta para Cart")
        fun `debe tener ruta correcta para Cart`() {
            val screen = Screen.Cart

            screen.route shouldBe "cart"
        }

        @Test
        @DisplayName("debe tener ruta correcta para Account")
        fun `debe tener ruta correcta para Account`() {
            val screen = Screen.Account

            screen.route shouldBe "account"
        }

        @Test
        @DisplayName("debe tener ruta correcta para Product")
        fun `debe tener ruta correcta para Product`() {
            val screen = Screen.Product

            screen.route shouldBe "product"
        }

        @Test
        @DisplayName("Home debe ser instancia de Screen")
        fun `Home debe ser instancia de Screen`() {
            val screen = Screen.Home

            screen.shouldBeInstanceOf<Screen>()
        }

        @Test
        @DisplayName("Cart debe ser instancia de Screen")
        fun `Cart debe ser instancia de Screen`() {
            val screen = Screen.Cart

            screen.shouldBeInstanceOf<Screen>()
        }

        @Test
        @DisplayName("Account debe ser instancia de Screen")
        fun `Account debe ser instancia de Screen`() {
            val screen = Screen.Account

            screen.shouldBeInstanceOf<Screen>()
        }

        @Test
        @DisplayName("Product debe ser instancia de Screen")
        fun `Product debe ser instancia de Screen`() {
            val screen = Screen.Product

            screen.shouldBeInstanceOf<Screen>()
        }

        @Test
        @DisplayName("debe verificar que todas las rutas son diferentes")
        fun `debe verificar que todas las rutas son diferentes`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.distinct().size shouldBe 4
        }

        @Test
        @DisplayName("debe verificar que ninguna ruta está vacía")
        fun `debe verificar que ninguna ruta esta vacia`() {
            val screens = listOf(
                Screen.Home,
                Screen.Cart,
                Screen.Account,
                Screen.Product
            )

            screens.forEach { screen ->
                screen.route shouldNotBe ""
                screen.route.isNotEmpty() shouldBe true
            }
        }

        @Test
        @DisplayName("debe verificar que Home.route no contiene espacios")
        fun `debe verificar que Home route no contiene espacios`() {
            Screen.Home.route.contains(" ") shouldBe false
        }

        @Test
        @DisplayName("debe verificar que Cart.route no contiene espacios")
        fun `debe verificar que Cart route no contiene espacios`() {
            Screen.Cart.route.contains(" ") shouldBe false
        }

        @Test
        @DisplayName("debe verificar que Account.route no contiene espacios")
        fun `debe verificar que Account route no contiene espacios`() {
            Screen.Account.route.contains(" ") shouldBe false
        }

        @Test
        @DisplayName("debe verificar que Product.route no contiene espacios")
        fun `debe verificar que Product route no contiene espacios`() {
            Screen.Product.route.contains(" ") shouldBe false
        }

        @Test
        @DisplayName("debe verificar que las rutas son en minúsculas")
        fun `debe verificar que las rutas son en minusculas`() {
            val screens = listOf(
                Screen.Home,
                Screen.Cart,
                Screen.Account,
                Screen.Product
            )

            screens.forEach { screen ->
                screen.route shouldBe screen.route.lowercase()
            }
        }

        @Test
        @DisplayName("debe poder acceder a Home múltiples veces")
        fun `debe poder acceder a Home multiples veces`() {
            val screen1 = Screen.Home
            val screen2 = Screen.Home
            val screen3 = Screen.Home

            screen1.route shouldBe screen2.route
            screen2.route shouldBe screen3.route
            screen1.route shouldBe "home"
        }

        @Test
        @DisplayName("debe poder acceder a Cart múltiples veces")
        fun `debe poder acceder a Cart multiples veces`() {
            val screen1 = Screen.Cart
            val screen2 = Screen.Cart

            screen1.route shouldBe screen2.route
            screen1.route shouldBe "cart"
        }

        @Test
        @DisplayName("debe poder acceder a Account múltiples veces")
        fun `debe poder acceder a Account multiples veces`() {
            val screen1 = Screen.Account
            val screen2 = Screen.Account

            screen1.route shouldBe screen2.route
            screen1.route shouldBe "account"
        }

        @Test
        @DisplayName("debe poder acceder a Product múltiples veces")
        fun `debe poder acceder a Product multiples veces`() {
            val screen1 = Screen.Product
            val screen2 = Screen.Product

            screen1.route shouldBe screen2.route
            screen1.route shouldBe "product"
        }

        @Test
        @DisplayName("debe verificar que Screen.Home no es null")
        fun `debe verificar que Screen Home no es null`() {
            Screen.Home shouldNotBe null
            Screen.Home.route shouldNotBe null
        }

        @Test
        @DisplayName("debe verificar que Screen.Cart no es null")
        fun `debe verificar que Screen Cart no es null`() {
            Screen.Cart shouldNotBe null
            Screen.Cart.route shouldNotBe null
        }

        @Test
        @DisplayName("debe verificar que Screen.Account no es null")
        fun `debe verificar que Screen Account no es null`() {
            Screen.Account shouldNotBe null
            Screen.Account.route shouldNotBe null
        }

        @Test
        @DisplayName("debe verificar que Screen.Product no es null")
        fun `debe verificar que Screen Product no es null`() {
            Screen.Product shouldNotBe null
            Screen.Product.route shouldNotBe null
        }
    }

    @Nested
    @DisplayName("Screen Route Validation Tests")
    inner class ValidacionRutasScreenTests {

        @Test
        @DisplayName("debe verificar que la ruta de Home es válida para navegación")
        fun `debe verificar que la ruta de Home es valida para navegacion`() {
            val route = Screen.Home.route

            route.length shouldNotBe 0
            route shouldBe "home"
            route.matches(Regex("[a-z]+")) shouldBe true
        }

        @Test
        @DisplayName("debe verificar que la ruta de Cart es válida para navegación")
        fun `debe verificar que la ruta de Cart es valida para navegacion`() {
            val route = Screen.Cart.route

            route.length shouldNotBe 0
            route shouldBe "cart"
            route.matches(Regex("[a-z]+")) shouldBe true
        }

        @Test
        @DisplayName("debe verificar que la ruta de Account es válida para navegación")
        fun `debe verificar que la ruta de Account es valida para navegacion`() {
            val route = Screen.Account.route

            route.length shouldNotBe 0
            route shouldBe "account"
            route.matches(Regex("[a-z]+")) shouldBe true
        }

        @Test
        @DisplayName("debe verificar que la ruta de Product es válida para navegación")
        fun `debe verificar que la ruta de Product es valida para navegacion`() {
            val route = Screen.Product.route

            route.length shouldNotBe 0
            route shouldBe "product"
            route.matches(Regex("[a-z]+")) shouldBe true
        }

        @Test
        @DisplayName("debe verificar que ninguna ruta contiene caracteres especiales")
        fun `debe verificar que ninguna ruta contiene caracteres especiales`() {
            val screens = listOf(
                Screen.Home,
                Screen.Cart,
                Screen.Account,
                Screen.Product
            )

            screens.forEach { screen ->
                screen.route.matches(Regex("[a-z]+")) shouldBe true
            }
        }

        @Test
        @DisplayName("debe verificar que todas las rutas tienen longitud mayor a cero")
        fun `debe verificar que todas las rutas tienen longitud mayor a cero`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.forEach { ruta ->
                ruta.length shouldNotBe 0
                ruta.isNotBlank() shouldBe true
            }
        }

        @Test
        @DisplayName("debe verificar que las rutas no contienen barras diagonales")
        fun `debe verificar que las rutas no contienen barras diagonales`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.forEach { ruta ->
                ruta.contains("/") shouldBe false
            }
        }

        @Test
        @DisplayName("debe verificar que las rutas no contienen parámetros")
        fun `debe verificar que las rutas no contienen parametros`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.forEach { ruta ->
                ruta.contains("{") shouldBe false
                ruta.contains("}") shouldBe false
                ruta.contains("?") shouldBe false
            }
        }

        @Test
        @DisplayName("debe verificar que la colección de rutas tiene 4 elementos únicos")
        fun `debe verificar que la coleccion de rutas tiene 4 elementos unicos`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas shouldHaveSize 4
            rutas.toSet() shouldHaveSize 4
        }
    }

    @Nested
    @DisplayName("Screen Navigation Tests")
    inner class NavegacionScreenTests {

        @Test
        @DisplayName("debe poder crear una lista de todas las pantallas")
        fun `debe poder crear una lista de todas las pantallas`() {
            val screens = listOf(
                Screen.Home,
                Screen.Cart,
                Screen.Account,
                Screen.Product
            )

            screens shouldHaveSize 4
            screens shouldContain Screen.Home
            screens shouldContain Screen.Cart
            screens shouldContain Screen.Account
            screens shouldContain Screen.Product
        }

        @Test
        @DisplayName("debe poder mapear rutas a nombres de pantalla")
        fun `debe poder mapear rutas a nombres de pantalla`() {
            val rutasPorNombre = mapOf(
                "Home" to Screen.Home.route,
                "Cart" to Screen.Cart.route,
                "Account" to Screen.Account.route,
                "Product" to Screen.Product.route
            )

            rutasPorNombre["Home"] shouldBe "home"
            rutasPorNombre["Cart"] shouldBe "cart"
            rutasPorNombre["Account"] shouldBe "account"
            rutasPorNombre["Product"] shouldBe "product"
        }

        @Test
        @DisplayName("debe verificar que Screen.Home es diferente de Screen.Cart")
        fun `debe verificar que Screen Home es diferente de Screen Cart`() {
            Screen.Home.route shouldNotBe Screen.Cart.route
        }

        @Test
        @DisplayName("debe verificar que Screen.Account es diferente de Screen.Product")
        fun `debe verificar que Screen Account es diferente de Screen Product`() {
            Screen.Account.route shouldNotBe Screen.Product.route
        }

        @Test
        @DisplayName("debe verificar que Screen.Home es diferente de Screen.Product")
        fun `debe verificar que Screen Home es diferente de Screen Product`() {
            Screen.Home.route shouldNotBe Screen.Product.route
        }

        @Test
        @DisplayName("debe verificar que Screen.Cart es diferente de Screen.Account")
        fun `debe verificar que Screen Cart es diferente de Screen Account`() {
            Screen.Cart.route shouldNotBe Screen.Account.route
        }

        @Test
        @DisplayName("debe poder crear un mapa de rutas a screens")
        fun `debe poder crear un mapa de rutas a screens`() {
            val screensPorRuta = mapOf(
                Screen.Home.route to Screen.Home,
                Screen.Cart.route to Screen.Cart,
                Screen.Account.route to Screen.Account,
                Screen.Product.route to Screen.Product
            )

            screensPorRuta["home"] shouldBe Screen.Home
            screensPorRuta["cart"] shouldBe Screen.Cart
            screensPorRuta["account"] shouldBe Screen.Account
            screensPorRuta["product"] shouldBe Screen.Product
        }

        @Test
        @DisplayName("debe verificar que todas las pantallas tienen rutas únicas")
        fun `debe verificar que todas las pantallas tienen rutas unicas`() {
            val rutasSet = setOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutasSet shouldHaveSize 4
        }
    }

    @Nested
    @DisplayName("Screen Properties Tests")
    inner class PropiedadesScreenTests {

        @Test
        @DisplayName("debe verificar que Home tiene exactamente 4 caracteres")
        fun `debe verificar que Home tiene exactamente 4 caracteres`() {
            Screen.Home.route.length shouldBe 4
        }

        @Test
        @DisplayName("debe verificar que Cart tiene exactamente 4 caracteres")
        fun `debe verificar que Cart tiene exactamente 4 caracteres`() {
            Screen.Cart.route.length shouldBe 4
        }

        @Test
        @DisplayName("debe verificar que Account tiene 7 caracteres")
        fun `debe verificar que Account tiene 7 caracteres`() {
            Screen.Account.route.length shouldBe 7
        }

        @Test
        @DisplayName("debe verificar que Product tiene 7 caracteres")
        fun `debe verificar que Product tiene 7 caracteres`() {
            Screen.Product.route.length shouldBe 7
        }

        @Test
        @DisplayName("debe verificar que la ruta más corta tiene 4 caracteres")
        fun `debe verificar que la ruta mas corta tiene 4 caracteres`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            val rutaMasCorta = rutas.minByOrNull { it.length }
            rutaMasCorta?.length shouldBe 4
        }

        @Test
        @DisplayName("debe verificar que la ruta más larga tiene 7 caracteres")
        fun `debe verificar que la ruta mas larga tiene 7 caracteres`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            val rutaMasLarga = rutas.maxByOrNull { it.length }
            rutaMasLarga?.length shouldBe 7
        }

        @Test
        @DisplayName("debe verificar que todas las rutas empiezan con letra minúscula")
        fun `debe verificar que todas las rutas empiezan con letra minuscula`() {
            val rutas = listOf(
                Screen.Home.route,
                Screen.Cart.route,
                Screen.Account.route,
                Screen.Product.route
            )

            rutas.forEach { ruta ->
                ruta.first().isLowerCase() shouldBe true
            }
        }

        @Test
        @DisplayName("debe verificar el tipo de dato de route es String")
        fun `debe verificar el tipo de dato de route es String`() {
            Screen.Home.route.shouldBeInstanceOf<String>()
            Screen.Cart.route.shouldBeInstanceOf<String>()
            Screen.Account.route.shouldBeInstanceOf<String>()
            Screen.Product.route.shouldBeInstanceOf<String>()
        }
    }
}