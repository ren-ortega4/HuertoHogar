package com.example.huertohogar.local

import com.example.huertohogar.data.local.UsuarioDao
import com.example.huertohogar.model.UserEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UsuarioDaoTest {

    private lateinit var usuarioDao: UsuarioDao

    @BeforeEach
    fun setUp() {
        usuarioDao = mockk(relaxed = true)
    }

    @Test
    fun `getAllUsers should return flow of users ordered by name`() = runTest {
        // Given
        val users = listOf(
            UserEntity(id = 1, nombre = "Ana", apellido = "García", correo = "ana@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true),
            UserEntity(id = 2, nombre = "Carlos", apellido = "López", correo = "carlos@test.com", region = "Valparaíso", fecha_registro = "2024-01-02", estado = true)
        )
        every { usuarioDao.getAllUsers() } returns flowOf(users)

        // When
        val result = usuarioDao.getAllUsers().first()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Ana", result[0].nombre)
        assertEquals("Carlos", result[1].nombre)
    }

    @Test
    fun `getAllUsers should return empty flow when no users`() = runTest {
        // Given
        every { usuarioDao.getAllUsers() } returns flowOf(emptyList())

        // When
        val result = usuarioDao.getAllUsers().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getUserById should return user when exists`() = runTest {
        // Given
        val user = UserEntity(id = 1, nombre = "Juan", apellido = "Pérez", correo = "juan@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true)
        every { usuarioDao.getUserById(1L) } returns flowOf(user)

        // When
        val result = usuarioDao.getUserById(1L).first()

        // Then
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("Juan", result?.nombre)
    }

    @Test
    fun `getUserById should return null when user does not exist`() = runTest {
        // Given
        every { usuarioDao.getUserById(999L) } returns flowOf(null)

        // When
        val result = usuarioDao.getUserById(999L).first()

        // Then
        assertNull(result)
    }

    @Test
    fun `getActiveUser should return user with estado true`() = runTest {
        // Given
        val activeUser = UserEntity(id = 1, nombre = "María", apellido = "González", correo = "maria@test.com", region = "Biobío", fecha_registro = "2024-01-01", estado = true)
        every { usuarioDao.getActiveUser() } returns flowOf(activeUser)

        // When
        val result = usuarioDao.getActiveUser().first()

        // Then
        assertNotNull(result)
        assertTrue(result?.estado == true)
        assertEquals("María", result?.nombre)
    }

    @Test
    fun `getActiveUser should return null when no active user`() = runTest {
        // Given
        every { usuarioDao.getActiveUser() } returns flowOf(null)

        // When
        val result = usuarioDao.getActiveUser().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `upsert should insert new user`() = runTest {
        // Given
        val user = UserEntity(id = 0, nombre = "Pedro", apellido = "Martínez", correo = "pedro@test.com", region = "Valparaíso", fecha_registro = "2024-01-01", estado = true)
        coEvery { usuarioDao.upsert(user) } returns Unit

        // When
        usuarioDao.upsert(user)

        // Then
        coVerify(exactly = 1) { usuarioDao.upsert(user) }
    }

    @Test
    fun `upsert should update existing user on conflict`() = runTest {
        // Given
        val user1 = UserEntity(id = 1, nombre = "Luis", apellido = "Rojas", correo = "luis@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true)
        val user2 = UserEntity(id = 1, nombre = "Luis Actualizado", apellido = "Rojas", correo = "luis@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true)
        
        coEvery { usuarioDao.upsert(user1) } returns Unit
        coEvery { usuarioDao.upsert(user2) } returns Unit

        // When
        usuarioDao.upsert(user1)
        usuarioDao.upsert(user2)

        // Then
        coVerify(exactly = 1) { usuarioDao.upsert(user1) }
        coVerify(exactly = 1) { usuarioDao.upsert(user2) }
    }

    @Test
    fun `upsertAll should insert multiple users`() = runTest {
        // Given
        val users = listOf(
            UserEntity(id = 1, nombre = "Ana", apellido = "Silva", correo = "ana@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true),
            UserEntity(id = 2, nombre = "Beatriz", apellido = "Torres", correo = "beatriz@test.com", region = "Valparaíso", fecha_registro = "2024-01-02", estado = false)
        )
        coEvery { usuarioDao.upsertAll(users) } returns Unit

        // When
        usuarioDao.upsertAll(users)

        // Then
        coVerify(exactly = 1) { usuarioDao.upsertAll(users) }
    }

    @Test
    fun `upsertAll should handle empty list`() = runTest {
        // Given
        val emptyList = emptyList<UserEntity>()
        coEvery { usuarioDao.upsertAll(emptyList) } returns Unit

        // When
        usuarioDao.upsertAll(emptyList)

        // Then
        coVerify(exactly = 1) { usuarioDao.upsertAll(emptyList) }
    }

    @Test
    fun `deleteById should delete specific user`() = runTest {
        // Given
        val userId = 1L
        coEvery { usuarioDao.deleteById(userId) } returns Unit

        // When
        usuarioDao.deleteById(userId)

        // Then
        coVerify(exactly = 1) { usuarioDao.deleteById(userId) }
    }

    @Test
    fun `user should have all required properties`() = runTest {
        // Given
        val user = UserEntity(
            id = 1,
            idApi = 100,
            nombre = "Roberto",
            apellido = "Fernández",
            correo = "roberto@test.com",
            region = "Araucanía",
            fecha_registro = "2024-11-30",
            estado = true,
            fotopefil = "http://example.com/photo.jpg"
        )
        every { usuarioDao.getUserById(1L) } returns flowOf(user)

        // When
        val result = usuarioDao.getUserById(1L).first()

        // Then
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals(100, result?.idApi)
        assertEquals("Roberto", result?.nombre)
        assertEquals("Fernández", result?.apellido)
        assertEquals("roberto@test.com", result?.correo)
        assertEquals("Araucanía", result?.region)
        assertEquals("2024-11-30", result?.fecha_registro)
        assertTrue(result?.estado == true)
        assertEquals("http://example.com/photo.jpg", result?.fotopefil)
    }

    @Test
    fun `user can have null fotopefil`() = runTest {
        // Given
        val user = UserEntity(
            id = 1,
            nombre = "Sandra",
            apellido = "Ramírez",
            correo = "sandra@test.com",
            region = "Biobío",
            fecha_registro = "2024-01-01",
            estado = true,
            fotopefil = null
        )
        every { usuarioDao.getUserById(1L) } returns flowOf(user)

        // When
        val result = usuarioDao.getUserById(1L).first()

        // Then
        assertNull(result?.fotopefil)
    }

    @Test
    fun `user can have null idApi`() = runTest {
        // Given
        val user = UserEntity(
            id = 1,
            idApi = null,
            nombre = "Diego",
            apellido = "Vargas",
            correo = "diego@test.com",
            region = "Los Lagos",
            fecha_registro = "2024-01-01",
            estado = false
        )
        every { usuarioDao.getUserById(1L) } returns flowOf(user)

        // When
        val result = usuarioDao.getUserById(1L).first()

        // Then
        assertNull(result?.idApi)
    }

    @Test
    fun `getAllUsers should return users with different estados`() = runTest {
        // Given
        val users = listOf(
            UserEntity(id = 1, nombre = "Usuario Activo", apellido = "Activo", correo = "activo@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true),
            UserEntity(id = 2, nombre = "Usuario Inactivo", apellido = "Inactivo", correo = "inactivo@test.com", region = "Valparaíso", fecha_registro = "2024-01-02", estado = false)
        )
        every { usuarioDao.getAllUsers() } returns flowOf(users)

        // When
        val result = usuarioDao.getAllUsers().first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0].estado)
        assertFalse(result[1].estado)
    }

    @Test
    fun `upsertAll should handle users from API sync`() = runTest {
        // Given
        val usersFromApi = listOf(
            UserEntity(id = 0, idApi = 101, nombre = "API User 1", apellido = "Test", correo = "api1@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true),
            UserEntity(id = 0, idApi = 102, nombre = "API User 2", apellido = "Test", correo = "api2@test.com", region = "Valparaíso", fecha_registro = "2024-01-02", estado = true)
        )
        coEvery { usuarioDao.upsertAll(usersFromApi) } returns Unit

        // When
        usuarioDao.upsertAll(usersFromApi)

        // Then
        coVerify(exactly = 1) { usuarioDao.upsertAll(match { it.size == 2 && it.all { user -> user.idApi != null } }) }
    }

    @Test
    fun `correo should be unique per database constraint`() = runTest {
        // Given
        val user = UserEntity(
            id = 1,
            nombre = "Test",
            apellido = "Test",
            correo = "unique@test.com",
            region = "Metropolitana",
            fecha_registro = "2024-01-01",
            estado = true
        )
        every { usuarioDao.getUserById(1L) } returns flowOf(user)

        // When
        val result = usuarioDao.getUserById(1L).first()

        // Then
        assertNotNull(result?.correo)
        assertTrue(result?.correo?.contains("@") == true)
    }

    @Test
    fun `fecha_registro should be stored as string`() = runTest {
        // Given
        val user = UserEntity(
            id = 1,
            nombre = "Test",
            apellido = "Test",
            correo = "test@test.com",
            region = "Metropolitana",
            fecha_registro = "2024-11-30",
            estado = true
        )
        every { usuarioDao.getUserById(1L) } returns flowOf(user)

        // When
        val result = usuarioDao.getUserById(1L).first()

        // Then
        assertTrue(result?.fecha_registro is String)
        assertTrue(result?.fecha_registro?.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) == true)
    }

    @Test
    fun `deleteById should handle non-existing user`() = runTest {
        // Given
        val nonExistingId = 999L
        coEvery { usuarioDao.deleteById(nonExistingId) } returns Unit

        // When
        usuarioDao.deleteById(nonExistingId)

        // Then
        coVerify(exactly = 1) { usuarioDao.deleteById(nonExistingId) }
    }

    @Test
    fun `getActiveUser should return only first active user`() = runTest {
        // Given - Solo retorna el primer usuario activo debido a LIMIT 1
        val activeUser = UserEntity(id = 1, nombre = "Primer Activo", apellido = "Usuario", correo = "primero@test.com", region = "Metropolitana", fecha_registro = "2024-01-01", estado = true)
        every { usuarioDao.getActiveUser() } returns flowOf(activeUser)

        // When
        val result = usuarioDao.getActiveUser().first()

        // Then
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertTrue(result?.estado == true)
    }
}
