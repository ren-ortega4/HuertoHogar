package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class PreferenceResponseTest {

    @Test
    fun propiedadesPreferenceResponseEstanCorrectas() {
        val pref = PreferenceResponse(id = "123", init_point = "https://init.point")

        assertEquals("123", pref.id)
        assertEquals("https://init.point", pref.init_point)
    }

    @Test
    fun igualdadYCopiaDePreferenceResponse() {
        val p1 = PreferenceResponse(id = "1", init_point = "a")
        val p2 = PreferenceResponse(id = "1", init_point = "a")
        assertEquals(p1, p2)

        val p3 = p1.copy(id = "2")
        assertNotEquals(p1, p3)
        assertEquals("2", p3.id)
    }

    @Test
    fun camposNoSonNulos() {
        val pref = PreferenceResponse(id = "x", init_point = "y")
        assertNotNull(pref.id)
        assertNotNull(pref.init_point)
    }
}