package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class CartRequestTest {

    @Test
    fun propiedadesItemEstanCorrectas() {
        val item = Item(title = "Manzana", quantity = 3, unitPrice = 150.0)

        assertEquals("Manzana", item.title)
        assertEquals(3, item.quantity)
        assertEquals(150.0, item.unitPrice, 0.0001)
    }

    @Test
    fun igualdadYCopiaDeItem() {
        val i1 = Item(title = "X", quantity = 1, unitPrice = 10.0)
        val i2 = Item(title = "X", quantity = 1, unitPrice = 10.0)
        assertEquals(i1, i2)

        val i3 = i1.copy(quantity = 2)
        assertNotEquals(i1, i3)
        assertEquals(2, i3.quantity)
    }

    @Test
    fun cuandoCantidadEsCeroSePermite() {
        val item = Item(title = "Y", quantity = 0, unitPrice = 5.0)
        assertEquals(0, item.quantity)
    }

    @Test
    fun propiedadesCartRequestEstanCorrectas() {
        val items = listOf(
            Item(title = "A", quantity = 1, unitPrice = 10.0),
            Item(title = "B", quantity = 2, unitPrice = 20.0)
        )
        val cart = CartRequest(items = items)

        assertEquals(2, cart.items.size)
        assertEquals("A", cart.items[0].title)
        assertEquals(2, cart.items[1].quantity)
    }

    @Test
    fun carritoVacioEsValido() {
        val cart = CartRequest(items = emptyList())
        assertTrue(cart.items.isEmpty())
    }
}