package com.example.huertohogar.view.screen

import com.example.huertohogar.model.Notificacion
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("NotificacionesScreen Tests")
class NotificacionesScreenTest {

    @Nested
    @DisplayName("Notificacion Data Class Tests")
    inner class ClaseNotificacionDatosTests {

        @Test
        @DisplayName("debe crear notificación con todos los parámetros")
        fun `debe crear notificacion con todos los parametros`() {
            val notificacion = Notificacion(
                id = 1,
                mensaje = "Bienvenido a HuertoHogar",
                leido = false
            )

            notificacion.id shouldBe 1
            notificacion.mensaje shouldBe "Bienvenido a HuertoHogar"
            notificacion.leido shouldBe false
        }

        @Test
        @DisplayName("debe crear notificación con valor por defecto de leido como false")
        fun `debe crear notificacion con valor por defecto de leido como false`() {
            val notificacion = Notificacion(
                id = 1,
                mensaje = "Mensaje de prueba"
            )

            notificacion.leido shouldBe false
        }

        @Test
        @DisplayName("debe verificar que id es un Int")
        fun `debe verificar que id es un Int`() {
            val notificacion = Notificacion(1, "Mensaje", false)

            notificacion.id.shouldBeInstanceOf<Int>()
        }

        @Test
        @DisplayName("debe verificar que mensaje es un String")
        fun `debe verificar que mensaje es un String`() {
            val notificacion = Notificacion(1, "Mensaje", false)

            notificacion.mensaje.shouldBeInstanceOf<String>()
        }

        @Test
        @DisplayName("debe verificar que leido es un Boolean")
        fun `debe verificar que leido es un Boolean`() {
            val notificacion = Notificacion(1, "Mensaje", false)

            notificacion.leido.shouldBeInstanceOf<Boolean>()
        }

        @Test
        @DisplayName("debe crear dos notificaciones con los mismos valores como iguales")
        fun `debe crear dos notificaciones con los mismos valores como iguales`() {
            val notif1 = Notificacion(1, "Mensaje", false)
            val notif2 = Notificacion(1, "Mensaje", false)

            notif1 shouldBe notif2
        }

        @Test
        @DisplayName("debe crear dos notificaciones con valores diferentes como no iguales")
        fun `debe crear dos notificaciones con valores diferentes como no iguales`() {
            val notif1 = Notificacion(1, "Mensaje 1", false)
            val notif2 = Notificacion(2, "Mensaje 2", false)

            notif1 shouldNotBe notif2
        }

        @Test
        @DisplayName("debe permitir copiar notificación con método copy")
        fun `debe permitir copiar notificacion con metodo copy`() {
            val original = Notificacion(1, "Mensaje", false)
            val copia = original.copy()

            copia shouldBe original
        }

        @Test
        @DisplayName("debe permitir copiar notificación modificando solo leido")
        fun `debe permitir copiar notificacion modificando solo leido`() {
            val original = Notificacion(1, "Mensaje", false)
            val modificada = original.copy(leido = true)

            modificada.id shouldBe original.id
            modificada.mensaje shouldBe original.mensaje
            modificada.leido shouldBe true
        }

        @Test
        @DisplayName("debe permitir marcar notificación como leída")
        fun `debe permitir marcar notificacion como leida`() {
            val noLeida = Notificacion(1, "Mensaje", false)
            val leida = noLeida.copy(leido = true)

            noLeida.leido shouldBe false
            leida.leido shouldBe true
        }

        @Test
        @DisplayName("debe permitir copiar notificación modificando solo el mensaje")
        fun `debe permitir copiar notificacion modificando solo el mensaje`() {
            val original = Notificacion(1, "Mensaje original", false)
            val modificada = original.copy(mensaje = "Mensaje nuevo")

            modificada.id shouldBe original.id
            modificada.mensaje shouldBe "Mensaje nuevo"
            modificada.leido shouldBe original.leido
        }

        @Test
        @DisplayName("debe permitir copiar notificación modificando solo el id")
        fun `debe permitir copiar notificacion modificando solo el id`() {
            val original = Notificacion(1, "Mensaje", false)
            val modificada = original.copy(id = 2)

            modificada.id shouldBe 2
            modificada.mensaje shouldBe original.mensaje
            modificada.leido shouldBe original.leido
        }

        @Test
        @DisplayName("debe manejar mensajes vacíos")
        fun `debe manejar mensajes vacios`() {
            val notificacion = Notificacion(1, "", false)

            notificacion.mensaje shouldBe ""
            notificacion.mensaje.isEmpty() shouldBe true
        }

        @Test
        @DisplayName("debe manejar mensajes largos")
        fun `debe manejar mensajes largos`() {
            val mensajeLargo = "Este es un mensaje muy largo de notificación ".repeat(10)
            val notificacion = Notificacion(1, mensajeLargo, false)

            notificacion.mensaje shouldBe mensajeLargo
            notificacion.mensaje.length shouldNotBe 0
        }

        @Test
        @DisplayName("debe manejar mensajes con caracteres especiales")
        fun `debe manejar mensajes con caracteres especiales`() {
            val notificacion = Notificacion(1, "¡Oferta especial! 50% de descuento 💰", false)

            notificacion.mensaje shouldContain "¡Oferta especial!"
            notificacion.mensaje shouldContain "%"
        }

        @Test
        @DisplayName("debe manejar IDs negativos")
        fun `debe manejar IDs negativos`() {
            val notificacion = Notificacion(-1, "Mensaje", false)

            notificacion.id shouldBe -1
        }

        @Test
        @DisplayName("debe manejar IDs grandes")
        fun `debe manejar IDs grandes`() {
            val notificacion = Notificacion(Int.MAX_VALUE, "Mensaje", false)

            notificacion.id shouldBe Int.MAX_VALUE
        }

        @Test
        @DisplayName("debe crear notificación leída")
        fun `debe crear notificacion leida`() {
            val notificacion = Notificacion(1, "Mensaje", leido = true)

            notificacion.leido shouldBe true
        }

        @Test
        @DisplayName("debe crear notificación no leída por defecto")
        fun `debe crear notificacion no leida por defecto`() {
            val notificacion = Notificacion(1, "Mensaje")

            notificacion.leido shouldBe false
        }

        @Test
        @DisplayName("debe poder crear lista de notificaciones")
        fun `debe poder crear lista de notificaciones`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", false)
            )

            notificaciones shouldHaveSize 3
            notificaciones.first().id shouldBe 1
            notificaciones.last().id shouldBe 3
        }

        @Test
        @DisplayName("debe verificar que dos notificaciones con mismo id pero diferente mensaje no son iguales")
        fun `debe verificar que dos notificaciones con mismo id pero diferente mensaje no son iguales`() {
            val notif1 = Notificacion(1, "Mensaje 1", false)
            val notif2 = Notificacion(1, "Mensaje 2", false)

            notif1 shouldNotBe notif2
        }

        @Test
        @DisplayName("debe verificar que dos notificaciones con mismo mensaje pero diferente estado leído no son iguales")
        fun `debe verificar que dos notificaciones con mismo mensaje pero diferente estado leido no son iguales`() {
            val notif1 = Notificacion(1, "Mensaje", false)
            val notif2 = Notificacion(1, "Mensaje", true)

            notif1 shouldNotBe notif2
        }

        @Test
        @DisplayName("debe generar hashCode consistente para mismos valores")
        fun `debe generar hashCode consistente para mismos valores`() {
            val notif1 = Notificacion(1, "Mensaje", false)
            val notif2 = Notificacion(1, "Mensaje", false)

            notif1.hashCode() shouldBe notif2.hashCode()
        }

        @Test
        @DisplayName("debe generar toString con todos los campos")
        fun `debe generar toString con todos los campos`() {
            val notificacion = Notificacion(1, "Mensaje de prueba", false)
            val resultado = notificacion.toString()

            resultado shouldContain "1"
            resultado shouldContain "Mensaje de prueba"
            resultado shouldContain "false"
        }

        @Test
        @DisplayName("debe manejar mensajes con saltos de línea")
        fun `debe manejar mensajes con saltos de linea`() {
            val notificacion = Notificacion(1, "Línea 1\nLínea 2\nLínea 3", false)

            notificacion.mensaje shouldContain "\n"
            notificacion.mensaje shouldContain "Línea 1"
        }
    }

    @Nested
    @DisplayName("Notificacion Business Logic Tests")
    inner class LogicaNegocioNotificacionTests {

        @Test
        @DisplayName("debe poder filtrar notificaciones no leídas")
        fun `debe poder filtrar notificaciones no leidas`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", false)
            )

            val noLeidas = notificaciones.filter { !it.leido }

            noLeidas shouldHaveSize 2
            noLeidas.all { !it.leido } shouldBe true
        }

        @Test
        @DisplayName("debe poder filtrar notificaciones leídas")
        fun `debe poder filtrar notificaciones leidas`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", true)
            )

            val leidas = notificaciones.filter { it.leido }

            leidas shouldHaveSize 2
            leidas.all { it.leido } shouldBe true
        }

        @Test
        @DisplayName("debe poder contar notificaciones no leídas")
        fun `debe poder contar notificaciones no leidas`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", false),
                Notificacion(3, "Mensaje 3", true)
            )

            val countNoLeidas = notificaciones.count { !it.leido }

            countNoLeidas shouldBe 2
        }

        @Test
        @DisplayName("debe poder marcar todas las notificaciones como leídas")
        fun `debe poder marcar todas las notificaciones como leidas`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", false)
            )

            val todasLeidas = notificaciones.map { it.copy(leido = true) }

            todasLeidas.all { it.leido } shouldBe true
            todasLeidas shouldHaveSize 2
        }

        @Test
        @DisplayName("debe poder buscar notificación por id")
        fun `debe poder buscar notificacion por id`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", false)
            )

            val encontrada = notificaciones.find { it.id == 2 }

            encontrada shouldNotBe null
            encontrada?.id shouldBe 2
            encontrada?.leido shouldBe true
        }

        @Test
        @DisplayName("debe poder marcar una notificación específica como leída")
        fun `debe poder marcar una notificacion especifica como leida`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", false),
                Notificacion(3, "Mensaje 3", false)
            )

            val actualizadas = notificaciones.map {
                if (it.id == 2) it.copy(leido = true) else it
            }

            actualizadas[0].leido shouldBe false
            actualizadas[1].leido shouldBe true
            actualizadas[2].leido shouldBe false
        }

        @Test
        @DisplayName("debe poder ordenar notificaciones por id")
        fun `debe poder ordenar notificaciones por id`() {
            val notificaciones = listOf(
                Notificacion(3, "Mensaje 3", false),
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true)
            )

            val ordenadas = notificaciones.sortedBy { it.id }

            ordenadas[0].id shouldBe 1
            ordenadas[1].id shouldBe 2
            ordenadas[2].id shouldBe 3
        }

        @Test
        @DisplayName("debe verificar si existe alguna notificación no leída")
        fun `debe verificar si existe alguna notificacion no leida`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", true),
                Notificacion(2, "Mensaje 2", false),
                Notificacion(3, "Mensaje 3", true)
            )

            val hayNoLeidas = notificaciones.any { !it.leido }

            hayNoLeidas shouldBe true
        }

        @Test
        @DisplayName("debe verificar si todas las notificaciones están leídas")
        fun `debe verificar si todas las notificaciones estan leidas`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", true),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", true)
            )

            val todasLeidas = notificaciones.all { it.leido }

            todasLeidas shouldBe true
        }

        @Test
        @DisplayName("debe poder eliminar notificaciones leídas")
        fun `debe poder eliminar notificaciones leidas`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", false)
            )

            val soloNoLeidas = notificaciones.filter { !it.leido }

            soloNoLeidas shouldHaveSize 2
            soloNoLeidas.none { it.leido } shouldBe true
        }

        @Test
        @DisplayName("debe manejar lista vacía de notificaciones")
        fun `debe manejar lista vacia de notificaciones`() {
            val notificaciones = emptyList<Notificacion>()

            notificaciones.shouldBeEmpty()
            notificaciones.count { !it.leido } shouldBe 0
        }

        @Test
        @DisplayName("debe poder agrupar notificaciones por estado leído")
        fun `debe poder agrupar notificaciones por estado leido`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", false),
                Notificacion(4, "Mensaje 4", true)
            )

            val agrupadas = notificaciones.groupBy { it.leido }

            agrupadas[false]?.shouldHaveSize(2)
            agrupadas[true]?.shouldHaveSize(2)
        }

        @Test
        @DisplayName("debe poder obtener el total de notificaciones")
        fun `debe poder obtener el total de notificaciones`() {
            val notificaciones = listOf(
                Notificacion(1, "Mensaje 1", false),
                Notificacion(2, "Mensaje 2", true),
                Notificacion(3, "Mensaje 3", false)
            )

            notificaciones.size shouldBe 3
            notificaciones.shouldNotBeEmpty()
        }

        @Test
        @DisplayName("debe poder buscar notificaciones por contenido del mensaje")
        fun `debe poder buscar notificaciones por contenido del mensaje`() {
            val notificaciones = listOf(
                Notificacion(1, "Nueva promoción disponible", false),
                Notificacion(2, "Tu pedido fue enviado", true),
                Notificacion(3, "Nueva oferta especial", false)
            )

            val conNueva = notificaciones.filter { it.mensaje.contains("Nueva", ignoreCase = true) }

            conNueva shouldHaveSize 2
            conNueva.all { it.mensaje.contains("Nueva", ignoreCase = true) } shouldBe true
        }
    }

    @Nested
    @DisplayName("Notificacion Edge Cases Tests")
    inner class CasosBordeNotificacionTests {

        @Test
        @DisplayName("debe manejar id cero")
        fun `debe manejar id cero`() {
            val notificacion = Notificacion(0, "Mensaje", false)

            notificacion.id shouldBe 0
        }

        @Test
        @DisplayName("debe manejar cambios múltiples de estado")
        fun `debe manejar cambios multiples de estado`() {
            val inicial = Notificacion(1, "Mensaje", false)
            val leida = inicial.copy(leido = true)
            val noLeida = leida.copy(leido = false)
            val leidaOtraVez = noLeida.copy(leido = true)

            inicial.leido shouldBe false
            leida.leido shouldBe true
            noLeida.leido shouldBe false
            leidaOtraVez.leido shouldBe true
        }

        @Test
        @DisplayName("debe preservar inmutabilidad de data class")
        fun `debe preservar inmutabilidad de data class`() {
            val original = Notificacion(1, "Mensaje", false)
            val modificada = original.copy(leido = true)

            original.leido shouldBe false
            modificada.leido shouldBe true
            original shouldNotBe modificada
        }

        @Test
        @DisplayName("debe manejar mensajes con solo espacios")
        fun `debe manejar mensajes con solo espacios`() {
            val notificacion = Notificacion(1, "   ", false)

            notificacion.mensaje shouldBe "   "
            notificacion.mensaje.isBlank() shouldBe true
        }

        @Test
        @DisplayName("debe poder crear notificaciones con IDs duplicados")
        fun `debe poder crear notificaciones con IDs duplicados`() {
            val notif1 = Notificacion(1, "Mensaje 1", false)
            val notif2 = Notificacion(1, "Mensaje 2", false)
            val lista = listOf(notif1, notif2)

            lista shouldHaveSize 2
            lista[0].id shouldBe lista[1].id
            lista[0] shouldNotBe lista[1]
        }
    }
}