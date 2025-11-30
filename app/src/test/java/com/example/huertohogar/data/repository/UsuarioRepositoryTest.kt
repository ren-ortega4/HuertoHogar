package com.example.huertohogar.data.repository

import com.example.huertohogar.data.local.UsuarioDao
import com.example.huertohogar.model.User
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("Pruebas unitarias de UsuarioRepository")
class UsuarioRepositoryTest {

    private lateinit var usuarioDao: UsuarioDao
    private lateinit var usuarioRepository: UsuarioRepository

    private val usuarioTest = User(
        id = 1,
        nombre = "Juan Pérez",
        correo = "juan@example.com",
        clave = "password123",
        confirmarClave = "password123",
        direccion = "Calle Principal 123",
        region = "Región Metropolitana",
        aceptaTerminos = true,
        fotopefil = null
    )

    private val usuarioTest2 = User(
        id = 2,
        nombre = "María González",
        correo = "maria@example.com",
        clave = "maria456",
        confirmarClave = "maria456",
        direccion = "Avenida Central 456",
        region = "Valparaíso",
        aceptaTerminos = true,
        fotopefil = "content://media/photo.jpg"
    )

    @BeforeEach
    fun setUp() {
        usuarioDao = mockk()
        usuarioRepository = UsuarioRepository(usuarioDao)
    }

    @Test
    @DisplayName("Debe obtener la lista de usuarios correctamente")
    fun `obtenerUsuarios debe retornar lista de usuarios`() = runTest {
        // Given
        val listaUsuarios = listOf(usuarioTest, usuarioTest2)
        coEvery { usuarioDao.obtenerUsuarios() } returns listaUsuarios

        // When
        val resultado = usuarioRepository.obtenerUsuarios()

        // Then
        resultado shouldNotBe null
        resultado.size shouldBe 2
        resultado[0].nombre shouldBe "Juan Pérez"
        resultado[1].nombre shouldBe "María González"
        coVerify(exactly = 1) { usuarioDao.obtenerUsuarios() }
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay usuarios")
    fun `obtenerUsuarios debe retornar lista vacia cuando no hay usuarios`() = runTest {
        // Given
        coEvery { usuarioDao.obtenerUsuarios() } returns emptyList()

        // When
        val resultado = usuarioRepository.obtenerUsuarios()

        // Then
        resultado shouldNotBe null
        resultado.size shouldBe 0
        coVerify(exactly = 1) { usuarioDao.obtenerUsuarios() }
    }

    @Test
    @DisplayName("Debe insertar un usuario correctamente")
    fun `insertar debe llamar al dao con el usuario correcto`() = runTest {
        // Given
        coEvery { usuarioDao.insertar(any()) } returns Unit

        // When
        usuarioRepository.insertar(usuarioTest)

        // Then
        coVerify(exactly = 1) { usuarioDao.insertar(usuarioTest) }
    }

    @Test
    @DisplayName("Debe insertar múltiples usuarios correctamente")
    fun `insertar debe permitir insertar varios usuarios`() = runTest {
        // Given
        coEvery { usuarioDao.insertar(any()) } returns Unit

        // When
        usuarioRepository.insertar(usuarioTest)
        usuarioRepository.insertar(usuarioTest2)

        // Then
        coVerify(exactly = 1) { usuarioDao.insertar(usuarioTest) }
        coVerify(exactly = 1) { usuarioDao.insertar(usuarioTest2) }
    }

    @Test
    @DisplayName("Debe eliminar un usuario correctamente")
    fun `eliminar debe llamar al dao con el usuario correcto`() = runTest {
        // Given
        coEvery { usuarioDao.eliminar(any()) } returns Unit

        // When
        usuarioRepository.eliminar(usuarioTest)

        // Then
        coVerify(exactly = 1) { usuarioDao.eliminar(usuarioTest) }
    }

    @Test
    @DisplayName("Debe permitir eliminar múltiples usuarios")
    fun `eliminar debe permitir eliminar varios usuarios`() = runTest {
        // Given
        coEvery { usuarioDao.eliminar(any()) } returns Unit

        // When
        usuarioRepository.eliminar(usuarioTest)
        usuarioRepository.eliminar(usuarioTest2)

        // Then
        coVerify(exactly = 1) { usuarioDao.eliminar(usuarioTest) }
        coVerify(exactly = 1) { usuarioDao.eliminar(usuarioTest2) }
    }

    @Test
    @DisplayName("Debe realizar login exitosamente con credenciales correctas")
    fun `login debe retornar usuario cuando credenciales son correctas`() = runTest {
        // Given
        val correo = "juan@example.com"
        val clave = "password123"
        coEvery { usuarioDao.login(correo, clave) } returns usuarioTest

        // When
        val resultado = usuarioRepository.login(correo, clave)

        // Then
        resultado.shouldNotBeNull()
        resultado.correo shouldBe correo
        resultado.clave shouldBe clave
        resultado.nombre shouldBe "Juan Pérez"
        coVerify(exactly = 1) { usuarioDao.login(correo, clave) }
    }

    @Test
    @DisplayName("Debe retornar null cuando las credenciales son incorrectas")
    fun `login debe retornar null cuando credenciales son incorrectas`() = runTest {
        // Given
        val correo = "juan@example.com"
        val claveIncorrecta = "wrongpassword"
        coEvery { usuarioDao.login(correo, claveIncorrecta) } returns null

        // When
        val resultado = usuarioRepository.login(correo, claveIncorrecta)

        // Then
        resultado.shouldBeNull()
        coVerify(exactly = 1) { usuarioDao.login(correo, claveIncorrecta) }
    }

    @Test
    @DisplayName("Debe retornar null cuando el correo no existe")
    fun `login debe retornar null cuando correo no existe`() = runTest {
        // Given
        val correoInexistente = "noexiste@example.com"
        val clave = "password123"
        coEvery { usuarioDao.login(correoInexistente, clave) } returns null

        // When
        val resultado = usuarioRepository.login(correoInexistente, clave)

        // Then
        resultado.shouldBeNull()
        coVerify(exactly = 1) { usuarioDao.login(correoInexistente, clave) }
    }

    @Test
    @DisplayName("Debe actualizar la foto de perfil correctamente")
    fun `actualizarFoto debe llamar al dao con parametros correctos`() = runTest {
        // Given
        val id = 1
        val nuevaUri = "content://media/nueva_foto.jpg"
        coEvery { usuarioDao.actualizarFoto(id, nuevaUri) } returns Unit

        // When
        usuarioRepository.actualizarFoto(id, nuevaUri)

        // Then
        coVerify(exactly = 1) { usuarioDao.actualizarFoto(id, nuevaUri) }
    }

    @Test
    @DisplayName("Debe permitir actualizar foto a null")
    fun `actualizarFoto debe permitir establecer foto como null`() = runTest {
        // Given
        val id = 1
        coEvery { usuarioDao.actualizarFoto(id, null) } returns Unit

        // When
        usuarioRepository.actualizarFoto(id, null)

        // Then
        coVerify(exactly = 1) { usuarioDao.actualizarFoto(id, null) }
    }

    @Test
    @DisplayName("Debe actualizar foto para diferentes usuarios")
    fun `actualizarFoto debe funcionar para diferentes usuarios`() = runTest {
        // Given
        val id1 = 1
        val id2 = 2
        val uri1 = "content://media/foto1.jpg"
        val uri2 = "content://media/foto2.jpg"
        coEvery { usuarioDao.actualizarFoto(any(), any()) } returns Unit

        // When
        usuarioRepository.actualizarFoto(id1, uri1)
        usuarioRepository.actualizarFoto(id2, uri2)

        // Then
        coVerify(exactly = 1) { usuarioDao.actualizarFoto(id1, uri1) }
        coVerify(exactly = 1) { usuarioDao.actualizarFoto(id2, uri2) }
    }
}