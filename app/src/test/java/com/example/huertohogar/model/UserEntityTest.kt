package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class UserEntityTest {

    @Test
    fun idPorDefectoEsCeroEnUserEntity() {
        val ue = UserEntity(
            nombre = "Nombre",
            apellido = "Apellido",
            correo = "correo@example.com",
            region = "Region",
            fecha_registro = "2025-01-01",
            estado = true
        )

        assertEquals(0L, ue.id)
    }

    @Test
    fun idSeMantieneSiSeProporciona() {
        val ue = UserEntity(
            id = 42L,
            idApi = 7,
            nombre = "N",
            apellido = "A",
            correo = "c@c",
            region = "R",
            fecha_registro = "2025-01-02",
            estado = false
        )

        assertEquals(42L, ue.id)
        assertEquals(Integer.valueOf(7), ue.idApi)
    }
}

