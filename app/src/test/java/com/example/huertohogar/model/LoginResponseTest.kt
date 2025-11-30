package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class LoginResponseTest {

    @Test
    fun propiedadesLoginResponseEstanCorrectas() {
        val user = User(
            id_usuario = 10L,
            nombre = "Juan",
            apellido = "Perez",
            correo = "juan@example.com",
            region = "RM",
            contrasena = "pass",
            fecha_registro = "2025-01-01",
            estado = true,
            rol = null,
            fotopefil = null
        )

        val lr = LoginResponse(token = "abc123", user = user)

        assertEquals("abc123", lr.token)
        assertNotNull(lr.user)
        assertEquals(10L, lr.user?.id_usuario)
        assertEquals("juan@example.com", lr.user?.correo)
    }

    @Test
    fun cuandoUserEsNuloSeAceptaNull() {
        val lr = LoginResponse(token = "tok", user = null)
        assertEquals("tok", lr.token)
        assertNull(lr.user)
    }

    @Test
    fun igualdadYCopiaDeLoginResponse() {
        val user = User(
            id_usuario = 1L,
            nombre = "A",
            apellido = "B",
            correo = "c@c",
            region = "R",
            contrasena = "p",
            fecha_registro = "2025-01-01",
            estado = false,
            rol = null,
            fotopefil = null
        )

        val l1 = LoginResponse(token = "t", user = user)
        val l2 = LoginResponse(token = "t", user = user)
        assertEquals(l1, l2)

        val l3 = l1.copy(token = "nuevo")
        assertNotEquals(l1, l3)
        assertEquals("nuevo", l3.token)
    }
}