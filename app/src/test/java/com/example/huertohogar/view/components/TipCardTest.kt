package com.example.huertohogar.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("TipCard Tests")
class TipCardTest {

    @Nested
    @DisplayName("getIconForName Tests")
    inner class ObtenerIconoPorNombreTests {

        @Test
        @DisplayName("debe retornar el icono LocalOffer cuando el nombre es LocalOffer")
        fun `debe retornar el icono LocalOffer cuando el nombre es LocalOffer`() {
            val resultado = obtenerIconoPorNombre("LocalOffer")

            resultado shouldBe Icons.Filled.LocalOffer
            resultado shouldNotBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe retornar el icono ThumbUp cuando el nombre es ThumbUp")
        fun `debe retornar el icono ThumbUp cuando el nombre es ThumbUp`() {
            val resultado = obtenerIconoPorNombre("ThumbUp")

            resultado shouldBe Icons.Filled.ThumbUp
        }

        @Test
        @DisplayName("debe retornar el icono Storefront cuando el nombre es Storefront")
        fun `debe retornar el icono Storefront cuando el nombre es Storefront`() {
            val resultado = obtenerIconoPorNombre("Storefront")

            resultado shouldBe Icons.Filled.Storefront
        }

        @Test
        @DisplayName("debe retornar el icono Call cuando el nombre es Call")
        fun `debe retornar el icono Call cuando el nombre es Call`() {
            val resultado = obtenerIconoPorNombre("Call")

            resultado shouldBe Icons.Filled.Call
        }

        @Test
        @DisplayName("debe retornar el icono Error cuando el nombre es Error")
        fun `debe retornar el icono Error cuando el nombre es Error`() {
            val resultado = obtenerIconoPorNombre("Error")

            resultado shouldBe Icons.Filled.Error
        }

        @Test
        @DisplayName("debe retornar el icono Info por defecto cuando el nombre no coincide")
        fun `debe retornar el icono Info por defecto cuando el nombre no coincide`() {
            val resultado = obtenerIconoPorNombre("NombreDesconocido")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe retornar el icono Info cuando el nombre está vacío")
        fun `debe retornar el icono Info cuando el nombre esta vacio`() {
            val resultado = obtenerIconoPorNombre("")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe retornar ImageVector para cualquier entrada")
        fun `debe retornar ImageVector para cualquier entrada`() {
            val resultado = obtenerIconoPorNombre("LocalOffer")

            resultado.shouldBeInstanceOf<ImageVector>()
        }

        @ParameterizedTest
        @ValueSource(strings = ["LocalOffer", "ThumbUp", "Storefront", "Call", "Error"])
        @DisplayName("debe retornar un icono válido para todos los nombres conocidos")
        fun `debe retornar un icono valido para todos los nombres conocidos`(nombreIcono: String) {
            val resultado = obtenerIconoPorNombre(nombreIcono)

            resultado shouldNotBe null
            resultado.shouldBeInstanceOf<ImageVector>()
        }

        @Test
        @DisplayName("debe distinguir entre diferentes iconos")
        fun `debe distinguir entre diferentes iconos`() {
            val iconoLocalOffer = obtenerIconoPorNombre("LocalOffer")
            val iconoThumbUp = obtenerIconoPorNombre("ThumbUp")
            val iconoStorefront = obtenerIconoPorNombre("Storefront")

            iconoLocalOffer shouldNotBe iconoThumbUp
            iconoLocalOffer shouldNotBe iconoStorefront
            iconoThumbUp shouldNotBe iconoStorefront
        }

        @Test
        @DisplayName("debe ser sensible a mayúsculas y minúsculas")
        fun `debe ser sensible a mayusculas y minusculas`() {
            val resultadoMayuscula = obtenerIconoPorNombre("LocalOffer")
            val resultadoMinuscula = obtenerIconoPorNombre("localoffer")

            resultadoMayuscula shouldBe Icons.Filled.LocalOffer
            resultadoMinuscula shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe manejar nombres con espacios retornando icono por defecto")
        fun `debe manejar nombres con espacios retornando icono por defecto`() {
            val resultado = obtenerIconoPorNombre("Local Offer")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe manejar caracteres especiales retornando icono por defecto")
        fun `debe manejar caracteres especiales retornando icono por defecto`() {
            val resultado = obtenerIconoPorNombre("Local@Offer#123")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe manejar nombres muy largos retornando icono por defecto")
        fun `debe manejar nombres muy largos retornando icono por defecto`() {
            val nombreLargo = "LocalOffer".repeat(100)
            val resultado = obtenerIconoPorNombre(nombreLargo)

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe retornar consistentemente el mismo icono para la misma entrada")
        fun `debe retornar consistentemente el mismo icono para la misma entrada`() {
            val resultado1 = obtenerIconoPorNombre("ThumbUp")
            val resultado2 = obtenerIconoPorNombre("ThumbUp")
            val resultado3 = obtenerIconoPorNombre("ThumbUp")

            resultado1 shouldBe resultado2
            resultado1 shouldBe resultado3
            resultado1 shouldBe Icons.Filled.ThumbUp
        }

        @Test
        @DisplayName("debe manejar números retornando icono por defecto")
        fun `debe manejar numeros retornando icono por defecto`() {
            val resultado = obtenerIconoPorNombre("12345")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe retornar Info para null convertido a string")
        fun `debe retornar Info para entradas no esperadas`() {
            val resultado = obtenerIconoPorNombre("null")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe manejar espacios en blanco retornando icono por defecto")
        fun `debe manejar espacios en blanco retornando icono por defecto`() {
            val resultado = obtenerIconoPorNombre("   ")

            resultado shouldBe Icons.Filled.Info
        }

        @Test
        @DisplayName("debe verificar que todos los casos conocidos no retornan el icono por defecto")
        fun `debe verificar que todos los casos conocidos no retornan el icono por defecto`() {
            val nombresConocidos = listOf("LocalOffer", "ThumbUp", "Storefront", "Call", "Error")

            nombresConocidos.forEach { nombre ->
                val resultado = obtenerIconoPorNombre(nombre)
                resultado shouldNotBe Icons.Filled.Info
            }
        }
    }

    // Función auxiliar para probar sin Composable
    private fun obtenerIconoPorNombre(nombreIcono: String): ImageVector {
        return when(nombreIcono){
            "LocalOffer" -> Icons.Filled.LocalOffer
            "ThumbUp" -> Icons.Filled.ThumbUp
            "Storefront" -> Icons.Filled.Storefront
            "Call" -> Icons.Filled.Call
            "Error" -> Icons.Filled.Error
            else -> Icons.Filled.Info
        }
    }
}