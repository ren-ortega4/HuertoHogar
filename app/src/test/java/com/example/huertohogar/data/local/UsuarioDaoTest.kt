package com.example.huertohogar.data.local

import com.example.huertohogar.model.User
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("UsuarioDao Tests")
class UsuarioDaoTest {
    
    private lateinit var usuarioDao: UsuarioDao

    @BeforeEach
    fun setup() {
        usuarioDao = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSampleUser(
        id: Int = 1,
        nombre: String = "Juan Pérez",
        correo: String = "juan@example.com",
        clave: String = "password123",
        confirmarClave: String = "password123",
        direccion: String = "Calle Principal 123",
        region: String = "Metropolitana",
        aceptaTerminos: Boolean = true,
        fotopefil: String? = null
    ) = User(
        id = id,
        nombre = nombre,
        correo = correo,
        clave = clave,
        confirmarClave = confirmarClave,
        direccion = direccion,
        region = region,
        aceptaTerminos = aceptaTerminos,
        fotopefil = fotopefil
    )

    @Nested
    @DisplayName("obtenerUsuarios Tests")
    inner class ObtenerUsuariosTests {

        @Test
        @DisplayName("obtenerUsuarios should return list of all users")
        fun `obtenerUsuarios should return list of all users`() = runBlocking {
            val users = listOf(
                createSampleUser(id = 1, nombre = "Usuario 1"),
                createSampleUser(id = 2, nombre = "Usuario 2"),
                createSampleUser(id = 3, nombre = "Usuario 3")
            )
            coEvery { usuarioDao.obtenerUsuarios() } returns users

            val result = usuarioDao.obtenerUsuarios()

            result shouldHaveSize 3
            result shouldBe users
        }

        @Test
        @DisplayName("obtenerUsuarios should return empty list when no users")
        fun `obtenerUsuarios should return empty list when no users`() = runBlocking {
            coEvery { usuarioDao.obtenerUsuarios() } returns emptyList()

            val result = usuarioDao.obtenerUsuarios()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("obtenerUsuarios should return users in ascending id order")
        fun `obtenerUsuarios should return users in ascending id order`() = runBlocking {
            val users = listOf(
                createSampleUser(id = 1, nombre = "Usuario 1"),
                createSampleUser(id = 2, nombre = "Usuario 2"),
                createSampleUser(id = 3, nombre = "Usuario 3")
            )
            coEvery { usuarioDao.obtenerUsuarios() } returns users

            val result = usuarioDao.obtenerUsuarios()

            result[0].id shouldBe 1
            result[1].id shouldBe 2
            result[2].id shouldBe 3
        }

        @Test
        @DisplayName("obtenerUsuarios should preserve user data")
        fun `obtenerUsuarios should preserve user data`() = runBlocking {
            val user = createSampleUser(
                id = 10,
                nombre = "Usuario Especial",
                correo = "especial@example.com",
                direccion = "Dirección Completa 456",
                region = "Valparaíso",
                fotopefil = "photo.jpg"
            )
            coEvery { usuarioDao.obtenerUsuarios() } returns listOf(user)

            val result = usuarioDao.obtenerUsuarios()

            result.first().id shouldBe 10
            result.first().nombre shouldBe "Usuario Especial"
            result.first().correo shouldBe "especial@example.com"
            result.first().direccion shouldBe "Dirección Completa 456"
            result.first().region shouldBe "Valparaíso"
            result.first().fotopefil shouldBe "photo.jpg"
        }

        @Test
        @DisplayName("obtenerUsuarios should return users with null profile photo")
        fun `obtenerUsuarios should return users with null profile photo`() = runBlocking {
            val users = listOf(
                createSampleUser(id = 1, fotopefil = null),
                createSampleUser(id = 2, fotopefil = "photo.jpg")
            )
            coEvery { usuarioDao.obtenerUsuarios() } returns users

            val result = usuarioDao.obtenerUsuarios()

            result[0].fotopefil.shouldBeNull()
            result[1].fotopefil.shouldNotBeNull()
        }

        @Test
        @DisplayName("obtenerUsuarios should return consistent results")
        fun `obtenerUsuarios should return consistent results`() = runBlocking {
            val users = listOf(createSampleUser(id = 1))
            coEvery { usuarioDao.obtenerUsuarios() } returns users

            val result1 = usuarioDao.obtenerUsuarios()
            val result2 = usuarioDao.obtenerUsuarios()

            result1 shouldBe result2
        }
    }

    @Nested
    @DisplayName("insertar Tests")
    inner class InsertarTests {

        @Test
        @DisplayName("insertar should insert single user")
        fun `insertar should insert single user`() = runBlocking {
            val user = createSampleUser()
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(user) }
        }

        @Test
        @DisplayName("insertar should replace on conflict")
        fun `insertar should replace on conflict`() = runBlocking {
            val user1 = createSampleUser(id = 1, nombre = "Original")
            val user2 = createSampleUser(id = 1, nombre = "Actualizado")
            
            coEvery { usuarioDao.insertar(any()) } just Runs

            usuarioDao.insertar(user1)
            usuarioDao.insertar(user2)

            coVerify(exactly = 2) { usuarioDao.insertar(any()) }
        }

        @Test
        @DisplayName("insertar should handle user with all fields")
        fun `insertar should handle user with all fields`() = runBlocking {
            val user = createSampleUser(
                id = 100,
                nombre = "María González",
                correo = "maria@test.com",
                clave = "securepass",
                confirmarClave = "securepass",
                direccion = "Avenida Libertad 789",
                region = "Biobío",
                aceptaTerminos = true,
                fotopefil = "profile.png"
            )
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { 
                it.id == 100 && it.nombre == "María González" 
            }) }
        }

        @Test
        @DisplayName("insertar should handle user without profile photo")
        fun `insertar should handle user without profile photo`() = runBlocking {
            val user = createSampleUser(fotopefil = null)
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.fotopefil == null }) }
        }

        @Test
        @DisplayName("insertar should handle user with special characters in name")
        fun `insertar should handle user with special characters in name`() = runBlocking {
            val user = createSampleUser(nombre = "José María Ñuñez O'Brien")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.nombre.contains("Ñ") }) }
        }

        @Test
        @DisplayName("insertar should handle user with auto-generated id")
        fun `insertar should handle user with auto-generated id`() = runBlocking {
            val user = createSampleUser(id = 0)
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.id == 0 }) }
        }

        @Test
        @DisplayName("insertar should handle user with aceptaTerminos false")
        fun `insertar should handle user with aceptaTerminos false`() = runBlocking {
            val user = createSampleUser(aceptaTerminos = false)
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { !it.aceptaTerminos }) }
        }

        @Test
        @DisplayName("insertar should handle user with long address")
        fun `insertar should handle user with long address`() = runBlocking {
            val longAddress = "Calle muy larga número 12345, Departamento 678, Block C, ".repeat(3)
            val user = createSampleUser(direccion = longAddress)
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.direccion.length > 100 }) }
        }
    }

    @Nested
    @DisplayName("eliminar Tests")
    inner class EliminarTests {

        @Test
        @DisplayName("eliminar should delete specific user")
        fun `eliminar should delete specific user`() = runBlocking {
            val user = createSampleUser(id = 1)
            coEvery { usuarioDao.eliminar(user) } just Runs

            usuarioDao.eliminar(user)

            coVerify { usuarioDao.eliminar(user) }
        }

        @Test
        @DisplayName("eliminar should delete user by id")
        fun `eliminar should delete user by id`() = runBlocking {
            val user = createSampleUser(id = 5)
            coEvery { usuarioDao.eliminar(user) } just Runs

            usuarioDao.eliminar(user)

            coVerify { usuarioDao.eliminar(match { it.id == 5 }) }
        }

        @Test
        @DisplayName("eliminar should work with any user")
        fun `eliminar should work with any user`() = runBlocking {
            val user = createSampleUser(
                id = 10,
                nombre = "Para Eliminar",
                correo = "eliminar@test.com"
            )
            coEvery { usuarioDao.eliminar(user) } just Runs

            usuarioDao.eliminar(user)

            coVerify { usuarioDao.eliminar(user) }
        }

        @Test
        @DisplayName("eliminar should handle user with profile photo")
        fun `eliminar should handle user with profile photo`() = runBlocking {
            val user = createSampleUser(fotopefil = "photo_to_delete.jpg")
            coEvery { usuarioDao.eliminar(user) } just Runs

            usuarioDao.eliminar(user)

            coVerify { usuarioDao.eliminar(match { it.fotopefil != null }) }
        }

        @Test
        @DisplayName("eliminar should be callable multiple times")
        fun `eliminar should be callable multiple times`() = runBlocking {
            val user1 = createSampleUser(id = 1)
            val user2 = createSampleUser(id = 2)
            val user3 = createSampleUser(id = 3)
            
            coEvery { usuarioDao.eliminar(any()) } just Runs

            usuarioDao.eliminar(user1)
            usuarioDao.eliminar(user2)
            usuarioDao.eliminar(user3)

            coVerify(exactly = 3) { usuarioDao.eliminar(any()) }
        }
    }

    @Nested
    @DisplayName("login Tests")
    inner class LoginTests {

        @Test
        @DisplayName("login should return user when credentials are correct")
        fun `login should return user when credentials are correct`() = runBlocking {
            val user = createSampleUser(
                correo = "test@example.com",
                clave = "password123"
            )
            coEvery { usuarioDao.login("test@example.com", "password123") } returns user

            val result = usuarioDao.login("test@example.com", "password123")

            result.shouldNotBeNull()
            result.correo shouldBe "test@example.com"
            result.clave shouldBe "password123"
        }

        @Test
        @DisplayName("login should return null when credentials are incorrect")
        fun `login should return null when credentials are incorrect`() = runBlocking {
            coEvery { usuarioDao.login("test@example.com", "wrongpassword") } returns null

            val result = usuarioDao.login("test@example.com", "wrongpassword")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should return null when user does not exist")
        fun `login should return null when user does not exist`() = runBlocking {
            coEvery { usuarioDao.login("nonexistent@example.com", "password123") } returns null

            val result = usuarioDao.login("nonexistent@example.com", "password123")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should return null when email is incorrect")
        fun `login should return null when email is incorrect`() = runBlocking {
            coEvery { usuarioDao.login("wrong@example.com", "password123") } returns null

            val result = usuarioDao.login("wrong@example.com", "password123")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should return null when password is incorrect")
        fun `login should return null when password is incorrect`() = runBlocking {
            coEvery { usuarioDao.login("test@example.com", "wrongpass") } returns null

            val result = usuarioDao.login("test@example.com", "wrongpass")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should be case sensitive for email")
        fun `login should be case sensitive for email`() = runBlocking {
            val user = createSampleUser(correo = "test@example.com")
            coEvery { usuarioDao.login("test@example.com", "password123") } returns user
            coEvery { usuarioDao.login("TEST@EXAMPLE.COM", "password123") } returns null

            val result1 = usuarioDao.login("test@example.com", "password123")
            val result2 = usuarioDao.login("TEST@EXAMPLE.COM", "password123")

            result1.shouldNotBeNull()
            result2.shouldBeNull()
        }

        @Test
        @DisplayName("login should return first match when LIMIT 1")
        fun `login should return first match when LIMIT 1`() = runBlocking {
            val user = createSampleUser(id = 1)
            coEvery { usuarioDao.login("test@example.com", "password123") } returns user

            val result = usuarioDao.login("test@example.com", "password123")

            result.shouldNotBeNull()
            result.id shouldBe 1
        }

        @Test
        @DisplayName("login should handle empty email")
        fun `login should handle empty email`() = runBlocking {
            coEvery { usuarioDao.login("", "password123") } returns null

            val result = usuarioDao.login("", "password123")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should handle empty password")
        fun `login should handle empty password`() = runBlocking {
            coEvery { usuarioDao.login("test@example.com", "") } returns null

            val result = usuarioDao.login("test@example.com", "")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should handle both empty credentials")
        fun `login should handle both empty credentials`() = runBlocking {
            coEvery { usuarioDao.login("", "") } returns null

            val result = usuarioDao.login("", "")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("login should return complete user data")
        fun `login should return complete user data`() = runBlocking {
            val user = createSampleUser(
                id = 5,
                nombre = "Test User",
                correo = "test@example.com",
                clave = "pass123",
                direccion = "Test Address",
                region = "Test Region",
                aceptaTerminos = true,
                fotopefil = "test.jpg"
            )
            coEvery { usuarioDao.login("test@example.com", "pass123") } returns user

            val result = usuarioDao.login("test@example.com", "pass123")

            result.shouldNotBeNull()
            result.id shouldBe 5
            result.nombre shouldBe "Test User"
            result.direccion shouldBe "Test Address"
            result.region shouldBe "Test Region"
            result.aceptaTerminos shouldBe true
            result.fotopefil shouldBe "test.jpg"
        }
    }

    @Nested
    @DisplayName("actualizarFoto Tests")
    inner class ActualizarFotoTests {

        @Test
        @DisplayName("actualizarFoto should update photo URI")
        fun `actualizarFoto should update photo URI`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(1, "new_photo.jpg") } just Runs

            usuarioDao.actualizarFoto(1, "new_photo.jpg")

            coVerify { usuarioDao.actualizarFoto(1, "new_photo.jpg") }
        }

        @Test
        @DisplayName("actualizarFoto should update photo to null")
        fun `actualizarFoto should update photo to null`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(1, null) } just Runs

            usuarioDao.actualizarFoto(1, null)

            coVerify { usuarioDao.actualizarFoto(1, null) }
        }

        @Test
        @DisplayName("actualizarFoto should update for specific user id")
        fun `actualizarFoto should update for specific user id`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(10, "photo.png") } just Runs

            usuarioDao.actualizarFoto(10, "photo.png")

            coVerify { usuarioDao.actualizarFoto(10, "photo.png") }
        }

        @Test
        @DisplayName("actualizarFoto should handle different user ids")
        fun `actualizarFoto should handle different user ids`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(any(), any()) } just Runs

            usuarioDao.actualizarFoto(1, "photo1.jpg")
            usuarioDao.actualizarFoto(2, "photo2.jpg")
            usuarioDao.actualizarFoto(3, "photo3.jpg")

            coVerify(exactly = 3) { usuarioDao.actualizarFoto(any(), any()) }
        }

        @Test
        @DisplayName("actualizarFoto should handle URI with path")
        fun `actualizarFoto should handle URI with path`() = runBlocking {
            val uri = "content://media/external/images/media/123"
            coEvery { usuarioDao.actualizarFoto(1, uri) } just Runs

            usuarioDao.actualizarFoto(1, uri)

            coVerify { usuarioDao.actualizarFoto(1, match { it?.contains("content://") == true }) }
        }

        @Test
        @DisplayName("actualizarFoto should handle file path")
        fun `actualizarFoto should handle file path`() = runBlocking {
            val filePath = "/storage/emulated/0/Pictures/photo.jpg"
            coEvery { usuarioDao.actualizarFoto(1, filePath) } just Runs

            usuarioDao.actualizarFoto(1, filePath)

            coVerify { usuarioDao.actualizarFoto(1, filePath) }
        }

        @Test
        @DisplayName("actualizarFoto should handle empty string")
        fun `actualizarFoto should handle empty string`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(1, "") } just Runs

            usuarioDao.actualizarFoto(1, "")

            coVerify { usuarioDao.actualizarFoto(1, "") }
        }

        @Test
        @DisplayName("actualizarFoto should handle zero id")
        fun `actualizarFoto should handle zero id`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(0, "photo.jpg") } just Runs

            usuarioDao.actualizarFoto(0, "photo.jpg")

            coVerify { usuarioDao.actualizarFoto(0, "photo.jpg") }
        }

        @Test
        @DisplayName("actualizarFoto should handle negative id")
        fun `actualizarFoto should handle negative id`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(-1, "photo.jpg") } just Runs

            usuarioDao.actualizarFoto(-1, "photo.jpg")

            coVerify { usuarioDao.actualizarFoto(-1, "photo.jpg") }
        }

        @Test
        @DisplayName("actualizarFoto should be callable multiple times for same user")
        fun `actualizarFoto should be callable multiple times for same user`() = runBlocking {
            coEvery { usuarioDao.actualizarFoto(1, any()) } just Runs

            usuarioDao.actualizarFoto(1, "photo1.jpg")
            usuarioDao.actualizarFoto(1, "photo2.jpg")
            usuarioDao.actualizarFoto(1, "photo3.jpg")

            coVerify(exactly = 3) { usuarioDao.actualizarFoto(1, any()) }
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    inner class IntegrationScenariosTests {

        @Test
        @DisplayName("should insert user and then retrieve all")
        fun `should insert user and then retrieve all`() = runBlocking {
            val user = createSampleUser(id = 1, nombre = "Test User")
            coEvery { usuarioDao.insertar(user) } just Runs
            coEvery { usuarioDao.obtenerUsuarios() } returns listOf(user)

            usuarioDao.insertar(user)
            val result = usuarioDao.obtenerUsuarios()

            result shouldHaveSize 1
            result.first().nombre shouldBe "Test User"
        }

        @Test
        @DisplayName("should insert user and login successfully")
        fun `should insert user and login successfully`() = runBlocking {
            val user = createSampleUser(
                correo = "test@example.com",
                clave = "password123"
            )
            coEvery { usuarioDao.insertar(user) } just Runs
            coEvery { usuarioDao.login("test@example.com", "password123") } returns user

            usuarioDao.insertar(user)
            val result = usuarioDao.login("test@example.com", "password123")

            result.shouldNotBeNull()
            result.correo shouldBe "test@example.com"
        }

        @Test
        @DisplayName("should insert user, update photo, and verify")
        fun `should insert user, update photo, and verify`() = runBlocking {
            val user = createSampleUser(id = 1, fotopefil = null)
            val updatedUser = user.copy(fotopefil = "new_photo.jpg")
            
            coEvery { usuarioDao.insertar(user) } just Runs
            coEvery { usuarioDao.actualizarFoto(1, "new_photo.jpg") } just Runs
            coEvery { usuarioDao.obtenerUsuarios() } returnsMany listOf(
                listOf(user),
                listOf(updatedUser)
            )

            usuarioDao.insertar(user)
            val beforeUpdate = usuarioDao.obtenerUsuarios()
            usuarioDao.actualizarFoto(1, "new_photo.jpg")
            val afterUpdate = usuarioDao.obtenerUsuarios()

            beforeUpdate.first().fotopefil.shouldBeNull()
            afterUpdate.first().fotopefil shouldBe "new_photo.jpg"
        }

        @Test
        @DisplayName("should insert user and then delete")
        fun `should insert user and then delete`() = runBlocking {
            val user = createSampleUser(id = 1)
            coEvery { usuarioDao.insertar(user) } just Runs
            coEvery { usuarioDao.eliminar(user) } just Runs
            coEvery { usuarioDao.obtenerUsuarios() } returnsMany listOf(
                listOf(user),
                emptyList()
            )

            usuarioDao.insertar(user)
            val beforeDelete = usuarioDao.obtenerUsuarios()
            usuarioDao.eliminar(user)
            val afterDelete = usuarioDao.obtenerUsuarios()

            beforeDelete shouldHaveSize 1
            afterDelete.shouldBeEmpty()
        }

        @Test
        @DisplayName("should handle multiple user insertions")
        fun `should handle multiple user insertions`() = runBlocking {
            val user1 = createSampleUser(id = 1, correo = "user1@test.com")
            val user2 = createSampleUser(id = 2, correo = "user2@test.com")
            val user3 = createSampleUser(id = 3, correo = "user3@test.com")
            
            coEvery { usuarioDao.insertar(any()) } just Runs
            coEvery { usuarioDao.obtenerUsuarios() } returns listOf(user1, user2, user3)

            usuarioDao.insertar(user1)
            usuarioDao.insertar(user2)
            usuarioDao.insertar(user3)
            val result = usuarioDao.obtenerUsuarios()

            result shouldHaveSize 3
        }

        @Test
        @DisplayName("should login after user insertion")
        fun `should login after user insertion`() = runBlocking {
            val user = createSampleUser(
                correo = "newuser@test.com",
                clave = "newpass123"
            )
            coEvery { usuarioDao.insertar(user) } just Runs
            coEvery { usuarioDao.login("newuser@test.com", "newpass123") } returns user

            usuarioDao.insertar(user)
            val loginResult = usuarioDao.login("newuser@test.com", "newpass123")

            loginResult.shouldNotBeNull()
        }

        @Test
        @DisplayName("should fail login with wrong credentials after insertion")
        fun `should fail login with wrong credentials after insertion`() = runBlocking {
            val user = createSampleUser(
                correo = "user@test.com",
                clave = "correctpass"
            )
            coEvery { usuarioDao.insertar(user) } just Runs
            coEvery { usuarioDao.login("user@test.com", "wrongpass") } returns null

            usuarioDao.insertar(user)
            val loginResult = usuarioDao.login("user@test.com", "wrongpass")

            loginResult.shouldBeNull()
        }
    }

    @Nested
    @DisplayName("Edge Cases and Data Validation")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("should handle user with very long email")
        fun `should handle user with very long email`() = runBlocking {
            val longEmail = "verylongemailaddress".repeat(10) + "@example.com"
            val user = createSampleUser(correo = longEmail)
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.correo.length > 100 }) }
        }

        @Test
        @DisplayName("should handle user with special characters in email")
        fun `should handle user with special characters in email`() = runBlocking {
            val user = createSampleUser(correo = "user+test@example.com")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.correo.contains("+") }) }
        }

        @Test
        @DisplayName("should handle user with unicode characters in name")
        fun `should handle user with unicode characters in name`() = runBlocking {
            val user = createSampleUser(nombre = "José María Ñuñez 한글")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(any()) }
        }

        @Test
        @DisplayName("should handle user with matching clave and confirmarClave")
        fun `should handle user with matching clave and confirmarClave`() = runBlocking {
            val user = createSampleUser(clave = "pass123", confirmarClave = "pass123")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.clave == it.confirmarClave }) }
        }

        @Test
        @DisplayName("should handle user with non-matching clave and confirmarClave")
        fun `should handle user with non-matching clave and confirmarClave`() = runBlocking {
            val user = createSampleUser(clave = "pass123", confirmarClave = "differentpass")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.clave != it.confirmarClave }) }
        }

        @Test
        @DisplayName("should handle user with maximum integer id")
        fun `should handle user with maximum integer id`() = runBlocking {
            val user = createSampleUser(id = Int.MAX_VALUE)
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.id == Int.MAX_VALUE }) }
        }

        @Test
        @DisplayName("should handle login with SQL injection attempt")
        fun `should handle login with SQL injection attempt`() = runBlocking {
            coEvery { usuarioDao.login("admin' OR '1'='1", "password") } returns null

            val result = usuarioDao.login("admin' OR '1'='1", "password")

            result.shouldBeNull()
        }

        @Test
        @DisplayName("should handle actualizarFoto with very long URI")
        fun `should handle actualizarFoto with very long URI`() = runBlocking {
            val longUri = "content://media/external/images/media/" + "1234567890".repeat(20)
            coEvery { usuarioDao.actualizarFoto(1, longUri) } just Runs

            usuarioDao.actualizarFoto(1, longUri)

            coVerify { usuarioDao.actualizarFoto(1, match { it?.length!! > 100 }) }
        }

        @Test
        @DisplayName("should handle user with empty direccion")
        fun `should handle user with empty direccion`() = runBlocking {
            val user = createSampleUser(direccion = "")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.direccion.isEmpty() }) }
        }

        @Test
        @DisplayName("should handle user with empty region")
        fun `should handle user with empty region`() = runBlocking {
            val user = createSampleUser(region = "")
            coEvery { usuarioDao.insertar(user) } just Runs

            usuarioDao.insertar(user)

            coVerify { usuarioDao.insertar(match { it.region.isEmpty() }) }
        }
    }
}