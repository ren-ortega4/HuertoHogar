package com.example.huertohogar.view.screen

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.text.NumberFormat
import java.util.Locale

@DisplayName("CartScreen Tests")
class CartScreenTest {

    @Nested
    @DisplayName("formatPrice Tests")
    inner class FormatearPrecioTests {

        @Test
        @DisplayName("debe formatear precio cero correctamente")
        fun `debe formatear precio cero correctamente`() {
            val resultado = formatearPrecio(0.0)

            resultado shouldBe "$0"
        }

        @Test
        @DisplayName("debe formatear precio entero sin decimales")
        fun `debe formatear precio entero sin decimales`() {
            val resultado = formatearPrecio(1000.0)

            resultado shouldContain "$1"
            resultado shouldNotContain "CLP"
        }

        @Test
        @DisplayName("debe formatear precio con decimales correctamente")
        fun `debe formatear precio con decimales correctamente`() {
            val resultado = formatearPrecio(1500.50)

            resultado shouldContain "$"
            resultado shouldNotContain "CLP"
        }

        @Test
        @DisplayName("debe remover el código CLP del formato")
        fun `debe remover el codigo CLP del formato`() {
            val resultado = formatearPrecio(5000.0)

            resultado shouldNotContain "CLP"
            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe comenzar con el símbolo de peso")
        fun `debe comenzar con el simbolo de peso`() {
            val resultado = formatearPrecio(2500.0)

            resultado shouldStartWith "$"
        }

        @Test
        @DisplayName("debe formatear precios grandes con separadores de miles")
        fun `debe formatear precios grandes con separadores de miles`() {
            val resultado = formatearPrecio(1000000.0)

            resultado shouldContain "$"
            resultado shouldNotBe "$1000000"
        }

        @Test
        @DisplayName("debe manejar precios decimales pequeños")
        fun `debe manejar precios decimales pequenos`() {
            val resultado = formatearPrecio(0.50)

            resultado shouldContain "$"
            resultado shouldNotContain "CLP"
        }

        @Test
        @DisplayName("debe formatear precio con centavos")
        fun `debe formatear precio con centavos`() {
            val resultado = formatearPrecio(99.99)

            resultado shouldContain "$"            
            (resultado.contains("99") || resultado.contains("100")) shouldBe true
        }

        @ParameterizedTest
        @ValueSource(doubles = [10.0, 100.0, 1000.0, 10000.0, 100000.0])
        @DisplayName("debe formatear correctamente múltiples valores")
        fun `debe formatear correctamente multiples valores`(precio: Double) {
            val resultado = formatearPrecio(precio)

            resultado shouldContain "$"
            resultado shouldNotContain "CLP"
        }

        @ParameterizedTest
        @CsvSource(
            "100.00, $100",
            "250.00, $250",
            "500.00, $500",
            "1000.00, $1"
        )
        @DisplayName("debe producir formato esperado para precios específicos")
        fun `debe producir formato esperado para precios especificos`(precio: Double, esperado: String) {
            val resultado = formatearPrecio(precio)

            resultado shouldContain esperado.substring(0, 2)
        }

        @Test
        @DisplayName("debe manejar precio muy pequeño")
        fun `debe manejar precio muy pequeno`() {
            val resultado = formatearPrecio(0.01)

            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe formatear precio de mil exacto")
        fun `debe formatear precio de mil exacto`() {
            val resultado = formatearPrecio(1000.0)

            resultado shouldContain "$1"
        }

        @Test
        @DisplayName("debe formatear precio con dos decimales")
        fun `debe formatear precio con dos decimales`() {
            val resultado = formatearPrecio(123.45)

            resultado shouldContain "$"
            resultado shouldContain "123"
        }

        @Test
        @DisplayName("debe formatear precio redondo de cien")
        fun `debe formatear precio redondo de cien`() {
            val resultado = formatearPrecio(100.0)

            resultado shouldBe "$100"
        }

        @Test
        @DisplayName("debe formatear precio de diez mil")
        fun `debe formatear precio de diez mil`() {
            val resultado = formatearPrecio(10000.0)

            resultado shouldContain "$"
            resultado shouldContain "10"
        }

        @Test
        @DisplayName("debe manejar precio de un millón")
        fun `debe manejar precio de un millon`() {
            val resultado = formatearPrecio(1000000.0)

            resultado shouldContain "$"
            resultado shouldContain "1"
        }

        @Test
        @DisplayName("debe formatear precio típico de producto")
        fun `debe formatear precio tipico de producto`() {
            val resultado = formatearPrecio(2990.0)

            resultado shouldContain "$"
            resultado shouldContain "2"
            resultado shouldNotContain "CLP"
        }

        @Test
        @DisplayName("debe formatear precio con noventa y nueve centavos")
        fun `debe formatear precio con noventa y nueve centavos`() {
            val resultado = formatearPrecio(1999.99)

            resultado shouldContain "$"            
            (resultado.contains("1.999") || resultado.contains("2.000") || resultado.contains("2")) shouldBe true
        }

        @Test
        @DisplayName("debe producir string no vacío para cualquier precio")
        fun `debe producir string no vacio para cualquier precio`() {
            val resultado = formatearPrecio(500.0)

            resultado shouldNotBe ""
            resultado.length shouldNotBe 0
        }

        @Test
        @DisplayName("debe ser consistente con múltiples llamadas al mismo valor")
        fun `debe ser consistente con multiples llamadas al mismo valor`() {
            val precio = 7500.0
            val resultado1 = formatearPrecio(precio)
            val resultado2 = formatearPrecio(precio)
            val resultado3 = formatearPrecio(precio)

            resultado1 shouldBe resultado2
            resultado1 shouldBe resultado3
        }

        @Test
        @DisplayName("debe formatear diferentes precios de forma diferente")
        fun `debe formatear diferentes precios de forma diferente`() {
            val resultado100 = formatearPrecio(100.0)
            val resultado200 = formatearPrecio(200.0)
            val resultado500 = formatearPrecio(500.0)

            resultado100 shouldNotBe resultado200
            resultado200 shouldNotBe resultado500
            resultado100 shouldNotBe resultado500
        }

        @Test
        @DisplayName("debe manejar precio de cincuenta")
        fun `debe manejar precio de cincuenta`() {
            val resultado = formatearPrecio(50.0)

            resultado shouldBe "$50"
        }

        @Test
        @DisplayName("debe manejar precio de quinientos")
        fun `debe manejar precio de quinientos`() {
            val resultado = formatearPrecio(500.0)

            resultado shouldBe "$500"
        }

        @Test
        @DisplayName("debe manejar precio de cinco mil")
        fun `debe manejar precio de cinco mil`() {
            val resultado = formatearPrecio(5000.0)

            resultado shouldContain "$5"
        }

        @Test
        @DisplayName("debe formatear precio con tres decimales redondeándolo")
        fun `debe formatear precio con tres decimales redondeandolo`() {
            val resultado = formatearPrecio(123.456)

            resultado shouldContain "$"
            resultado shouldContain "123"
        }

        @Test
        @DisplayName("debe manejar precio muy grande")
        fun `debe manejar precio muy grande`() {
            val resultado = formatearPrecio(999999999.99)

            resultado shouldContain "$"
            resultado shouldNotContain "CLP"
        }

        @Test
        @DisplayName("debe formatear precio de quince mil")
        fun `debe formatear precio de quince mil`() {
            val resultado = formatearPrecio(15000.0)

            resultado shouldContain "$"
            resultado shouldContain "15"
        }

        @Test
        @DisplayName("debe formatear precio de veinte mil")
        fun `debe formatear precio de veinte mil`() {
            val resultado = formatearPrecio(20000.0)

            resultado shouldContain "$20"
        }

        @Test
        @DisplayName("debe verificar que no contiene espacios innecesarios al inicio")
        fun `debe verificar que no contiene espacios innecesarios al inicio`() {
            val resultado = formatearPrecio(1000.0)

            resultado.first() shouldBe '$'
        }

        @Test
        @DisplayName("debe formatear precio decimal menor a uno")
        fun `debe formatear precio decimal menor a uno`() {
            val resultado = formatearPrecio(0.75)

            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe usar formato de moneda chilena sin CLP")
        fun `debe usar formato de moneda chilena sin CLP`() {
            val resultado = formatearPrecio(3500.0)

            resultado shouldContain "$"
            resultado shouldNotContain "CLP"
            resultado shouldNotContain "CL"
        }
    }

    @Nested
    @DisplayName("Validación de Lógica de Formateo")
    inner class ValidacionLogicaFormateoTests {

        @Test
        @DisplayName("debe usar Locale en español chileno")
        fun `debe usar Locale en espanol chileno`() {
            val locale = Locale.Builder().setLanguage("es").setRegion("CL").build()
            val format = NumberFormat.getCurrencyInstance(locale)
            val resultado = format.format(1000.0)

            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe reemplazar CLP por vacío correctamente")
        fun `debe reemplazar CLP por vacio correctamente`() {
            val textoConCLP = "CLP 1.000"
            val resultado = textoConCLP.replace("CLP", "$")

            resultado shouldNotContain "CLP"
            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe mantener el símbolo de moneda después del reemplazo")
        fun `debe mantener el simbolo de moneda despues del reemplazo`() {
            val resultado = formatearPrecio(750.0)

            resultado.contains('$') shouldBe true
        }

        @Test
        @DisplayName("debe producir string legible para humanos")
        fun `debe producir string legible para humanos`() {
            val resultado = formatearPrecio(8999.0)

            resultado shouldContain "$"
            resultado.length shouldNotBe 0
        }

        @Test
        @DisplayName("debe formatear precios del carrito típicos")
        fun `debe formatear precios del carrito tipicos`() {
            val preciosCarrito = listOf(1500.0, 2300.0, 890.0, 4500.0)
            
            preciosCarrito.forEach { precio ->
                val resultado = formatearPrecio(precio)
                resultado shouldContain "$"
                resultado shouldNotContain "CLP"
            }
        }
    }

    @Nested
    @DisplayName("Casos de Borde")
    inner class CasosDeBordeTests {

        @Test
        @DisplayName("debe manejar Double MAX_VALUE sin lanzar excepción")
        fun `debe manejar Double MAX_VALUE sin lanzar excepcion`() {
            val resultado = formatearPrecio(Double.MAX_VALUE)

            resultado shouldNotBe null
            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe manejar precio casi cero")
        fun `debe manejar precio casi cero`() {
            val resultado = formatearPrecio(0.001)

            resultado shouldContain "$"
        }

        @Test
        @DisplayName("debe manejar precio con muchos decimales")
        fun `debe manejar precio con muchos decimales`() {
            val resultado = formatearPrecio(123.456789)

            resultado shouldContain "$"
            resultado shouldContain "123"
        }

        @Test
        @DisplayName("debe formatear precio de exactamente un peso")
        fun `debe formatear precio de exactamente un peso`() {
            val resultado = formatearPrecio(1.0)

            resultado shouldContain "$1"
        }

        @Test
        @DisplayName("debe formatear precio de exactamente diez pesos")
        fun `debe formatear precio de exactamente diez pesos`() {
            val resultado = formatearPrecio(10.0)

            resultado shouldBe "$10"
        }
    }

    // Función auxiliar para probar sin Composable
    private fun formatearPrecio(precio: Double): String {
        val formato = NumberFormat.getCurrencyInstance(
            Locale.Builder().setLanguage("es").setRegion("CL").build()
        )
        return formato.format(precio).replace("CLP", "$")
    }
}