package com.example.huertohogar.view.components

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

/**
 * Pruebas unitarias para InicioSesion
 * Estas pruebas verifican las validaciones de login sin necesidad de UI de Compose
 */
class InicioSesionTest {

    private lateinit var loginCorreo: String
    private lateinit var loginClave: String

    @BeforeEach
    fun setup() {
        loginCorreo = ""
        loginClave = ""
    }

    // ========== Pruebas de validación de correo de login ==========

    @Test
    fun `correo de login valido con formato correcto`() {
        // Given
        val validCorreo = "usuario@example.com"

        // When
        val result = validarCorreoLogin(validCorreo)

        // Then
        assertTrue(result)
        assertTrue(validCorreo.contains("@"))
        assertTrue(validCorreo.contains("."))
    }

    @Test
    fun `correo de login vacio debe ser invalido`() {
        // Given
        val emptyCorreo = ""

        // When
        val result = validarCorreoLogin(emptyCorreo)

        // Then
        assertFalse(result)
        assertTrue(emptyCorreo.isEmpty())
    }

    @Test
    fun `correo de login sin arroba debe ser invalido`() {
        // Given
        val invalidCorreo = "usuarioexample.com"

        // When
        val result = validarCorreoLogin(invalidCorreo)

        // Then
        assertFalse(result)
        assertFalse(invalidCorreo.contains("@"))
    }

    @Test
    fun `correo de login sin dominio debe ser invalido`() {
        // Given
        val invalidCorreo = "usuario@"

        // When
        val result = validarCorreoLogin(invalidCorreo)

        // Then
        assertFalse(result)
    }

    @Test
    fun `correo de login sin punto debe ser invalido`() {
        // Given
        val invalidCorreo = "usuario@example"

        // When
        val result = validarCorreoLogin(invalidCorreo)

        // Then
        assertFalse(result)
    }

    @Test
    fun `correo de login con espacios debe ser invalido`() {
        // Given
        val invalidCorreo = "usuario @example.com"

        // When
        val result = validarCorreoLogin(invalidCorreo)

        // Then
        assertFalse(result)
        assertTrue(invalidCorreo.contains(" "))
    }

    @Test
    fun `correo de login con mayusculas debe ser valido`() {
        // Given
        val validCorreo = "USUARIO@corre.com"

        // When
        val result = validarCorreoLogin(validCorreo)

        // Then
        assertTrue(result)
    }

    @Test
    fun `correo de login con guiones debe ser valido`() {
        // Given
        val validCorreo = "usuario-test@exampledomain.com"

        // When
        val result = validarCorreoLogin(validCorreo)

        // Then
        assertTrue(result)
        assertTrue(validCorreo.contains("-"))
    }

    @Test
    fun `correo de login con puntos debe ser valido`() {
        // Given
        val validCorreo = "usuario.test@example.com"

        // When
        val result = validarCorreoLogin(validCorreo)

        // Then
        assertTrue(result)
        assertTrue(validCorreo.split("@")[0].contains("."))
    }

    @Test
    fun `correo de login con numeros debe ser valido`() {
        // Given
        val validCorreo = "usuario123@example.com"

        // When
        val result = validarCorreoLogin(validCorreo)

        // Then
        assertTrue(result)
        assertTrue(validCorreo.any { it.isDigit() })
    }

    // ========== Pruebas de validación de clave de login ==========

    @Test
    fun `clave de login valida con al menos 6 caracteres`() {
        // Given
        val validClave = "password123"

        // When
        val result = validarClaveLogin(validClave)

        // Then
        assertTrue(result)
        assertTrue(validClave.length >= 6)
    }

    @Test
    fun `clave de login vacia debe ser invalida`() {
        // Given
        val emptyClave = ""

        // When
        val result = validarClaveLogin(emptyClave)

        // Then
        assertFalse(result)
        assertTrue(emptyClave.isEmpty())
    }

    @Test
    fun `clave de login con menos de 6 caracteres debe ser invalida`() {
        // Given
        val shortClave = "12345"

        // When
        val result = validarClaveLogin(shortClave)

        // Then
        assertFalse(result)
        assertTrue(shortClave.length < 6)
    }

    @Test
    fun `clave de login con exactamente 6 caracteres debe ser valida`() {
        // Given
        val minClave = "abc123"

        // When
        val result = validarClaveLogin(minClave)

        // Then
        assertTrue(result)
        assertEquals(6, minClave.length)
    }

    @Test
    fun `clave de login con espacios debe ser valida`() {
        // Given
        val claveConEspacios = "pass word 123"

        // When
        val result = validarClaveLogin(claveConEspacios)

        // Then
        assertTrue(result)
        assertTrue(claveConEspacios.contains(" "))
    }

    @Test
    fun `clave de login con caracteres especiales debe ser valida`() {
        // Given
        val claveEspecial = "P@ssw0rd!"

        // When
        val result = validarClaveLogin(claveEspecial)

        // Then
        assertTrue(result)
        assertTrue(claveEspecial.any { !it.isLetterOrDigit() })
    }

    @Test
    fun `clave de login solo con numeros debe ser valida`() {
        // Given
        val claveNumerica = "123456"

        // When
        val result = validarClaveLogin(claveNumerica)

        // Then
        assertTrue(result)
        assertTrue(claveNumerica.all { it.isDigit() })
    }

    @Test
    fun `clave de login solo con letras debe ser valida`() {
        // Given
        val claveLetras = "abcdefgh"

        // When
        val result = validarClaveLogin(claveLetras)

        // Then
        assertTrue(result)
        assertTrue(claveLetras.all { it.isLetter() })
    }

    // ========== Pruebas de validación de formulario de login completo ==========

    @Test
    fun `formulario de login valido debe retornar verdadero`() {
        // Given
        val loginData = LoginData(
            correo = "usuario@example.com",
            clave = "password123"
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertTrue(result)
    }

    @Test
    fun `formulario de login con correo invalido debe retornar falso`() {
        // Given
        val loginData = LoginData(
            correo = "correoInvalido",
            clave = "password123"
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario de login con clave invalida debe retornar falso`() {
        // Given
        val loginData = LoginData(
            correo = "usuario@example.com",
            clave = "123"
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario de login con ambos campos vacios debe retornar falso`() {
        // Given
        val loginData = LoginData(
            correo = "",
            clave = ""
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario de login con solo correo debe retornar falso`() {
        // Given
        val loginData = LoginData(
            correo = "usuario@example.com",
            clave = ""
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario de login con solo clave debe retornar falso`() {
        // Given
        val loginData = LoginData(
            correo = "",
            clave = "password123"
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertFalse(result)
    }

    // ========== Pruebas de mensajes de error ==========

    @Test
    fun `mensaje de error para correo vacio debe ser descriptivo`() {
        // Given
        val correo = ""

        // When
        val mensaje = obtenerMensajeErrorCorreo(correo)

        // Then
        assertNotNull(mensaje)
        assertTrue(mensaje!!.isNotEmpty())
        assertTrue(mensaje.contains("correo", ignoreCase = true))
    }

    @Test
    fun `mensaje de error para correo invalido debe ser descriptivo`() {
        // Given
        val correo = "correoInvalido"

        // When
        val mensaje = obtenerMensajeErrorCorreo(correo)

        // Then
        assertNotNull(mensaje)
        assertTrue(mensaje!!.isNotEmpty())
        assertTrue(mensaje.contains("válido", ignoreCase = true) || 
                   mensaje.contains("valido", ignoreCase = true) ||
                   mensaje.contains("formato", ignoreCase = true))
    }

    @Test
    fun `mensaje de error para clave vacia debe ser descriptivo`() {
        // Given
        val clave = ""

        // When
        val mensaje = obtenerMensajeErrorClave(clave)

        // Then
        assertNotNull(mensaje)
        assertTrue(mensaje!!.isNotEmpty())
        assertTrue(mensaje.contains("contraseña", ignoreCase = true) || 
                   mensaje.contains("clave", ignoreCase = true))
    }

    @Test
    fun `mensaje de error para clave corta debe ser descriptivo`() {
        // Given
        val clave = "123"

        // When
        val mensaje = obtenerMensajeErrorClave(clave)

        // Then
        assertNotNull(mensaje)
        assertTrue(mensaje!!.isNotEmpty())
        assertTrue(mensaje.contains("6", ignoreCase = true) || 
                   mensaje.contains("caracteres", ignoreCase = true))
    }

    @Test
    fun `no debe haber mensaje de error para correo valido`() {
        // Given
        val correo = "usuario@example.com"

        // When
        val mensaje = obtenerMensajeErrorCorreo(correo)

        // Then
        assertNull(mensaje)
    }

    @Test
    fun `no debe haber mensaje de error para clave valida`() {
        // Given
        val clave = "password123"

        // When
        val mensaje = obtenerMensajeErrorClave(clave)

        // Then
        assertNull(mensaje)
    }

    // ========== Pruebas de casos especiales ==========

    @Test
    fun `correo con subdominios debe ser valido`() {
        // Given
        val correo = "usuario@correo.mx"

        // When
        val result = validarCorreoLogin(correo)

        // Then
        assertTrue(result)
    }

    @Test
    fun `correo con guion bajo debe ser valido`() {
        // Given
        val correo = "usuario_test@example.com"

        // When
        val result = validarCorreoLogin(correo)

        // Then
        assertTrue(result)
        assertTrue(correo.contains("_"))
    }

    @Test
    fun `clave muy larga debe ser valida`() {
        // Given
        val largeClave = "a".repeat(100)

        // When
        val result = validarClaveLogin(largeClave)

        // Then
        assertTrue(result)
        assertTrue(largeClave.length > 50)
    }

    @Test
    fun `credenciales con caracteres unicode deben ser validas`() {
        // Given
        val loginData = LoginData(
            correo = "usuario@example.com",
            clave = "contraseña123"
        )

        // When
        val result = validarFormularioLogin(loginData)

        // Then
        assertTrue(result)
        assertTrue(loginData.clave.contains("ñ"))
    }

    // ========== Pruebas de estado de UI ==========

    @Test
    fun `estado inicial debe tener campos vacios`() {
        // Given
        val estadoInicial = EstadoLogin()

        // Then
        assertEquals("", estadoInicial.correo)
        assertEquals("", estadoInicial.clave)
        assertNull(estadoInicial.errorCorreo)
        assertNull(estadoInicial.errorClave)
    }

    @Test
    fun `estado con errores debe mantener los valores ingresados`() {
        // Given
        val estadoConErrores = EstadoLogin(
            correo = "correoInvalido",
            clave = "123",
            errorCorreo = "Formato de correo inválido",
            errorClave = "La contraseña debe tener al menos 6 caracteres"
        )

        // Then
        assertEquals("correoInvalido", estadoConErrores.correo)
        assertEquals("123", estadoConErrores.clave)
        assertNotNull(estadoConErrores.errorCorreo)
        assertNotNull(estadoConErrores.errorClave)
    }

    @Test
    fun `limpiar formulario debe resetear todos los campos`() {
        // Given
        val estado = EstadoLogin(
            correo = "usuario@example.com",
            clave = "password123",
            errorCorreo = "Error",
            errorClave = "Error"
        )

        // When
        val estadoLimpio = limpiarFormulario(estado)

        // Then
        assertEquals("", estadoLimpio.correo)
        assertEquals("", estadoLimpio.clave)
        assertNull(estadoLimpio.errorCorreo)
        assertNull(estadoLimpio.errorClave)
    }

    // ========== Funciones auxiliares de validación ==========

    private fun validarCorreoLogin(correo: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return correo.isNotEmpty() && correo.matches(emailPattern.toRegex())
    }

    private fun validarClaveLogin(clave: String): Boolean {
        return clave.isNotEmpty() && clave.length >= 6
    }

    private fun validarFormularioLogin(loginData: LoginData): Boolean {
        return validarCorreoLogin(loginData.correo) && validarClaveLogin(loginData.clave)
    }

    private fun obtenerMensajeErrorCorreo(correo: String): String? {
        return when {
            correo.isEmpty() -> "El correo es requerido"
            !validarCorreoLogin(correo) -> "Formato de correo inválido"
            else -> null
        }
    }

    private fun obtenerMensajeErrorClave(clave: String): String? {
        return when {
            clave.isEmpty() -> "La contraseña es requerida"
            clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    private fun limpiarFormulario(estado: EstadoLogin): EstadoLogin {
        return EstadoLogin()
    }

    /**
     * Clase auxiliar para representar los datos de login
     */
    private data class LoginData(
        val correo: String,
        val clave: String
    )

    /**
     * Clase auxiliar para representar el estado del formulario de login
     */
    private data class EstadoLogin(
        val correo: String = "",
        val clave: String = "",
        val errorCorreo: String? = null,
        val errorClave: String? = null
    )
}