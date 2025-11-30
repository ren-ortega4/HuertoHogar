package com.example.huertohogar.repository

import com.example.huertohogar.data.local.UsuarioDao
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.model.LoginRequest
import com.example.huertohogar.model.LoginResponse
import com.example.huertohogar.model.User
import com.example.huertohogar.model.UserEntity
import com.example.huertohogar.network.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class UsuarioRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var usuarioRepository: UsuarioRepository

    @BeforeEach
    fun setUp() {
        apiService = mockk(relaxed = true)
        usuarioDao = mockk(relaxed = true)
        every { usuarioDao.getActiveUser() } returns flowOf(null)
        every { usuarioDao.getAllUsers() } returns flowOf(emptyList())
        every { usuarioDao.getUserById(any()) } returns flowOf(null)
        usuarioRepository = UsuarioRepository(apiService, usuarioDao)
    }

    @Test
    fun `activeUser should return flow from dao`() = runTest {
        // Given
        val user = UserEntity(id = 1, nombre = "Juan", apellido = "Pérez", correo = "juan@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true)
        every { usuarioDao.getActiveUser() } returns flowOf(user)
        val newRepository = UsuarioRepository(apiService, usuarioDao)

        // When
        val result = newRepository.activeUser.first()

        // Then
        assertNotNull(result)
        assertEquals("Juan", result?.nombre)
        assertTrue(result?.estado == true)
    }

    @Test
    fun `register should return true when API registration succeeds`() = runTest {
        // Given
        val user = User(id_usuario = null, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", contrasena = "pass", fecha_registro = "2024-01-01", estado = true, rol = null)
        val apiUser = user.copy(id_usuario = 100)
        coEvery { apiService.registarUsusario(user) } returns Response.success(apiUser)
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.register(user)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { apiService.registarUsusario(user) }
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.idApi == 100 && it.estado == true }) }
    }

    @Test
    fun `register should save locally with estado false when API fails`() = runTest {
        // Given
        val user = User(id_usuario = null, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", contrasena = "pass", fecha_registro = "2024-01-01", estado = true, rol = null)
        coEvery { apiService.registarUsusario(user) } returns Response.error(500, "".toResponseBody())
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.register(user)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.estado == false }) }
    }

    @Test
    fun `register should save locally when API throws exception`() = runTest {
        // Given
        val user = User(id_usuario = null, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", contrasena = "pass", fecha_registro = "2024-01-01", estado = true, rol = null)
        coEvery { apiService.registarUsusario(user) } throws Exception("Network error")
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.register(user)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.estado == false }) }
    }

    @Test
    fun `register should return false when both API and DB fail`() = runTest {
        // Given
        val user = User(id_usuario = null, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", contrasena = "pass", fecha_registro = "2024-01-01", estado = true, rol = null)
        coEvery { apiService.registarUsusario(user) } throws Exception("Network error")
        coEvery { usuarioDao.upsert(any()) } throws Exception("DB error")

        // When
        val result = usuarioRepository.register(user)

        // Then
        assertFalse(result)
    }

    @Test
    fun `login should return true when API login succeeds`() = runTest {
        // Given
        val correo = "test@test.com"
        val clave = "password"
        val token = "fake-token"
        val apiUser = User(id_usuario = 100, nombre = "Test", apellido = "User", correo = correo, region = "RM", contrasena = clave, fecha_registro = "2024-01-01", estado = true, rol = null)
        
        coEvery { apiService.login(any()) } returns Response.success(LoginResponse(token, apiUser))
        coEvery { apiService.getallUser(any()) } returns Response.success(listOf(apiUser))
        every { usuarioDao.getAllUsers() } returns flowOf(emptyList())
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.login(correo, clave)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { apiService.login(match { it.correo == correo }) }
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.estado == true }) }
    }

    @Test
    fun `login should fallback to offline when API login fails`() = runTest {
        // Given
        val correo = "test@test.com"
        val clave = "password"
        val localUser = UserEntity(id = 1, nombre = "Test", apellido = "User", correo = correo, region = "RM", fecha_registro = "2024-01-01", estado = false)
        
        coEvery { apiService.login(any()) } returns Response.error(401, "".toResponseBody())
        every { usuarioDao.getAllUsers() } returns flowOf(listOf(localUser))
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.login(correo, clave)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.correo == correo && it.estado == true }) }
    }

    @Test
    fun `login should return false when offline user not found`() = runTest {
        // Given
        val correo = "test@test.com"
        val clave = "password"
        
        coEvery { apiService.login(any()) } throws Exception("Network error")
        every { usuarioDao.getAllUsers() } returns flowOf(emptyList())

        // When
        val result = usuarioRepository.login(correo, clave)

        // Then
        assertFalse(result)
    }

    @Test
    fun `login should preserve local id when user exists locally`() = runTest {
        // Given
        val correo = "test@test.com"
        val clave = "password"
        val token = "fake-token"
        val localUser = UserEntity(id = 5, nombre = "Test", apellido = "User", correo = correo, region = "RM", fecha_registro = "2024-01-01", estado = false)
        val apiUser = User(id_usuario = 100, nombre = "Test Updated", apellido = "User", correo = correo, region = "RM", contrasena = clave, fecha_registro = "2024-01-01", estado = true, rol = null)
        
        coEvery { apiService.login(any()) } returns Response.success(LoginResponse(token, apiUser))
        coEvery { apiService.getallUser(any()) } returns Response.success(listOf(apiUser))
        every { usuarioDao.getAllUsers() } returns flowOf(listOf(localUser))
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.login(correo, clave)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.id == 5L && it.estado == true }) }
    }

    @Test
    fun `login should fallback to offline when getallUser fails`() = runTest {
        // Given
        val correo = "test@test.com"
        val clave = "password"
        val token = "fake-token"
        val localUser = UserEntity(id = 1, nombre = "Test", apellido = "User", correo = correo, region = "RM", fecha_registro = "2024-01-01", estado = false)
        
        coEvery { apiService.login(any()) } returns Response.success(LoginResponse(token, null))
        coEvery { apiService.getallUser(any()) } returns Response.error(500, "".toResponseBody())
        every { usuarioDao.getAllUsers() } returns flowOf(listOf(localUser))
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.login(correo, clave)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.estado == true }) }
    }

    @Test
    fun `logout should mark active user as inactive`() = runTest {
        // Given
        val activeUser = UserEntity(id = 1, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", fecha_registro = "2024-01-01", estado = true)
        every { usuarioDao.getActiveUser() } returns flowOf(activeUser)
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        usuarioRepository.logout()

        // Then
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.id == 1L && it.estado == false }) }
    }

    @Test
    fun `logout should do nothing when no active user`() = runTest {
        // Given
        every { usuarioDao.getActiveUser() } returns flowOf(null)

        // When
        usuarioRepository.logout()

        // Then
        coVerify(exactly = 0) { usuarioDao.upsert(any()) }
    }

    @Test
    fun `ActualizarFotoPerfil should update user photo`() = runTest {
        // Given
        val userId = 1L
        val photoUri = "content://photos/1"
        val user = UserEntity(id = userId, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", fecha_registro = "2024-01-01", estado = true, fotopefil = null)
        
        every { usuarioDao.getUserById(userId) } returns flowOf(user)
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        usuarioRepository.ActualizarFotoPerfil(userId, photoUri)

        // Then
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.fotopefil == photoUri }) }
    }

    @Test
    fun `ActualizarFotoPerfil should do nothing when user not found`() = runTest {
        // Given
        val userId = 999L
        val photoUri = "content://photos/1"
        
        every { usuarioDao.getUserById(userId) } returns flowOf(null)

        // When
        usuarioRepository.ActualizarFotoPerfil(userId, photoUri)

        // Then
        coVerify(exactly = 0) { usuarioDao.upsert(any()) }
    }

    @Test
    fun `eliminarUsuarioApi should call API service`() = runTest {
        // Given
        val userId = 100
        coEvery { apiService.eliminarUsuario(userId) } returns Response.success(Unit)

        // When
        usuarioRepository.eliminarUsuarioApi(userId)

        // Then
        coVerify(exactly = 1) { apiService.eliminarUsuario(userId) }
    }

    @Test
    fun `eliminarUsuarioLocal should call dao deleteById`() = runTest {
        // Given
        val userId = 1L
        coEvery { usuarioDao.deleteById(userId) } returns Unit

        // When
        usuarioRepository.eliminarUsuarioLocal(userId)

        // Then
        coVerify(exactly = 1) { usuarioDao.deleteById(userId) }
    }

    @Test
    fun `login should handle user not found in API list`() = runTest {
        // Given
        val correo = "test@test.com"
        val clave = "password"
        val token = "fake-token"
        val otherUser = User(id_usuario = 100, nombre = "Other", apellido = "User", correo = "other@test.com", region = "RM", contrasena = "pass", fecha_registro = "2024-01-01", estado = true, rol = null)
        val localUser = UserEntity(id = 1, nombre = "Test", apellido = "User", correo = correo, region = "RM", fecha_registro = "2024-01-01", estado = false)
        
        coEvery { apiService.login(any()) } returns Response.success(LoginResponse(token, null))
        coEvery { apiService.getallUser(any()) } returns Response.success(listOf(otherUser))
        every { usuarioDao.getAllUsers() } returns flowOf(listOf(localUser))
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.login(correo, clave)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.correo == correo && it.estado == true }) }
    }

    @Test
    fun `register should handle API returning null body`() = runTest {
        // Given
        val user = User(id_usuario = null, nombre = "Test", apellido = "User", correo = "test@test.com", region = "RM", contrasena = "pass", fecha_registro = "2024-01-01", estado = true, rol = null)
        coEvery { apiService.registarUsusario(user) } returns Response.success(null)
        coEvery { usuarioDao.upsert(any()) } returns Unit

        // When
        val result = usuarioRepository.register(user)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { usuarioDao.upsert(match { it.estado == false }) }
    }
}
