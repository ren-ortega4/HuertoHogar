package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class TiendaTest {

    @Test
    fun propiedadesTiendaEstanCorrectas() {
        val tienda = Tienda(
            name = "Mi Tienda",
            address = "Calle Falsa 123",
            phone = "+56912345678",
            latitude = -33.45,
            longitude = -70.66,
            description = "Tienda de ejemplo"
        )

        assertEquals(0, tienda.id)
        assertEquals("Mi Tienda", tienda.name)
        assertEquals("Calle Falsa 123", tienda.address)
        assertEquals("+56912345678", tienda.phone)
        assertEquals(-33.45, tienda.latitude, 0.0001)
        assertEquals(-70.66, tienda.longitude, 0.0001)
        assertEquals("Tienda de ejemplo", tienda.description)
    }

    @Test
    fun igualdadYCopiaDeTienda() {
        val t1 = Tienda(id = 1, name = "A", address = "B", phone = "C", latitude = 1.0, longitude = 2.0, description = "D")
        val t2 = Tienda(id = 1, name = "A", address = "B", phone = "C", latitude = 1.0, longitude = 2.0, description = "D")
        assertEquals(t1, t2)

        val t3 = t1.copy(id = 2)
        assertNotEquals(t1, t3)
        assertEquals(2, t3.id)
    }

    @Test
    fun idPorDefectoEsCero() {
        val tienda = Tienda(name = "X", address = "Y", phone = "Z", latitude = 0.0, longitude = 0.0, description = "desc")
        assertEquals(0, tienda.id)
    }
}