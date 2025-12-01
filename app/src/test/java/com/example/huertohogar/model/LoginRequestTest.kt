package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class LoginRequestTest {

    @Test
    fun propiedadesLoginRequestEstanCorrectas() {
        val lr = LoginRequest(correo = "user@example.com", contrasena = "secret")

        assertEquals("user@example.com", lr.correo)
        assertEquals("secret", lr.contrasena)
    }

    @Test
    fun cuandoCorreoEstaVacioSePermiteComoValor() {
        val lr = LoginRequest(correo = "", contrasena = "pass")

        assertEquals("", lr.correo)
        assertEquals("pass", lr.contrasena)
    }

    @Test
    fun igualdadYCopiaDeLoginRequest() {
        val original = LoginRequest(correo = "a@b.com", contrasena = "p")
        val copia = LoginRequest(correo = "a@b.com", contrasena = "p")

        assertEquals(original, copia)

        val modificada = original.copy(contrasena = "nuevo")
        assertNotEquals(original, modificada)
        assertEquals("nuevo", modificada.contrasena)
        assertEquals("a@b.com", modificada.correo)
    }

    @Test
    fun camposNoSonNulos() {
        val lr = LoginRequest(correo = "x", contrasena = "y")
        assertNotNull(lr.correo)
        assertNotNull(lr.contrasena)
    }
}
