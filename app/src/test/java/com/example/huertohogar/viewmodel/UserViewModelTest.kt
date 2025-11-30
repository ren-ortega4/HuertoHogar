package com.example.huertohogar.viewmodel

import android.net.Uri
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.model.User
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

/**
 * Pruebas unitarias para UserViewModel
 * 
 * Cubre:
 * - Login y autenticación
 * - Validaciones de formularios
 * - Registro de usuarios
 * - Gestión de estado
 * - Actualización de perfil
 */
@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("UserViewModel Tests")
class UserViewModelTest {

    private lateinit var mockRepository: UsuarioRepository
    private lateinit var viewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk(relaxed = true)
        viewModel = UserViewModel(mockRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // ==================== PRUEBAS DE LOGIN ====================

    @Nested
    @DisplayName("Login Tests")
    inner class LoginTests {

        @Test
        fun `login con credenciales válidas retorna true y actualiza estado`() = runTest {
            // Given
            val mockUser = User(
                id = 1,
                nombre = "Test User",
                correo = "test@example.com",
                clave = "password123",
                direccion = "Test Address",
                region = "Metropolitana de Santiago",
                confirmarClave = "password123",
                aceptaTerminos = true,
                fotopefil = null
            )

            coEvery { mockRepository.login("test@example.com", "password123") } returns mockUser

            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("password123")

            // When
            val result = viewModel.login()
            advanceUntilIdle()

            // Then
            assertTrue(result, "Login debería retornar true con credenciales válidas")

            val estado = viewModel.estado.first()
            assertTrue(estado.isLoggedIn, "Usuario debería estar logueado")
            assertEquals(mockUser, estado.currentUser, "CurrentUser debería ser el usuario mockeado")
            assertEquals("", estado.loginCorreo, "LoginCorreo debería limpiarse")
            assertEquals("", estado.loginClave, "LoginClave debería limpiarse")
            assertNull(estado.errores.errorLoginClave, "No debería haber errores")

            coVerify(exactly = 1) { mockRepository.login("test@example.com", "password123") }
        }

        @Test
        fun `login con credenciales no validas retorna false y muestra error`() = runTest {
            // Given
            coEvery { mockRepository.login(any(), any()) } returns null

            viewModel.onLoginCorreoChange("wrong@example.com")
            viewModel.onLoginClaveChange("wrongpassword")

            // When
            val result = viewModel.login()
            advanceUntilIdle()

            // Then
            assertFalse(result, "Login debería retornar false con credenciales inválidas")

            val estado = viewModel.estado.first()
            assertFalse(estado.isLoggedIn, "Usuario no debería estar logueado")
            assertNull(estado.currentUser, "CurrentUser debería ser null")
            assertEquals("Correo o clave incorrectos", estado.errores.errorLoginClave)
        }

        @Test
        fun `login con correo vacío retorna false sin llamar al repositorio`() = runTest {
            // Given
            viewModel.onLoginCorreoChange("")
            viewModel.onLoginClaveChange("password123")

            // When
            val result = viewModel.login()
            advanceUntilIdle()

            // Then
            assertFalse(result, "Login debería retornar false con correo vacío")

            val estado = viewModel.estado.first()
            assertEquals("El correo es requerido", estado.errores.errorLoginCorreo)

            coVerify(exactly = 0) { mockRepository.login(any(), any()) }
        }

        @Test
        fun `login con clave vacía retorna false sin llamar al repositorio`() = runTest {
            // Given
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("")

            // When
            val result = viewModel.login()
            advanceUntilIdle()

            // Then
            assertFalse(result)

            val estado = viewModel.estado.first()
            assertEquals("La clave es requerida", estado.errores.errorLoginClave)

            coVerify(exactly = 0) { mockRepository.login(any(), any()) }
        }

        @Test
        fun `login con clave menor a 8 caracteres retorna false`() = runTest {
            // Given
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("pass")

            // When
            val result = viewModel.login()
            advanceUntilIdle()

            // Then
            assertFalse(result)

            val estado = viewModel.estado.first()
            assertEquals("Debe tener al menos 8 caracteres", estado.errores.errorLoginClave)

            coVerify(exactly = 0) { mockRepository.login(any(), any()) }
        }

        @Test
        fun `login con correo sin arroba retorna false`() = runTest {
            // Given
            viewModel.onLoginCorreoChange("invalidemail.com")
            viewModel.onLoginClaveChange("password123")

            // When
            val result = viewModel.login()
            advanceUntilIdle()

            // Then
            assertFalse(result)

            val estado = viewModel.estado.first()
            assertEquals("@ en el correo es requerida", estado.errores.errorLoginCorreo)
        }
    }

    // ==================== PRUEBAS DE VALIDACIÓN DE REGISTRO ====================

    @Nested
    @DisplayName("Validación de Registro Tests")
    inner class ValidacionRegistroTests {

        @Test
        fun `validarFormularioRegistro con datos válidos retorna true`() {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password123")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("Metropolitana de Santiago")
            viewModel.onAceptarTerminosChange(true)

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertTrue(resultado, "Validación debería pasar con datos válidos")

            val estado = viewModel.estado.value
            assertNull(estado.errores.nombre)
            assertNull(estado.errores.correo)
            assertNull(estado.errores.clave)
            assertNull(estado.errores.confirmarClave)
            assertNull(estado.errores.direccion)
            assertNull(estado.errores.region)
        }

        @Test
        fun `validarFormularioRegistro con nombre vacío retorna false`() {
            // Given
            viewModel.onNombreChange("")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password123")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("Metropolitana de Santiago")

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertFalse(resultado)
            assertEquals("El nombre es requerido", viewModel.estado.value.errores.nombre)
        }

        @Test
        fun `validarFormularioRegistro con correo sin arroba retorna false`() {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juanexample.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password123")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("Metropolitana de Santiago")

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertFalse(resultado)
            assertEquals("El correo debe tener '@' ", viewModel.estado.value.errores.correo)
        }

        @Test
        fun `validarFormularioRegistro con clave menor a 8 caracteres retorna false`() {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("pass")
            viewModel.onConfirmarClaveChange("pass")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("Metropolitana de Santiago")

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertFalse(resultado)
            assertEquals("La clave debe tener al menos 8 caracteres", viewModel.estado.value.errores.clave)
        }

        @Test
        fun `validarFormularioRegistro con claves que no coinciden retorna false`() {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password456")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("Metropolitana de Santiago")

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertFalse(resultado)
            assertEquals("Las claves no coinciden", viewModel.estado.value.errores.confirmarClave)
        }

        @Test
        fun `validarFormularioRegistro con dirección vacía retorna false`() {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password123")
            viewModel.onDireccionChange("")
            viewModel.onRegionChange("Metropolitana de Santiago")

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertFalse(resultado)
            assertEquals("La direccion es requerida", viewModel.estado.value.errores.direccion)
        }

        @Test
        fun `validarFormularioRegistro con región vacía retorna false`() {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password123")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("")

            // When
            val resultado = viewModel.validarFormularioRegistro()

            // Then
            assertFalse(resultado)
            assertEquals("la region es requerida", viewModel.estado.value.errores.region)
        }
    }

    // ==================== PRUEBAS DE REGISTRO DE USUARIO ====================

    @Nested
    @DisplayName("Guardar Usuario Tests")
    inner class GuardarUsuarioTests {

        @Test
        fun `guardarUsuario con datos válidos llama al repositorio y limpia formulario`() = runTest {
            // Given
            viewModel.onNombreChange("Juan Pérez")
            viewModel.onCorreoChange("juan@example.com")
            viewModel.onClaveChange("password123")
            viewModel.onConfirmarClaveChange("password123")
            viewModel.onDireccionChange("Calle Falsa 123")
            viewModel.onRegionChange("Metropolitana de Santiago")
            viewModel.onAceptarTerminosChange(true)

            // When
            viewModel.guardarUsuario()
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) {
                mockRepository.insertar(match { user ->
                    user.nombre == "Juan Pérez" &&
                    user.correo == "juan@example.com" &&
                    user.clave == "password123" &&
                    user.direccion == "Calle Falsa 123" &&
                    user.region == "Metropolitana de Santiago" &&
                    user.aceptaTerminos
                })
            }

            // Verificar que el formulario se limpió
            val estado = viewModel.estado.first()
            assertEquals("", estado.nombre)
            assertEquals("", estado.correo)
            assertEquals("", estado.clave)
            assertEquals("", estado.confirmarClave)
            assertEquals("", estado.direccion)
            assertEquals("", estado.region)
            assertFalse(estado.aceptaTerminos)
        }

        @Test
        fun `guardarUsuario con datos no validos no llama al repositorio`() = runTest {
            // Given - datos inválidos
            viewModel.onNombreChange("")
            viewModel.onCorreoChange("invalid")
            viewModel.onClaveChange("short")

            // When
            viewModel.guardarUsuario()
            advanceUntilIdle()

            // Then
            coVerify(exactly = 0) { mockRepository.insertar(any()) }
        }
    }

    // ==================== PRUEBAS DE GESTIÓN DE ESTADO ====================

    @Nested
    @DisplayName("Gestión de Estado Tests")
    inner class GestionEstadoTests {

        @Test
        fun `onNombreChange actualiza el nombre en el estado`() {
            // When
            viewModel.onNombreChange("Juan")

            // Then
            assertEquals("Juan", viewModel.estado.value.nombre)
        }

        @Test
        fun `onCorreoChange actualiza el correo en el estado`() {
            // When
            viewModel.onCorreoChange("juan@example.com")

            // Then
            assertEquals("juan@example.com", viewModel.estado.value.correo)
        }

        @Test
        fun `onClaveChange actualiza la clave en el estado`() {
            // When
            viewModel.onClaveChange("password123")

            // Then
            assertEquals("password123", viewModel.estado.value.clave)
        }

        @Test
        fun `onConfirmarClaveChange actualiza confirmarClave en el estado`() {
            // When
            viewModel.onConfirmarClaveChange("password123")

            // Then
            assertEquals("password123", viewModel.estado.value.confirmarClave)
        }

        @Test
        fun `onDireccionChange actualiza la dirección en el estado`() {
            // When
            viewModel.onDireccionChange("Calle Falsa 123")

            // Then
            assertEquals("Calle Falsa 123", viewModel.estado.value.direccion)
        }

        @Test
        fun `onRegionChange actualiza la región en el estado`() {
            // When
            viewModel.onRegionChange("Metropolitana de Santiago")

            // Then
            assertEquals("Metropolitana de Santiago", viewModel.estado.value.region)
        }

        @Test
        fun `onAceptarTerminosChange actualiza aceptaTerminos en el estado`() {
            // When
            viewModel.onAceptarTerminosChange(true)

            // Then
            assertTrue(viewModel.estado.value.aceptaTerminos)
        }

        @Test
        fun `onLoginCorreoChange actualiza loginCorreo en el estado`() {
            // When
            viewModel.onLoginCorreoChange("test@example.com")

            // Then
            assertEquals("test@example.com", viewModel.estado.value.loginCorreo)
        }

        @Test
        fun `onLoginClaveChange actualiza loginClave en el estado`() {
            // When
            viewModel.onLoginClaveChange("password123")

            // Then
            assertEquals("password123", viewModel.estado.value.loginClave)
        }
    }

    // ==================== PRUEBAS DE LIMPIEZA Y LOGOUT ====================

    @Nested
    @DisplayName("Limpieza y Logout Tests")
    inner class LimpiezaLogoutTests {

        @Test
        fun `limpiarFormulario limpia campos de registro manteniendo sesión`() = runTest {
            // Given - Usuario logueado con datos en formularios
            val mockUser = User(
                id = 1,
                nombre = "Test User",
                correo = "test@example.com",
                clave = "password123",
                direccion = "Address",
                region = "Region",
                confirmarClave = "password123",
                aceptaTerminos = true,
                fotopefil = null
            )

            coEvery { mockRepository.login(any(), any()) } returns mockUser
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("password123")
            viewModel.login()
            advanceUntilIdle()

            // Llenar formularios
            viewModel.onNombreChange("Otro nombre")
            viewModel.onCorreoChange("otro@example.com")
            viewModel.onClaveChange("otrapass")

            // When
            viewModel.limpiarFormulario()

            // Then
            val estado = viewModel.estado.first()
            assertEquals("", estado.nombre)
            assertEquals("", estado.correo)
            assertEquals("", estado.clave)
            assertEquals("", estado.confirmarClave)
            assertEquals("", estado.direccion)
            assertEquals("", estado.region)
            assertEquals("", estado.loginCorreo)
            assertEquals("", estado.loginClave)
            assertFalse(estado.aceptaTerminos)

            // Pero mantiene la sesión
            assertTrue(estado.isLoggedIn)
            assertEquals(mockUser, estado.currentUser)
        }

        @Test
        fun `logout resetea todo el estado excepto recordarUsuario`() = runTest {
            // Given - Usuario logueado
            val mockUser = User(
                id = 1,
                nombre = "Test User",
                correo = "test@example.com",
                clave = "password123",
                direccion = "Address",
                region = "Region",
                confirmarClave = "password123",
                aceptaTerminos = true,
                fotopefil = null
            )

            coEvery { mockRepository.login(any(), any()) } returns mockUser
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("password123")
            viewModel.login()
            advanceUntilIdle()

            // When
            viewModel.logout()

            // Then
            val estado = viewModel.estado.value
            assertFalse(estado.isLoggedIn)
            assertNull(estado.currentUser)
            assertEquals("", estado.nombre)
            assertEquals("", estado.correo)
            assertEquals("", estado.loginCorreo)
            assertEquals("", estado.loginClave)
        }
    }

    // ==================== PRUEBAS DE ACTUALIZACIÓN DE FOTO ====================

    @Nested
    @DisplayName("Actualización de Foto Tests")
    inner class ActualizacionFotoTests {

        @Test
        fun `actualizarFotoPerfil con usuario logueado actualiza la foto`() = runTest {
            // Given - Usuario logueado
            val mockUser = User(
                id = 1,
                nombre = "Test User",
                correo = "test@example.com",
                clave = "password123",
                direccion = "Address",
                region = "Region",
                fotopefil = null,
                confirmarClave ="password123",
                aceptaTerminos = true
            )

            coEvery { mockRepository.login(any(), any()) } returns mockUser
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("password123")
            viewModel.login()
            advanceUntilIdle()

            val mockUri = mockk<Uri>()
            every { mockUri.toString() } returns "content://test/image.jpg"

            // When
            viewModel.actualizarFotoPerfil(mockUri)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) {
                mockRepository.actualizarFoto(1, "content://test/image.jpg")
            }

            val estado = viewModel.estado.first()
            assertEquals("content://test/image.jpg", estado.currentUser?.fotopefil)
        }

        @Test
        fun `actualizarFotoPerfil sin usuario logueado no hace nada`() = runTest {
            // Given - No hay usuario logueado
            val mockUri = mockk<Uri>()
            every { mockUri.toString() } returns "content://test/image.jpg"

            // When
            viewModel.actualizarFotoPerfil(mockUri)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 0) { mockRepository.actualizarFoto(any(), any()) }
        }

        @Test
        fun `actualizarFotoPerfil con URI null actualiza a null`() = runTest {
            // Given - Usuario logueado
            val mockUser = User(
                id = 1,
                nombre = "Test User",
                correo = "test@example.com",
                clave = "password123",
                direccion = "Address",
                region = "Region",
                fotopefil = "old_photo.jpg",
                confirmarClave = "password",
                aceptaTerminos = true
            )
            
            coEvery { mockRepository.login(any(), any()) } returns mockUser
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("password123")
            viewModel.login()
            advanceUntilIdle()

            // When
            viewModel.actualizarFotoPerfil(null)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { mockRepository.actualizarFoto(1, null) }
            
            val estado = viewModel.estado.first()
            assertNull(estado.currentUser?.fotopefil)
        }
    }

    // ==================== PRUEBAS DE VALIDACIÓN DE LOGIN ====================

    @Nested
    @DisplayName("Validación de Login Tests")
    inner class ValidacionLoginTests {

        @Test
        fun `validarLogin con datos válidos retorna true`() {
            // Given
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("password123")

            // When
            val resultado = viewModel.validarLogin()

            // Then
            assertTrue(resultado)
            val estado = viewModel.estado.value
            assertNull(estado.errores.errorLoginCorreo)
            assertNull(estado.errores.errorLoginClave)
        }

        @Test
        fun `validarLogin con correo vacío retorna false`() {
            // Given
            viewModel.onLoginCorreoChange("")
            viewModel.onLoginClaveChange("password123")

            // When
            val resultado = viewModel.validarLogin()

            // Then
            assertFalse(resultado)
            assertEquals("El correo es requerido", viewModel.estado.value.errores.errorLoginCorreo)
        }

        @Test
        fun `validarLogin con clave vacía retorna false`() {
            // Given
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("")

            // When
            val resultado = viewModel.validarLogin()

            // Then
            assertFalse(resultado)
            assertEquals("La clave es requerida", viewModel.estado.value.errores.errorLoginClave)
        }

        @Test
        fun `validarLogin con clave corta retorna false`() {
            // Given
            viewModel.onLoginCorreoChange("test@example.com")
            viewModel.onLoginClaveChange("pass")

            // When
            val resultado = viewModel.validarLogin()

            // Then
            assertFalse(resultado)
            assertEquals("Debe tener al menos 8 caracteres", viewModel.estado.value.errores.errorLoginClave)
        }
    }

    // ==================== PRUEBAS DE DATOS DE REGIONES ====================

    @Test
    fun `regiones contiene todas las regiones de Chile`() {
        // Then
        assertEquals(16, viewModel.regiones.size)
        assertTrue(viewModel.regiones.contains("Metropolitana de Santiago"))
        assertTrue(viewModel.regiones.contains("Valparaíso"))
        assertTrue(viewModel.regiones.contains("Biobío"))
    }
}
