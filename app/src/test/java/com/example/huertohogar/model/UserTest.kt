package com.example.huertohogar.model

import org.junit.Test
import org.junit.Assert.*

class UserTest {

    @Test
    fun mapeaEntidadConTodosLosCampos() {
        val user = User(
            id_usuario = 123L,
            nombre = "Nombre",
            apellido = "Apellido",
            correo = "correo@example.com",
            region = "Región",
            contrasena = "secret",
            fecha_registro = "2025-01-01",
            estado = true,
            rol = null,
            fotopefil = "https://example.com/avatar.png"
        )

        val entity = user.toEntity()

        assertEquals(0, entity.id)
        assertEquals(123, entity.idApi)
        assertEquals("Nombre", entity.nombre)
        assertEquals("Apellido", entity.apellido)
        assertEquals("correo@example.com", entity.correo)
        assertEquals("Región", entity.region)
        assertEquals("2025-01-01", entity.fecha_registro)
        assertTrue(entity.estado)
        assertEquals("https://example.com/avatar.png", entity.fotopefil)
    }

    @Test
    fun cuandoIdUsuarioEsNuloIdApiEsNulo() {
        val user = User(
            id_usuario = null,
            nombre = "N",
            apellido = "A",
            correo = "c@c",
            region = "R",
            contrasena = "p",
            fecha_registro = "2025-01-02",
            estado = false,
            rol = null,
            fotopefil = null
        )

        val entity = user.toEntity()

        assertEquals(0, entity.id)
        assertNull(entity.idApi)
        assertEquals("N", entity.nombre)
        assertEquals("A", entity.apellido)
        assertEquals("c@c", entity.correo)
        assertEquals("R", entity.region)
        assertEquals("2025-01-02", entity.fecha_registro)
        assertFalse(entity.estado)
        assertNull(entity.fotopefil)
    }

    @Test
    fun fotopefilSeMapeaCorrectamente() {
        // Caso: fotopefil con URL
        val userConFoto = User(
            id_usuario = 5L,
            nombre = "P",
            apellido = "Q",
            correo = "p@q",
            region = "Reg",
            contrasena = "x",
            fecha_registro = "2025-02-02",
            estado = true,
            rol = null,
            fotopefil = "https://imagenes/usuario.png"
        )

        val entityConFoto = userConFoto.toEntity()
        assertEquals("https://imagenes/usuario.png", entityConFoto.fotopefil)

        // Caso: fotopefil null
        val userSinFoto = User(
            id_usuario = 6L,
            nombre = "R",
            apellido = "S",
            correo = "r@s",
            region = "Reg",
            contrasena = "y",
            fecha_registro = "2025-03-03",
            estado = false,
            rol = null,
            fotopefil = null
        )

        val entitySinFoto = userSinFoto.toEntity()
        assertNull(entitySinFoto.fotopefil)
    }

    @Test
    fun rolRequestTieneValorEsperado() {
        val rol = RolRequest(id_rol = 7)
        assertEquals(7, rol.id_rol)
    }
}