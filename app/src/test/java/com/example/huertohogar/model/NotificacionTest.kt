package com.example.huertohogar.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NotificacionTest {

    @Test
    fun `crear notificacion con todos los parametros`() {
        // Given
        val id = 1
        val mensaje = "Nueva notificación de riego"
        val leido = true

        // When
        val notificacion = Notificacion(id, mensaje, leido)

        // Then
        assertEquals(id, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
        assertEquals(leido, notificacion.leido)
    }

    @Test
    fun `crear notificacion sin especificar leido debe tener valor false por defecto`() {
        // Given
        val id = 2
        val mensaje = "Notificación pendiente"

        // When
        val notificacion = Notificacion(id, mensaje)

        // Then
        assertEquals(id, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
        assertFalse(notificacion.leido)
    }

    @Test
    fun `crear notificacion con leido false`() {
        // Given
        val id = 3
        val mensaje = "Notificación no leída"
        val leido = false

        // When
        val notificacion = Notificacion(id, mensaje, leido)

        // Then
        assertEquals(id, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
        assertFalse(notificacion.leido)
    }

    @Test
    fun `crear notificacion con mensaje vacio`() {
        // Given
        val id = 4
        val mensaje = ""

        // When
        val notificacion = Notificacion(id, mensaje)

        // Then
        assertEquals(id, notificacion.id)
        assertEquals("", notificacion.mensaje)
        assertFalse(notificacion.leido)
    }

    @Test
    fun `crear notificacion con id negativo`() {
        // Given
        val id = -1
        val mensaje = "Notificación con id negativo"

        // When
        val notificacion = Notificacion(id, mensaje)

        // Then
        assertEquals(-1, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
    }

    @Test
    fun `crear notificacion con id cero`() {
        // Given
        val id = 0
        val mensaje = "Notificación con id cero"

        // When
        val notificacion = Notificacion(id, mensaje)

        // Then
        assertEquals(0, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
    }

    @Test
    fun `dos notificaciones con los mismos valores son iguales`() {
        // Given
        val notificacion1 = Notificacion(1, "Mensaje", true)
        val notificacion2 = Notificacion(1, "Mensaje", true)

        // Then
        assertEquals(notificacion1, notificacion2)
    }

    @Test
    fun `dos notificaciones con diferentes ids no son iguales`() {
        // Given
        val notificacion1 = Notificacion(1, "Mensaje", true)
        val notificacion2 = Notificacion(2, "Mensaje", true)

        // Then
        assertNotEquals(notificacion1, notificacion2)
    }

    @Test
    fun `dos notificaciones con diferentes mensajes no son iguales`() {
        // Given
        val notificacion1 = Notificacion(1, "Mensaje 1", true)
        val notificacion2 = Notificacion(1, "Mensaje 2", true)

        // Then
        assertNotEquals(notificacion1, notificacion2)
    }

    @Test
    fun `dos notificaciones con diferentes estados de leido no son iguales`() {
        // Given
        val notificacion1 = Notificacion(1, "Mensaje", true)
        val notificacion2 = Notificacion(1, "Mensaje", false)

        // Then
        assertNotEquals(notificacion1, notificacion2)
    }

    @Test
    fun `dos notificaciones iguales tienen el mismo hashCode`() {
        // Given
        val notificacion1 = Notificacion(1, "Mensaje", true)
        val notificacion2 = Notificacion(1, "Mensaje", true)

        // Then
        assertEquals(notificacion1.hashCode(), notificacion2.hashCode())
    }

    @Test
    fun `toString debe contener todos los valores`() {
        // Given
        val notificacion = Notificacion(1, "Mensaje de prueba", true)

        // When
        val resultado = notificacion.toString()

        // Then
        assertTrue(resultado.contains("1"))
        assertTrue(resultado.contains("Mensaje de prueba"))
        assertTrue(resultado.contains("true"))
    }

    @Test
    fun `copy debe crear una nueva instancia con los mismos valores`() {
        // Given
        val original = Notificacion(1, "Mensaje original", false)

        // When
        val copia = original.copy()

        // Then
        assertEquals(original, copia)
        assertNotSame(original, copia)
    }

    @Test
    fun `copy con cambio de id`() {
        // Given
        val original = Notificacion(1, "Mensaje", false)

        // When
        val modificada = original.copy(id = 2)

        // Then
        assertEquals(2, modificada.id)
        assertEquals(original.mensaje, modificada.mensaje)
        assertEquals(original.leido, modificada.leido)
    }

    @Test
    fun `copy con cambio de mensaje`() {
        // Given
        val original = Notificacion(1, "Mensaje original", false)

        // When
        val modificada = original.copy(mensaje = "Mensaje modificado")

        // Then
        assertEquals(original.id, modificada.id)
        assertEquals("Mensaje modificado", modificada.mensaje)
        assertEquals(original.leido, modificada.leido)
    }

    @Test
    fun `copy con cambio de leido`() {
        // Given
        val original = Notificacion(1, "Mensaje", false)

        // When
        val modificada = original.copy(leido = true)

        // Then
        assertEquals(original.id, modificada.id)
        assertEquals(original.mensaje, modificada.mensaje)
        assertTrue(modificada.leido)
    }

    @Test
    fun `copy con cambio de todos los parametros`() {
        // Given
        val original = Notificacion(1, "Mensaje original", false)

        // When
        val modificada = original.copy(id = 99, mensaje = "Nuevo mensaje", leido = true)

        // Then
        assertEquals(99, modificada.id)
        assertEquals("Nuevo mensaje", modificada.mensaje)
        assertTrue(modificada.leido)
    }

    @Test
    fun `crear notificacion con mensaje largo`() {
        // Given
        val id = 5
        val mensaje = "Este es un mensaje muy largo que podría representar una notificación detallada con mucha información sobre el estado de las plantas y el riego necesario"

        // When
        val notificacion = Notificacion(id, mensaje)

        // Then
        assertEquals(id, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
    }

    @Test
    fun `crear notificacion con caracteres especiales en mensaje`() {
        // Given
        val id = 6
        val mensaje = "¡Atención! Planta #1 necesita agua 💧"

        // When
        val notificacion = Notificacion(id, mensaje)

        // Then
        assertEquals(id, notificacion.id)
        assertEquals(mensaje, notificacion.mensaje)
    }
}