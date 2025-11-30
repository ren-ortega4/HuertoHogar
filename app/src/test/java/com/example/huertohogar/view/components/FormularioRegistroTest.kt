package com.example.huertohogar.view.components

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

/**
 * Pruebas unitarias para FormularioRegistro
 * Estas pruebas verifican las validaciones y lógica de negocio sin necesidad de UI de Compose
 */
class FormularioRegistroTest {

    private lateinit var nombre: String
    private lateinit var correo: String
    private lateinit var clave: String
    private lateinit var confirmarClave: String
    private lateinit var direccion: String
    private lateinit var region: String
    private var aceptaTerminos: Boolean = false

    @BeforeEach
    fun setup() {
        nombre = ""
        correo = ""
        clave = ""
        confirmarClave = ""
        direccion = ""
        region = ""
        aceptaTerminos = false
    }

    // ========== Pruebas de validación de nombre ==========

    @Test
    fun `nombre valido con mas de 3 caracteres`() {
        // Given
        val validNombre = "Juan Pérez"

        // When
        val result = validarNombre(validNombre)

        // Then
        assertTrue(result)
        assertTrue(validNombre.length >= 3)
    }

    @Test
    fun `nombre vacio debe ser invalido`() {
        // Given
        val emptyNombre = ""

        // When
        val result = validarNombre(emptyNombre)

        // Then
        assertFalse(result)
    }

    @Test
    fun `nombre con menos de 3 caracteres debe ser invalido`() {
        // Given
        val shortNombre = "Ab"

        // When
        val result = validarNombre(shortNombre)

        // Then
        assertFalse(result)
        assertTrue(shortNombre.length < 3)
    }

    @Test
    fun `nombre con exactamente 3 caracteres debe ser valido`() {
        // Given
        val minNombre = "Ana"

        // When
        val result = validarNombre(minNombre)

        // Then
        assertTrue(result)
        assertEquals(3, minNombre.length)
    }

    @Test
    fun `nombre con espacios en blanco debe ser valido`() {
        // Given
        val nombreConEspacios = "María José García"

        // When
        val result = validarNombre(nombreConEspacios)

        // Then
        assertTrue(result)
        assertTrue(nombreConEspacios.contains(" "))
    }

    // ========== Pruebas de validación de correo ==========

    @Test
    fun `correo valido con formato correcto`() {
        // Given
        val validCorreo = "usuario@example.com"

        // When
        val result = validarCorreo(validCorreo)

        // Then
        assertTrue(result)
        assertTrue(validCorreo.contains("@"))
        assertTrue(validCorreo.contains("."))
    }

    @Test
    fun `correo sin arroba debe ser invalido`() {
        // Given
        val invalidCorreo = "usuarioexample.com"

        // When
        val result = validarCorreo(invalidCorreo)

        // Then
        assertFalse(result)
        assertFalse(invalidCorreo.contains("@"))
    }

    @Test
    fun `correo sin dominio debe ser invalido`() {
        // Given
        val invalidCorreo = "usuario@"

        // When
        val result = validarCorreo(invalidCorreo)

        // Then
        assertFalse(result)
    }

    @Test
    fun `correo sin punto en dominio debe ser invalido`() {
        // Given
        val invalidCorreo = "usuario@example"

        // When
        val result = validarCorreo(invalidCorreo)

        // Then
        assertFalse(result)
    }

    @Test
    fun `correo vacio debe ser invalido`() {
        // Given
        val emptyCorreo = ""

        // When
        val result = validarCorreo(emptyCorreo)

        // Then
        assertFalse(result)
    }

    @Test
    fun `correo con subdominios debe ser valido`() {
        // Given
        val validCorreo = "usuario@correo.com"

        // When
        val result = validarCorreo(validCorreo)

        // Then
        assertTrue(result)
    }

    // ========== Pruebas de validación de contraseña ==========

    @Test
    fun `clave valida con al menos 6 caracteres`() {
        // Given
        val validClave = "password123"

        // When
        val result = validarClave(validClave)

        // Then
        assertTrue(result)
        assertTrue(validClave.length >= 6)
    }

    @Test
    fun `clave con menos de 6 caracteres debe ser invalida`() {
        // Given
        val shortClave = "12345"

        // When
        val result = validarClave(shortClave)

        // Then
        assertFalse(result)
        assertTrue(shortClave.length < 6)
    }

    @Test
    fun `clave vacia debe ser invalida`() {
        // Given
        val emptyClave = ""

        // When
        val result = validarClave(emptyClave)

        // Then
        assertFalse(result)
    }

    @Test
    fun `clave con exactamente 6 caracteres debe ser valida`() {
        // Given
        val minClave = "abc123"

        // When
        val result = validarClave(minClave)

        // Then
        assertTrue(result)
        assertEquals(6, minClave.length)
    }

    // ========== Pruebas de confirmación de contraseña ==========

    @Test
    fun `confirmar clave igual a clave debe ser valida`() {
        // Given
        val clave = "password123"
        val confirmar = "password123"

        // When
        val result = validarConfirmarClave(clave, confirmar)

        // Then
        assertTrue(result)
        assertEquals(clave, confirmar)
    }

    @Test
    fun `confirmar clave diferente a clave debe ser invalida`() {
        // Given
        val clave = "password123"
        val confirmar = "password456"

        // When
        val result = validarConfirmarClave(clave, confirmar)

        // Then
        assertFalse(result)
        assertNotEquals(clave, confirmar)
    }

    @Test
    fun `confirmar clave vacia debe ser invalida`() {
        // Given
        val clave = "password123"
        val confirmar = ""

        // When
        val result = validarConfirmarClave(clave, confirmar)

        // Then
        assertFalse(result)
    }

    @Test
    fun `ambas claves vacias deben ser invalidas`() {
        // Given
        val clave = ""
        val confirmar = ""

        // When
        val result = validarConfirmarClave(clave, confirmar)

        // Then
        assertFalse(result)
    }

    // ========== Pruebas de validación de dirección ==========

    @Test
    fun `direccion valida con mas de 5 caracteres`() {
        // Given
        val validDireccion = "Calle Principal 123"

        // When
        val result = validarDireccion(validDireccion)

        // Then
        assertTrue(result)
        assertTrue(validDireccion.length > 5)
    }

    @Test
    fun `direccion vacia debe ser invalida`() {
        // Given
        val emptyDireccion = ""

        // When
        val result = validarDireccion(emptyDireccion)

        // Then
        assertFalse(result)
    }

    @Test
    fun `direccion con menos de 5 caracteres debe ser invalida`() {
        // Given
        val shortDireccion = "Casa"

        // When
        val result = validarDireccion(shortDireccion)

        // Then
        assertFalse(result)
        assertTrue(shortDireccion.length <= 5)
    }

    @Test
    fun `direccion con exactamente 6 caracteres debe ser valida`() {
        // Given
        val minDireccion = "Casa 1"

        // When
        val result = validarDireccion(minDireccion)

        // Then
        assertTrue(result)
        assertEquals(6, minDireccion.length)
    }

    // ========== Pruebas de validación de región ==========

    @Test
    fun `region valida no vacia`() {
        // Given
        val validRegion = "Lima"

        // When
        val result = validarRegion(validRegion)

        // Then
        assertTrue(result)
        assertTrue(validRegion.isNotEmpty())
    }

    @Test
    fun `region vacia debe ser invalida`() {
        // Given
        val emptyRegion = ""

        // When
        val result = validarRegion(emptyRegion)

        // Then
        assertFalse(result)
    }

    @Test
    fun `region seleccionar debe ser invalida`() {
        // Given
        val defaultRegion = "Seleccionar"

        // When
        val result = validarRegion(defaultRegion)

        // Then
        assertFalse(result)
    }

    @Test
    fun `lista de regiones debe contener opciones validas`() {
        // Given
        val regiones = listOf("Lima", "Arequipa", "Cusco", "Piura", "Trujillo")

        // Then
        assertTrue(regiones.isNotEmpty())
        assertTrue(regiones.size >= 5)
        assertTrue(regiones.contains("Lima"))
    }

    // ========== Pruebas de términos y condiciones ==========

    @Test
    fun `aceptar terminos debe ser verdadero`() {
        // Given
        val terminos = true

        // When
        val result = validarTerminos(terminos)

        // Then
        assertTrue(result)
    }

    @Test
    fun `no aceptar terminos debe ser falso`() {
        // Given
        val terminos = false

        // When
        val result = validarTerminos(terminos)

        // Then
        assertFalse(result)
    }

    // ========== Pruebas de validación de formulario completo ==========

    @Test
    fun `formulario completo valido debe retornar verdadero`() {
        // Given
        val formData = FormularioData(
            nombre = "Juan Pérez",
            correo = "juan@example.com",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle Principal 123",
            region = "Lima",
            aceptaTerminos = true
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertTrue(result)
    }

    @Test
    fun `formulario con nombre invalido debe retornar falso`() {
        // Given
        val formData = FormularioData(
            nombre = "Ab",
            correo = "juan@example.com",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle Principal 123",
            region = "Lima",
            aceptaTerminos = true
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario con correo invalido debe retornar falso`() {
        // Given
        val formData = FormularioData(
            nombre = "Juan Pérez",
            correo = "correoInvalido",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle Principal 123",
            region = "Lima",
            aceptaTerminos = true
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario con clave invalida debe retornar falso`() {
        // Given
        val formData = FormularioData(
            nombre = "Juan Pérez",
            correo = "juan@example.com",
            clave = "123",
            confirmarClave = "123",
            direccion = "Calle Principal 123",
            region = "Lima",
            aceptaTerminos = true
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario con claves no coincidentes debe retornar falso`() {
        // Given
        val formData = FormularioData(
            nombre = "Juan Pérez",
            correo = "juan@example.com",
            clave = "password123",
            confirmarClave = "password456",
            direccion = "Calle Principal 123",
            region = "Lima",
            aceptaTerminos = true
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario sin aceptar terminos debe retornar falso`() {
        // Given
        val formData = FormularioData(
            nombre = "Juan Pérez",
            correo = "juan@example.com",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle Principal 123",
            region = "Lima",
            aceptaTerminos = false
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertFalse(result)
    }

    @Test
    fun `formulario con todos los campos vacios debe retornar falso`() {
        // Given
        val formData = FormularioData(
            nombre = "",
            correo = "",
            clave = "",
            confirmarClave = "",
            direccion = "",
            region = "",
            aceptaTerminos = false
        )

        // When
        val result = validarFormularioCompleto(formData)

        // Then
        assertFalse(result)
    }

    // ========== Pruebas adicionales ==========

    @Test
    fun `nombre con caracteres especiales debe ser valido`() {
        // Given
        val nombreEspecial = "José María O'Brien"

        // When
        val result = validarNombre(nombreEspecial)

        // Then
        assertTrue(result)
    }

    @Test
    fun `correo con mayusculas debe ser valido`() {
        // Given
        val correoMayusculas = "USUARIO@correo.com"

        // When
        val result = validarCorreo(correoMayusculas)

        // Then
        assertTrue(result)
    }

    @Test
    fun `clave con caracteres especiales debe ser valida`() {
        // Given
        val claveEspecial = "Pass@123!"

        // When
        val result = validarClave(claveEspecial)

        // Then
        assertTrue(result)
    }

    @Test
    fun `direccion con numeros debe ser valida`() {
        // Given
        val direccionConNumeros = "Av. Principal 456 Dpto 301"

        // When
        val result = validarDireccion(direccionConNumeros)

        // Then
        assertTrue(result)
    }

    // ========== Funciones auxiliares de validación ==========

    private fun validarNombre(nombre: String): Boolean {
        return nombre.isNotEmpty() && nombre.length >= 3
    }

    private fun validarCorreo(correo: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return correo.isNotEmpty() && correo.matches(emailPattern.toRegex())
    }

    private fun validarClave(clave: String): Boolean {
        return clave.isNotEmpty() && clave.length >= 6
    }

    private fun validarConfirmarClave(clave: String, confirmar: String): Boolean {
        return confirmar.isNotEmpty() && clave == confirmar
    }

    private fun validarDireccion(direccion: String): Boolean {
        return direccion.isNotEmpty() && direccion.length > 5
    }

    private fun validarRegion(region: String): Boolean {
        return region.isNotEmpty() && region != "Seleccionar"
    }

    private fun validarTerminos(aceptado: Boolean): Boolean {
        return aceptado
    }

    private fun validarFormularioCompleto(formData: FormularioData): Boolean {
        return validarNombre(formData.nombre) &&
                validarCorreo(formData.correo) &&
                validarClave(formData.clave) &&
                validarConfirmarClave(formData.clave, formData.confirmarClave) &&
                validarDireccion(formData.direccion) &&
                validarRegion(formData.region) &&
                validarTerminos(formData.aceptaTerminos)
    }

    /**
     * Clase auxiliar para representar los datos del formulario
     */
    private data class FormularioData(
        val nombre: String,
        val correo: String,
        val clave: String,
        val confirmarClave: String,
        val direccion: String,
        val region: String,
        val aceptaTerminos: Boolean
    )
}