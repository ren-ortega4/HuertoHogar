package com.example.huertohogar.viewmodel

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("UserError Tests")
class UserErrorTest {

    @Test
    @DisplayName("Debería crearse con todos los valores null por defecto")
    fun `should create UserError with all null values by default`() {
        // When
        val userError = UserError()

        // Then
        userError.nombre.shouldBeNull()
        userError.correo.shouldBeNull()
        userError.clave.shouldBeNull()
        userError.confirmarClave.shouldBeNull()
        userError.direccion.shouldBeNull()
        userError.region.shouldBeNull()
        userError.errorLoginCorreo.shouldBeNull()
        userError.errorLoginClave.shouldBeNull()
        userError.errorLoginGeneral.shouldBeNull()
    }

    @Test
    @DisplayName("Debería almacenar errores de registro correctamente")
    fun `should store registration errors correctly`() {
        // When
        val userError = UserError(
            nombre = "Nombre requerido",
            correo = "Correo inválido",
            clave = "Contraseña débil",
            confirmarClave = "Las contraseñas no coinciden",
            direccion = "Dirección requerida",
            region = "Región requerida"
        )

        // Then
        userError.nombre shouldBe "Nombre requerido"
        userError.correo shouldBe "Correo inválido"
        userError.clave shouldBe "Contraseña débil"
        userError.confirmarClave shouldBe "Las contraseñas no coinciden"
        userError.direccion shouldBe "Dirección requerida"
        userError.region shouldBe "Región requerida"
        userError.errorLoginCorreo.shouldBeNull()
        userError.errorLoginClave.shouldBeNull()
        userError.errorLoginGeneral.shouldBeNull()
    }

    @Test
    @DisplayName("Debería almacenar errores de login correctamente")
    fun `should store login errors correctly`() {
        // When
        val userError = UserError(
            errorLoginCorreo = "Correo no encontrado",
            errorLoginClave = "Contraseña incorrecta",
            errorLoginGeneral = "Error al iniciar sesión"
        )

        // Then
        userError.errorLoginCorreo shouldBe "Correo no encontrado"
        userError.errorLoginClave shouldBe "Contraseña incorrecta"
        userError.errorLoginGeneral shouldBe "Error al iniciar sesión"
        userError.nombre.shouldBeNull()
        userError.correo.shouldBeNull()
        userError.clave.shouldBeNull()
        userError.confirmarClave.shouldBeNull()
        userError.direccion.shouldBeNull()
        userError.region.shouldBeNull()
    }

    @Test
    @DisplayName("Debería permitir solo error en nombre")
    fun `should allow only nombre error`() {
        // When
        val userError = UserError(nombre = "El nombre es obligatorio")

        // Then
        userError.nombre.shouldNotBeNull()
        userError.nombre shouldBe "El nombre es obligatorio"
        userError.correo.shouldBeNull()
        userError.clave.shouldBeNull()
        userError.confirmarClave.shouldBeNull()
        userError.direccion.shouldBeNull()
        userError.region.shouldBeNull()
        userError.errorLoginCorreo.shouldBeNull()
        userError.errorLoginClave.shouldBeNull()
        userError.errorLoginGeneral.shouldBeNull()
    }

    @Test
    @DisplayName("Debería permitir solo error en correo")
    fun `should allow only correo error`() {
        // When
        val userError = UserError(correo = "Formato de correo inválido")

        // Then
        userError.correo.shouldNotBeNull()
        userError.correo shouldBe "Formato de correo inválido"
        userError.nombre.shouldBeNull()
    }

    @Test
    @DisplayName("Debería permitir solo error en clave")
    fun `should allow only clave error`() {
        // When
        val userError = UserError(clave = "La contraseña debe tener al menos 8 caracteres")

        // Then
        userError.clave.shouldNotBeNull()
        userError.clave shouldBe "La contraseña debe tener al menos 8 caracteres"
    }

    @Test
    @DisplayName("Debería permitir solo error en confirmarClave")
    fun `should allow only confirmarClave error`() {
        // When
        val userError = UserError(confirmarClave = "Debe confirmar la contraseña")

        // Then
        userError.confirmarClave.shouldNotBeNull()
        userError.confirmarClave shouldBe "Debe confirmar la contraseña"
    }

    @Test
    @DisplayName("Debería permitir solo error en direccion")
    fun `should allow only direccion error`() {
        // When
        val userError = UserError(direccion = "La dirección es obligatoria")

        // Then
        userError.direccion.shouldNotBeNull()
        userError.direccion shouldBe "La dirección es obligatoria"
    }

    @Test
    @DisplayName("Debería permitir solo error en region")
    fun `should allow only region error`() {
        // When
        val userError = UserError(region = "Debe seleccionar una región")

        // Then
        userError.region.shouldNotBeNull()
        userError.region shouldBe "Debe seleccionar una región"
    }

    @Test
    @DisplayName("Debería permitir solo error en errorLoginCorreo")
    fun `should allow only errorLoginCorreo error`() {
        // When
        val userError = UserError(errorLoginCorreo = "Usuario no registrado")

        // Then
        userError.errorLoginCorreo.shouldNotBeNull()
        userError.errorLoginCorreo shouldBe "Usuario no registrado"
    }

    @Test
    @DisplayName("Debería permitir solo error en errorLoginClave")
    fun `should allow only errorLoginClave error`() {
        // When
        val userError = UserError(errorLoginClave = "Credenciales incorrectas")

        // Then
        userError.errorLoginClave.shouldNotBeNull()
        userError.errorLoginClave shouldBe "Credenciales incorrectas"
    }

    @Test
    @DisplayName("Debería permitir solo error en errorLoginGeneral")
    fun `should allow only errorLoginGeneral error`() {
        // When
        val userError = UserError(errorLoginGeneral = "Servicio no disponible")

        // Then
        userError.errorLoginGeneral.shouldNotBeNull()
        userError.errorLoginGeneral shouldBe "Servicio no disponible"
    }

    @Test
    @DisplayName("Debería permitir múltiples errores simultáneos")
    fun `should allow multiple simultaneous errors`() {
        // When
        val userError = UserError(
            nombre = "Error nombre",
            correo = "Error correo",
            clave = "Error clave",
            errorLoginGeneral = "Error general"
        )

        // Then
        userError.nombre shouldBe "Error nombre"
        userError.correo shouldBe "Error correo"
        userError.clave shouldBe "Error clave"
        userError.errorLoginGeneral shouldBe "Error general"
        userError.confirmarClave.shouldBeNull()
        userError.direccion.shouldBeNull()
        userError.region.shouldBeNull()
        userError.errorLoginCorreo.shouldBeNull()
        userError.errorLoginClave.shouldBeNull()
    }

    @Test
    @DisplayName("Debería soportar copy para actualizar solo un campo")
    fun `should support copy to update single field`() {
        // Given
        val originalError = UserError(nombre = "Error inicial")

        // When
        val updatedError = originalError.copy(correo = "Nuevo error de correo")

        // Then
        updatedError.nombre shouldBe "Error inicial"
        updatedError.correo shouldBe "Nuevo error de correo"
        updatedError.clave.shouldBeNull()
    }

    @Test
    @DisplayName("Debería soportar copy para limpiar un campo")
    fun `should support copy to clear field`() {
        // Given
        val originalError = UserError(
            nombre = "Error nombre",
            correo = "Error correo"
        )

        // When
        val updatedError = originalError.copy(nombre = null)

        // Then
        updatedError.nombre.shouldBeNull()
        updatedError.correo shouldBe "Error correo"
    }

    @Test
    @DisplayName("Debería soportar equality comparison")
    fun `should support equality comparison`() {
        // Given
        val error1 = UserError(nombre = "Error")
        val error2 = UserError(nombre = "Error")
        val error3 = UserError(nombre = "Otro error")

        // Then
        (error1 == error2) shouldBe true
        (error1 == error3) shouldBe false
    }

    @Test
    @DisplayName("Debería generar hashCode consistente")
    fun `should generate consistent hashCode`() {
        // Given
        val error1 = UserError(nombre = "Error", correo = "Error correo")
        val error2 = UserError(nombre = "Error", correo = "Error correo")

        // Then
        error1.hashCode() shouldBe error2.hashCode()
    }

    @Test
    @DisplayName("Debería tener toString legible")
    fun `should have readable toString`() {
        // Given
        val userError = UserError(
            nombre = "Error nombre",
            correo = "Error correo"
        )

        // When
        val toString = userError.toString()

        // Then
        toString.contains("nombre") shouldBe true
        toString.contains("Error nombre") shouldBe true
        toString.contains("correo") shouldBe true
        toString.contains("Error correo") shouldBe true
    }

    @Test
    @DisplayName("Con todos los errores debería almacenar todos los valores")
    fun `should store all values when all errors are present`() {
        // When
        val userError = UserError(
            nombre = "Error 1",
            correo = "Error 2",
            clave = "Error 3",
            confirmarClave = "Error 4",
            direccion = "Error 5",
            region = "Error 6",
            errorLoginCorreo = "Error 7",
            errorLoginClave = "Error 8",
            errorLoginGeneral = "Error 9"
        )

        // Then
        userError.nombre shouldBe "Error 1"
        userError.correo shouldBe "Error 2"
        userError.clave shouldBe "Error 3"
        userError.confirmarClave shouldBe "Error 4"
        userError.direccion shouldBe "Error 5"
        userError.region shouldBe "Error 6"
        userError.errorLoginCorreo shouldBe "Error 7"
        userError.errorLoginClave shouldBe "Error 8"
        userError.errorLoginGeneral shouldBe "Error 9"
    }
}