package com.example.huertohogar.viewmodel

import com.example.huertohogar.model.User
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("UserUiState Tests")
class UserUiStateTest {

    @Test
    @DisplayName("Debería crearse con valores por defecto")
    fun `should create with default values`() {
        // When
        val state = UserUiState()

        // Then
        state.currentUser.shouldBeNull()
        state.isLoggedIn shouldBe false
        state.id shouldBe 0
        state.nombre shouldBe ""
        state.correo shouldBe ""
        state.fotopefil.shouldBeNull()
        state.clave shouldBe ""
        state.confirmarClave shouldBe ""
        state.direccion shouldBe ""
        state.region shouldBe ""
        state.aceptaTerminos shouldBe false
        state.loginCorreo shouldBe ""
        state.loginClave shouldBe ""
        state.recordarUsuario shouldBe false
        state.errores shouldBe UserError()
    }

    @Test
    @DisplayName("Debería almacenar información de usuario logueado")
    fun `should store logged in user information`() {
        // Given
        val user = User(
            id = 1,
            nombre = "Juan Pérez",
            correo = "juan@example.com",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle 123",
            region = "Metropolitana",
            aceptaTerminos = true,
            fotopefil = "foto.jpg"
        )

        // When
        val state = UserUiState(
            currentUser = user,
            isLoggedIn = true
        )

        // Then
        state.currentUser.shouldNotBeNull()
        state.currentUser?.nombre shouldBe "Juan Pérez"
        state.isLoggedIn shouldBe true
    }

    @Test
    @DisplayName("Debería almacenar datos de registro correctamente")
    fun `should store registration data correctly`() {
        // When
        val state = UserUiState(
            id = 5,
            nombre = "María López",
            correo = "maria@example.com",
            fotopefil = "profile.png",
            clave = "securePass123",
            confirmarClave = "securePass123",
            direccion = "Avenida Siempre Viva 742",
            region = "Valparaíso",
            aceptaTerminos = true
        )

        // Then
        state.id shouldBe 5
        state.nombre shouldBe "María López"
        state.correo shouldBe "maria@example.com"
        state.fotopefil shouldBe "profile.png"
        state.clave shouldBe "securePass123"
        state.confirmarClave shouldBe "securePass123"
        state.direccion shouldBe "Avenida Siempre Viva 742"
        state.region shouldBe "Valparaíso"
        state.aceptaTerminos shouldBe true
    }

    @Test
    @DisplayName("Debería almacenar datos de login correctamente")
    fun `should store login data correctly`() {
        // When
        val state = UserUiState(
            loginCorreo = "usuario@test.com",
            loginClave = "mypassword",
            recordarUsuario = true
        )

        // Then
        state.loginCorreo shouldBe "usuario@test.com"
        state.loginClave shouldBe "mypassword"
        state.recordarUsuario shouldBe true
    }

    @Test
    @DisplayName("Debería almacenar errores de validación")
    fun `should store validation errors`() {
        // Given
        val errors = UserError(
            nombre = "Nombre requerido",
            correo = "Correo inválido"
        )

        // When
        val state = UserUiState(errores = errors)

        // Then
        state.errores shouldBe errors
        state.errores.nombre shouldBe "Nombre requerido"
        state.errores.correo shouldBe "Correo inválido"
    }

    @Test
    @DisplayName("Debería manejar estado sin foto de perfil")
    fun `should handle state without profile photo`() {
        // When
        val state = UserUiState(
            nombre = "Pedro",
            fotopefil = null
        )

        // Then
        state.fotopefil.shouldBeNull()
        state.nombre shouldBe "Pedro"
    }

    @Test
    @DisplayName("Debería manejar estado con foto de perfil")
    fun `should handle state with profile photo`() {
        // When
        val state = UserUiState(
            nombre = "Ana",
            fotopefil = "avatar.jpg"
        )

        // Then
        state.fotopefil.shouldNotBeNull()
        state.fotopefil shouldBe "avatar.jpg"
    }

    @Test
    @DisplayName("Debería representar usuario no autenticado")
    fun `should represent unauthenticated user`() {
        // When
        val state = UserUiState(
            currentUser = null,
            isLoggedIn = false
        )

        // Then
        state.currentUser.shouldBeNull()
        state.isLoggedIn shouldBe false
    }

    @Test
    @DisplayName("Debería actualizar aceptaTerminos independientemente")
    fun `should update aceptaTerminos independently`() {
        // When
        val state = UserUiState(aceptaTerminos = true)

        // Then
        state.aceptaTerminos shouldBe true
        state.nombre shouldBe ""
        state.correo shouldBe ""
    }

    @Test
    @DisplayName("Debería actualizar recordarUsuario independientemente")
    fun `should update recordarUsuario independently`() {
        // When
        val state = UserUiState(recordarUsuario = true)

        // Then
        state.recordarUsuario shouldBe true
        state.loginCorreo shouldBe ""
    }

    @Test
    @DisplayName("Debería soportar copy para actualizar solo currentUser")
    fun `should support copy to update only currentUser`() {
        // Given
        val initialState = UserUiState(nombre = "Test")
        val user = User(
            id = 1,
            nombre = "Usuario",
            correo = "test@test.com",
            clave = "pass",
            confirmarClave = "pass",
            direccion = "Dir",
            region = "Reg",
            aceptaTerminos = true
        )

        // When
        val updatedState = initialState.copy(currentUser = user, isLoggedIn = true)

        // Then
        updatedState.currentUser.shouldNotBeNull()
        updatedState.isLoggedIn shouldBe true
        updatedState.nombre shouldBe "Test"
    }

    @Test
    @DisplayName("Debería soportar copy para actualizar campos de registro")
    fun `should support copy to update registration fields`() {
        // Given
        val initialState = UserUiState()

        // When
        val updatedState = initialState.copy(
            nombre = "Nuevo Nombre",
            correo = "nuevo@email.com",
            clave = "newpass",
            confirmarClave = "newpass"
        )

        // Then
        updatedState.nombre shouldBe "Nuevo Nombre"
        updatedState.correo shouldBe "nuevo@email.com"
        updatedState.clave shouldBe "newpass"
        updatedState.confirmarClave shouldBe "newpass"
        updatedState.isLoggedIn shouldBe false
    }

    @Test
    @DisplayName("Debería soportar copy para actualizar campos de login")
    fun `should support copy to update login fields`() {
        // Given
        val initialState = UserUiState()

        // When
        val updatedState = initialState.copy(
            loginCorreo = "login@test.com",
            loginClave = "loginpass"
        )

        // Then
        updatedState.loginCorreo shouldBe "login@test.com"
        updatedState.loginClave shouldBe "loginpass"
    }

    @Test
    @DisplayName("Debería soportar copy para limpiar errores")
    fun `should support copy to clear errors`() {
        // Given
        val initialState = UserUiState(
            errores = UserError(nombre = "Error")
        )

        // When
        val updatedState = initialState.copy(errores = UserError())

        // Then
        updatedState.errores shouldBe UserError()
        updatedState.errores.nombre.shouldBeNull()
    }

    @Test
    @DisplayName("Debería soportar copy para actualizar errores")
    fun `should support copy to update errors`() {
        // Given
        val initialState = UserUiState()
        val newErrors = UserError(
            correo = "Email inválido",
            clave = "Contraseña débil"
        )

        // When
        val updatedState = initialState.copy(errores = newErrors)

        // Then
        updatedState.errores shouldBe newErrors
        updatedState.errores.correo shouldBe "Email inválido"
        updatedState.errores.clave shouldBe "Contraseña débil"
    }

    @Test
    @DisplayName("Debería mantener campos no modificados al usar copy")
    fun `should keep unmodified fields when using copy`() {
        // Given
        val initialState = UserUiState(
            nombre = "Original",
            correo = "original@test.com",
            direccion = "Dirección Original",
            aceptaTerminos = true
        )

        // When
        val updatedState = initialState.copy(nombre = "Modificado")

        // Then
        updatedState.nombre shouldBe "Modificado"
        updatedState.correo shouldBe "original@test.com"
        updatedState.direccion shouldBe "Dirección Original"
        updatedState.aceptaTerminos shouldBe true
    }

    @Test
    @DisplayName("Debería comparar igualdad correctamente")
    fun `should compare equality correctly`() {
        // Given
        val state1 = UserUiState(nombre = "Test", correo = "test@test.com")
        val state2 = UserUiState(nombre = "Test", correo = "test@test.com")
        val state3 = UserUiState(nombre = "Different", correo = "test@test.com")

        // Then
        (state1 == state2) shouldBe true
        (state1 == state3) shouldBe false
    }

    @Test
    @DisplayName("Debería generar hashCode consistente")
    fun `should generate consistent hashCode`() {
        // Given
        val state1 = UserUiState(nombre = "Test", loginCorreo = "test@test.com")
        val state2 = UserUiState(nombre = "Test", loginCorreo = "test@test.com")

        // Then
        state1.hashCode() shouldBe state2.hashCode()
    }

    @Test
    @DisplayName("Debería tener toString legible")
    fun `should have readable toString`() {
        // Given
        val state = UserUiState(
            nombre = "Test User",
            correo = "test@example.com",
            isLoggedIn = true
        )

        // When
        val toString = state.toString()

        // Then
        toString.contains("nombre") shouldBe true
        toString.contains("Test User") shouldBe true
        toString.contains("correo") shouldBe true
        toString.contains("test@example.com") shouldBe true
        toString.contains("isLoggedIn") shouldBe true
    }

    @Test
    @DisplayName("Debería representar formulario de registro completo")
    fun `should represent complete registration form`() {
        // When
        val state = UserUiState(
            nombre = "Carlos Rodríguez",
            correo = "carlos@email.com",
            fotopefil = "carlos.jpg",
            clave = "SecurePass123!",
            confirmarClave = "SecurePass123!",
            direccion = "Paseo Bulnes 194",
            region = "Región Metropolitana",
            aceptaTerminos = true
        )

        // Then
        state.nombre shouldBe "Carlos Rodríguez"
        state.correo shouldBe "carlos@email.com"
        state.fotopefil shouldBe "carlos.jpg"
        state.clave shouldBe "SecurePass123!"
        state.confirmarClave shouldBe "SecurePass123!"
        state.direccion shouldBe "Paseo Bulnes 194"
        state.region shouldBe "Región Metropolitana"
        state.aceptaTerminos shouldBe true
        state.isLoggedIn shouldBe false
        state.currentUser.shouldBeNull()
    }

    @Test
    @DisplayName("Debería representar formulario de login completo")
    fun `should represent complete login form`() {
        // When
        val state = UserUiState(
            loginCorreo = "usuario@huertohogar.com",
            loginClave = "MiPassword123",
            recordarUsuario = true
        )

        // Then
        state.loginCorreo shouldBe "usuario@huertohogar.com"
        state.loginClave shouldBe "MiPassword123"
        state.recordarUsuario shouldBe true
        state.isLoggedIn shouldBe false
    }

    @Test
    @DisplayName("Debería representar estado de sesión activa con usuario")
    fun `should represent active session state with user`() {
        // Given
        val user = User(
            id = 10,
            nombre = "Laura Martínez",
            correo = "laura@test.com",
            clave = "encrypted",
            confirmarClave = "encrypted",
            direccion = "Av. Principal 456",
            region = "Biobío",
            aceptaTerminos = true,
            fotopefil = "laura_profile.png"
        )

        // When
        val state = UserUiState(
            currentUser = user,
            isLoggedIn = true,
            recordarUsuario = true
        )

        // Then
        state.currentUser.shouldNotBeNull()
        state.currentUser?.id shouldBe 10
        state.currentUser?.nombre shouldBe "Laura Martínez"
        state.isLoggedIn shouldBe true
        state.recordarUsuario shouldBe true
    }

    @Test
    @DisplayName("Debería manejar estado con múltiples errores")
    fun `should handle state with multiple errors`() {
        // Given
        val errors = UserError(
            nombre = "Nombre muy corto",
            correo = "Formato inválido",
            clave = "Contraseña débil",
            confirmarClave = "No coincide",
            direccion = "Dirección requerida",
            region = "Región requerida"
        )

        // When
        val state = UserUiState(
            nombre = "AB",
            correo = "invalid-email",
            clave = "123",
            confirmarClave = "456",
            errores = errors
        )

        // Then
        state.errores.nombre shouldBe "Nombre muy corto"
        state.errores.correo shouldBe "Formato inválido"
        state.errores.clave shouldBe "Contraseña débil"
        state.errores.confirmarClave shouldBe "No coincide"
        state.errores.direccion shouldBe "Dirección requerida"
        state.errores.region shouldBe "Región requerida"
    }

    @Test
    @DisplayName("Debería permitir cambiar de no aceptar a aceptar términos")
    fun `should allow changing from not accepting to accepting terms`() {
        // Given
        val initialState = UserUiState(aceptaTerminos = false)

        // When
        val updatedState = initialState.copy(aceptaTerminos = true)

        // Then
        initialState.aceptaTerminos shouldBe false
        updatedState.aceptaTerminos shouldBe true
    }

    @Test
    @DisplayName("Debería permitir toggle de recordarUsuario")
    fun `should allow toggle of recordarUsuario`() {
        // Given
        val state1 = UserUiState(recordarUsuario = false)
        val state2 = state1.copy(recordarUsuario = true)
        val state3 = state2.copy(recordarUsuario = false)

        // Then
        state1.recordarUsuario shouldBe false
        state2.recordarUsuario shouldBe true
        state3.recordarUsuario shouldBe false
    }
}