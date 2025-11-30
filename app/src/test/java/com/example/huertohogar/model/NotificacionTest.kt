package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class NotificacionTest {

    @Test
    fun propiedadesNotificacionEstanCorrectas() {
        val n = Notificacion(id = 5, mensaje = "Hola", leido = true)

        assertEquals(5, n.id)
        assertEquals("Hola", n.mensaje)
        assertTrue(n.leido)
    }

    @Test
    fun igualdadYCopiaDeNotificacion() {
        val n1 = Notificacion(id = 1, mensaje = "X", leido = false)
        val n2 = Notificacion(id = 1, mensaje = "X", leido = false)
        assertEquals(n1, n2)

        val n3 = n1.copy(id = 2)
        assertNotEquals(n1, n3)
        assertEquals(2, n3.id)
    }

    @Test
    fun leidoPorDefectoEsFalse() {
        val n = Notificacion(id = 10, mensaje = "Mensaje")
        assertFalse(n.leido)
    }
}