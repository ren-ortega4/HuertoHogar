package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class TipTest {

    @Test
    fun propiedadesTipEstanCorrectas() {
        val tip = Tip(iconName = "icono", title = "Título", text = "Texto descriptivo")

        assertEquals(0, tip.id)
        assertEquals("icono", tip.iconName)
        assertEquals("Título", tip.title)
        assertEquals("Texto descriptivo", tip.text)
    }

    @Test
    fun igualdadYCopiaDeTip() {
        val t1 = Tip(id = 1, iconName = "i", title = "t", text = "x")
        val t2 = Tip(id = 1, iconName = "i", title = "t", text = "x")
        assertEquals(t1, t2)

        val t3 = t1.copy(id = 2)
        assertNotEquals(t1, t3)
        assertEquals(2, t3.id)
    }

    @Test
    fun idPorDefectoEsCero() {
        val tip = Tip(iconName = "ic", title = "ti", text = "tx")
        assertEquals(0, tip.id)
    }
}