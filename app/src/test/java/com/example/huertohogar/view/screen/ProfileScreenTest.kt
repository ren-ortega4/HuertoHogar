package com.example.huertohogar.view.screen

import android.net.Uri
import com.example.huertohogar.model.User
import com.example.huertohogar.viewmodel.UserError
import com.example.huertohogar.viewmodel.UserUiState
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@DisplayName("Tests para ProfileScreen")
class ProfileScreenTest {

    @Nested
    @DisplayName("Tests del Data Class User")
    inner class `Tests del Data Class User` {

        @Test
        fun `debería crear un usuario con todos los campos requeridos`() {
            val usuario = User(
                id = 1,
                nombre = "Juan Pérez",
                correo = "juan@example.com",
                clave = "password123",
                confirmarClave = "password123",
                direccion = "Calle Principal 123",
                region = "Metropolitana",
                aceptaTerminos = true,
                fotopefil = "photo_uri"
            )

            usuario.id shouldBe 1
            usuario.nombre shouldBe "Juan Pérez"
            usuario.correo shouldBe "juan@example.com"
            usuario.clave shouldBe "password123"
            usuario.confirmarClave shouldBe "password123"
            usuario.direccion shouldBe "Calle Principal 123"
            usuario.region shouldBe "Metropolitana"
            usuario.aceptaTerminos shouldBe true
            usuario.fotopefil shouldBe "photo_uri"
        }

        @Test
        fun `debería crear un usuario con id autogenerado en 0`() {
            val usuario = User(
                nombre = "María López",
                correo = "maria@example.com",
                clave = "pass456",
                confirmarClave = "pass456",
                direccion = "Av. Libertad 456",
                region = "Valparaíso",
                aceptaTerminos = true
            )

            usuario.id shouldBe 0
        }

        @Test
        fun `debería permitir fotoperfil null por defecto`() {
            val usuario = User(
                nombre = "Pedro González",
                correo = "pedro@example.com",
                clave = "pass789",
                confirmarClave = "pass789",
                direccion = "Calle Sur 789",
                region = "Biobío",
                aceptaTerminos = true
            )

            usuario.fotopefil shouldBe null
        }

        @Test
        fun `debería manejar nombres con caracteres especiales`() {
            val usuario = crearUsuario(nombre = "José María Fernández Ñuñez")

            usuario.nombre shouldBe "José María Fernández Ñuñez"
            usuario.nombre.shouldContain("José")
            usuario.nombre.shouldContain("Ñuñez")
        }

        @Test
        fun `debería validar formato de correo electrónico`() {
            val usuario1 = crearUsuario(correo = "usuario@dominio.com")
            val usuario2 = crearUsuario(correo = "test.user@empresa.cl")
            val usuario3 = crearUsuario(correo = "admin@servidor.com.ar")

            usuario1.correo.shouldContain("@")
            usuario2.correo.shouldContain("@")
            usuario3.correo.shouldContain("@")
        }

        @Test
        fun `debería permitir diferentes regiones de Chile`() {
            val usuario1 = crearUsuario(region = "Metropolitana")
            val usuario2 = crearUsuario(region = "Valparaíso")
            val usuario3 = crearUsuario(region = "Biobío")
            val usuario4 = crearUsuario(region = "Araucanía")

            usuario1.region shouldBe "Metropolitana"
            usuario2.region shouldBe "Valparaíso"
            usuario3.region shouldBe "Biobío"
            usuario4.region shouldBe "Araucanía"
        }

        @Test
        fun `debería validar que aceptaTerminos sea booleano`() {
            val usuario1 = crearUsuario(aceptaTerminos = true)
            val usuario2 = crearUsuario(aceptaTerminos = false)

            usuario1.aceptaTerminos shouldBe true
            usuario2.aceptaTerminos shouldBe false
        }

        @Test
        fun `debería validar que clave y confirmarClave coincidan`() {
            val usuario = User(
                nombre = "Test User",
                correo = "test@test.com",
                clave = "password123",
                confirmarClave = "password123",
                direccion = "Test 123",
                region = "Test",
                aceptaTerminos = true
            )

            usuario.clave shouldBe usuario.confirmarClave
        }
    }

    @Nested
    @DisplayName("Tests del Data Class UserUiState")
    inner class `Tests del Data Class UserUiState` {

        @Test
        fun `debería crear UserUiState con valores por defecto`() {
            val estado = UserUiState()

            estado.currentUser shouldBe null
            estado.isLoggedIn shouldBe false
            estado.id shouldBe 0
            estado.nombre shouldBe ""
            estado.correo shouldBe ""
            estado.fotopefil shouldBe null
            estado.clave shouldBe ""
            estado.confirmarClave shouldBe ""
            estado.direccion shouldBe ""
            estado.region shouldBe ""
            estado.aceptaTerminos shouldBe false
            estado.loginCorreo shouldBe ""
            estado.loginClave shouldBe ""
            estado.recordarUsuario shouldBe false
        }

        @Test
        fun `debería crear UserUiState con usuario logueado`() {
            val usuario = crearUsuario()
            val estado = UserUiState(
                currentUser = usuario,
                isLoggedIn = true
            )

            estado.currentUser shouldNotBe null
            estado.isLoggedIn shouldBe true
            estado.currentUser?.nombre shouldBe usuario.nombre
        }

        @Test
        fun `debería mantener datos de formulario de registro`() {
            val estado = UserUiState(
                nombre = "Nuevo Usuario",
                correo = "nuevo@test.com",
                clave = "pass123",
                confirmarClave = "pass123",
                direccion = "Calle Nueva 123",
                region = "Metropolitana",
                aceptaTerminos = true
            )

            estado.nombre shouldBe "Nuevo Usuario"
            estado.correo shouldBe "nuevo@test.com"
            estado.clave shouldBe "pass123"
            estado.confirmarClave shouldBe "pass123"
            estado.direccion shouldBe "Calle Nueva 123"
            estado.region shouldBe "Metropolitana"
            estado.aceptaTerminos shouldBe true
        }

        @Test
        fun `debería mantener datos de formulario de login`() {
            val estado = UserUiState(
                loginCorreo = "login@test.com",
                loginClave = "loginpass"
            )

            estado.loginCorreo shouldBe "login@test.com"
            estado.loginClave shouldBe "loginpass"
        }

        @Test
        fun `debería permitir actualizar fotoperfil`() {
            val estado1 = UserUiState(fotopefil = null)
            val estado2 = UserUiState(fotopefil = "content://photo_uri")

            estado1.fotopefil shouldBe null
            estado2.fotopefil shouldBe "content://photo_uri"
        }

        @Test
        fun `debería mantener estado de recordarUsuario`() {
            val estado1 = UserUiState(recordarUsuario = true)
            val estado2 = UserUiState(recordarUsuario = false)

            estado1.recordarUsuario shouldBe true
            estado2.recordarUsuario shouldBe false
        }

        @Test
        fun `debería permitir copiar estado con cambios`() {
            val estado = UserUiState(nombre = "Original")
            val estadoModificado = estado.copy(nombre = "Modificado", correo = "nuevo@email.com")

            estado.nombre shouldBe "Original"
            estadoModificado.nombre shouldBe "Modificado"
            estadoModificado.correo shouldBe "nuevo@email.com"
        }

        @Test
        fun `debería mantener errores en el estado`() {
            val errores = UserError(
                nombre = "Nombre requerido",
                correo = "Correo inválido"
            )
            val estado = UserUiState(errores = errores)

            estado.errores.nombre shouldBe "Nombre requerido"
            estado.errores.correo shouldBe "Correo inválido"
        }
    }

    @Nested
    @DisplayName("Tests del Data Class UserError")
    inner class `Tests del Data Class UserError` {

        @Test
        fun `debería crear UserError sin errores por defecto`() {
            val errores = UserError()

            errores.nombre shouldBe null
            errores.correo shouldBe null
            errores.clave shouldBe null
            errores.confirmarClave shouldBe null
            errores.direccion shouldBe null
            errores.region shouldBe null
            errores.errorLoginCorreo shouldBe null
            errores.errorLoginClave shouldBe null
            errores.errorLoginGeneral shouldBe null
        }

        @Test
        fun `debería crear UserError con errores de registro`() {
            val errores = UserError(
                nombre = "El nombre es requerido",
                correo = "El correo no es válido",
                clave = "La contraseña es muy corta",
                confirmarClave = "Las contraseñas no coinciden",
                direccion = "La dirección es requerida",
                region = "La región es requerida"
            )

            errores.nombre shouldBe "El nombre es requerido"
            errores.correo shouldBe "El correo no es válido"
            errores.clave shouldBe "La contraseña es muy corta"
            errores.confirmarClave shouldBe "Las contraseñas no coinciden"
            errores.direccion shouldBe "La dirección es requerida"
            errores.region shouldBe "La región es requerida"
        }

        @Test
        fun `debería crear UserError con errores de login`() {
            val errores = UserError(
                errorLoginCorreo = "Correo no registrado",
                errorLoginClave = "Contraseña incorrecta",
                errorLoginGeneral = "Error al iniciar sesión"
            )

            errores.errorLoginCorreo shouldBe "Correo no registrado"
            errores.errorLoginClave shouldBe "Contraseña incorrecta"
            errores.errorLoginGeneral shouldBe "Error al iniciar sesión"
        }

        @Test
        fun `debería permitir errores parciales`() {
            val errores = UserError(
                nombre = "Error en nombre",
                clave = "Error en clave"
            )

            errores.nombre shouldBe "Error en nombre"
            errores.clave shouldBe "Error en clave"
            errores.correo shouldBe null
            errores.confirmarClave shouldBe null
        }

        @Test
        fun `debería permitir copiar errores con cambios`() {
            val errores = UserError(nombre = "Error original")
            val erroresModificados = errores.copy(
                nombre = "Error modificado",
                correo = "Nuevo error"
            )

            errores.nombre shouldBe "Error original"
            erroresModificados.nombre shouldBe "Error modificado"
            erroresModificados.correo shouldBe "Nuevo error"
        }

        @Test
        fun `debería validar que errores sean mensajes descriptivos`() {
            val errores = UserError(
                nombre = "El nombre debe tener al menos 3 caracteres",
                correo = "El formato del correo no es válido"
            )

            errores.nombre.shouldNotBeEmpty()
            errores.correo.shouldNotBeEmpty()
            errores.nombre!!.shouldContain("nombre")
            errores.correo!!.shouldContain("correo")
        }
    }

    @Nested
    @DisplayName("Tests de Lógica del Negocio")
    inner class `Tests de Lógica del Negocio` {

        @Test
        fun `debería validar que usuario logueado tenga sesión activa`() {
            val usuario = crearUsuario()
            val estado = UserUiState(
                currentUser = usuario,
                isLoggedIn = true
            )

            (estado.currentUser != null && estado.isLoggedIn) shouldBe true
        }

        @Test
        fun `debería validar que usuario no logueado no tenga sesión`() {
            val estado = UserUiState(
                currentUser = null,
                isLoggedIn = false
            )

            (estado.currentUser == null && !estado.isLoggedIn) shouldBe true
        }

        @Test
        fun `debería comparar usuarios por igualdad correctamente`() {
            val usuario1 = User(
                id = 1,
                nombre = "Test",
                correo = "test@test.com",
                clave = "pass",
                confirmarClave = "pass",
                direccion = "Dir",
                region = "Reg",
                aceptaTerminos = true
            )
            val usuario2 = User(
                id = 1,
                nombre = "Test",
                correo = "test@test.com",
                clave = "pass",
                confirmarClave = "pass",
                direccion = "Dir",
                region = "Reg",
                aceptaTerminos = true
            )
            val usuario3 = User(
                id = 2,
                nombre = "Test",
                correo = "test@test.com",
                clave = "pass",
                confirmarClave = "pass",
                direccion = "Dir",
                region = "Reg",
                aceptaTerminos = true
            )

            (usuario1 == usuario2) shouldBe true
            (usuario1 == usuario3) shouldBe false
        }

        @Test
        fun `debería generar hashCode consistente para usuarios iguales`() {
            val usuario1 = crearUsuario(id = 1, nombre = "Test")
            val usuario2 = crearUsuario(id = 1, nombre = "Test")

            usuario1.hashCode() shouldBe usuario2.hashCode()
        }

        @Test
        fun `debería generar toString informativo para User`() {
            val usuario = crearUsuario(nombre = "Juan", correo = "juan@test.com")
            val resultado = usuario.toString()

            resultado.shouldContain("User")
            resultado.shouldContain("nombre=Juan")
            resultado.shouldContain("correo=juan@test.com")
        }

        @Test
        fun `debería permitir copiar usuario con cambios`() {
            val usuario = crearUsuario(nombre = "Original", correo = "original@test.com")
            val usuarioCopia = usuario.copy(nombre = "Modificado", fotopefil = "nueva_foto")

            usuario.nombre shouldBe "Original"
            usuarioCopia.nombre shouldBe "Modificado"
            usuarioCopia.fotopefil shouldBe "nueva_foto"
            usuarioCopia.correo shouldBe "original@test.com"
        }

        @Test
        fun `debería permitir filtrar lista de usuarios por región`() {
            val usuarios = listOf(
                crearUsuario(id = 1, region = "Metropolitana"),
                crearUsuario(id = 2, region = "Valparaíso"),
                crearUsuario(id = 3, region = "Metropolitana"),
                crearUsuario(id = 4, region = "Biobío")
            )

            val metropolitanos = usuarios.filter { it.region == "Metropolitana" }

            metropolitanos shouldHaveSize 2
            metropolitanos.all { it.region == "Metropolitana" } shouldBe true
        }

        @Test
        fun `debería permitir ordenar usuarios por nombre`() {
            val usuarios = listOf(
                crearUsuario(nombre = "Zacarías"),
                crearUsuario(nombre = "Ana"),
                crearUsuario(nombre = "Mario")
            )

            val ordenados = usuarios.sortedBy { it.nombre }

            ordenados[0].nombre shouldBe "Ana"
            ordenados[1].nombre shouldBe "Mario"
            ordenados[2].nombre shouldBe "Zacarías"
        }

        @Test
        fun `debería permitir buscar usuarios por correo`() {
            val usuarios = listOf(
                crearUsuario(correo = "ana@test.com"),
                crearUsuario(correo = "juan@test.com"),
                crearUsuario(correo = "maria@test.com")
            )

            val encontrado = usuarios.find { it.correo == "juan@test.com" }

            encontrado shouldNotBe null
            encontrado?.correo shouldBe "juan@test.com"
        }

        @Test
        fun `debería agrupar usuarios por región`() {
            val usuarios = listOf(
                crearUsuario(id = 1, region = "Metropolitana"),
                crearUsuario(id = 2, region = "Metropolitana"),
                crearUsuario(id = 3, region = "Valparaíso")
            )

            val agrupados = usuarios.groupBy { it.region }

            agrupados.keys shouldHaveSize 2
            agrupados["Metropolitana"]?.shouldHaveSize(2)
            agrupados["Valparaíso"]?.shouldHaveSize(1)
        }

        @Test
        fun `debería validar usuarios que aceptaron términos`() {
            val usuarios = listOf(
                crearUsuario(id = 1, aceptaTerminos = true),
                crearUsuario(id = 2, aceptaTerminos = false),
                crearUsuario(id = 3, aceptaTerminos = true)
            )

            val aceptaron = usuarios.filter { it.aceptaTerminos }

            aceptaron shouldHaveSize 2
            aceptaron.all { it.aceptaTerminos } shouldBe true
        }

        @Test
        fun `debería validar que nombre de usuario no esté vacío`() {
            val usuario = crearUsuario(nombre = "Usuario Válido")

            usuario.nombre.shouldNotBeEmpty()
            usuario.nombre.isNotBlank() shouldBe true
        }

        @Test
        fun `debería validar formato de timestamp para nombres de archivo`() {
            val formato = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val timestamp = formato.format(Date())

            timestamp.shouldNotBeEmpty()
            timestamp.length shouldBe 15 // yyyyMMdd_HHmmss = 15 caracteres
            timestamp.shouldContain("_")
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Datos")
    inner class `Tests de Validación de Datos` {

        @Test
        fun `debería manejar usuarios con nombres muy largos`() {
            val nombreLargo = "A".repeat(200)
            val usuario = crearUsuario(nombre = nombreLargo)

            usuario.nombre shouldBe nombreLargo
            usuario.nombre.length shouldBe 200
        }

        @Test
        fun `debería manejar direcciones muy largas`() {
            val direccionLarga = "Calle Principal " + "Número ".repeat(50)
            val usuario = crearUsuario(direccion = direccionLarga)

            usuario.direccion.shouldContain("Calle Principal")
            usuario.direccion.length shouldBe direccionLarga.length
        }

        @Test
        fun `debería manejar usuarios con id negativos`() {
            val usuario = crearUsuario(id = -1)

            usuario.id shouldBe -1
        }

        @Test
        fun `debería permitir crear múltiples usuarios con mismo correo pero diferentes ids`() {
            val usuario1 = crearUsuario(id = 1, correo = "mismo@test.com")
            val usuario2 = crearUsuario(id = 2, correo = "mismo@test.com")

            usuario1.correo shouldBe usuario2.correo
            usuario1.id shouldNotBe usuario2.id
        }

        @Test
        fun `debería permitir cambiar fotoperfil usando copy`() {
            val usuario = crearUsuario(fotopefil = "foto1.jpg")
            val usuarioModificado = usuario.copy(fotopefil = "foto2.jpg")

            usuario.fotopefil shouldBe "foto1.jpg"
            usuarioModificado.fotopefil shouldBe "foto2.jpg"
        }

        @Test
        fun `debería validar contraseñas que no coinciden`() {
            val usuario = User(
                nombre = "Test",
                correo = "test@test.com",
                clave = "password123",
                confirmarClave = "password456",
                direccion = "Test",
                region = "Test",
                aceptaTerminos = true
            )

            (usuario.clave == usuario.confirmarClave) shouldBe false
        }

        @Test
        fun `debería validar longitud mínima de contraseña`() {
            val usuario1 = crearUsuario(clave = "12345678")
            val usuario2 = crearUsuario(clave = "123")

            (usuario1.clave.length >= 6) shouldBe true
            (usuario2.clave.length >= 6) shouldBe false
        }

        @Test
        fun `debería validar formato básico de correo electrónico`() {
            val correoValido = "usuario@dominio.com"
            val correoInvalido = "usuariosindominio"

            correoValido.contains("@") shouldBe true
            correoInvalido.contains("@") shouldBe false
        }

        @Test
        fun `debería permitir estados sin errores cuando validación es exitosa`() {
            val errores = UserError()
            val tieneErrores = errores.nombre != null || 
                              errores.correo != null || 
                              errores.clave != null

            tieneErrores shouldBe false
        }

        @Test
        fun `debería detectar cuando hay errores de registro`() {
            val errores = UserError(nombre = "Error", correo = "Error")
            val tieneErrores = errores.nombre != null || errores.correo != null

            tieneErrores shouldBe true
        }
    }

    @Nested
    @DisplayName("Tests de Casos Extremos")
    inner class `Tests de Casos Extremos` {

        @Test
        fun `debería manejar lista vacía de usuarios`() {
            val usuarios = emptyList<User>()

            usuarios shouldHaveSize 0
            usuarios.isEmpty() shouldBe true
        }

        @Test
        fun `debería manejar lista con un solo usuario`() {
            val usuarios = listOf(crearUsuario())

            usuarios shouldHaveSize 1
            usuarios.first().nombre shouldBe "Usuario Test"
        }

        @Test
        fun `debería manejar lista con muchos usuarios`() {
            val usuarios = (1..1000).map { crearUsuario(id = it, correo = "user$it@test.com") }

            usuarios shouldHaveSize 1000
            usuarios.first().id shouldBe 1
            usuarios.last().id shouldBe 1000
        }

        @Test
        fun `debería permitir usuarios con fotoperfil nula`() {
            val usuario = crearUsuario(fotopefil = null)

            usuario.fotopefil shouldBe null
        }

        @Test
        fun `debería permitir usuarios con fotoperfil vacía`() {
            val usuario = crearUsuario(fotopefil = "")

            usuario.fotopefil shouldBe ""
        }

        @Test
        fun `debería manejar búsqueda en lista vacía`() {
            val usuarios = emptyList<User>()
            val resultado = usuarios.filter { it.region == "Metropolitana" }

            resultado shouldHaveSize 0
        }

        @Test
        fun `debería permitir crear set de usuarios sin duplicados`() {
            val usuario = crearUsuario(id = 1)
            val usuarios = setOf(usuario, usuario, usuario)

            usuarios shouldHaveSize 1
        }

        @Test
        fun `debería permitir verificar existencia de usuario en colección`() {
            val usuario1 = crearUsuario(id = 1, nombre = "Juan")
            val usuario2 = crearUsuario(id = 2, nombre = "María")
            val usuarios = listOf(usuario1, usuario2)

            usuarios shouldContain usuario1
            usuarios shouldContain usuario2
        }

        @Test
        fun `debería permitir verificar no existencia de usuario en colección`() {
            val usuario1 = crearUsuario(id = 1)
            val usuario2 = crearUsuario(id = 2)
            val usuarios = listOf(usuario1)

            usuarios shouldNotContain usuario2
        }

        @Test
        fun `debería manejar null safety en operaciones con colecciones`() {
            val usuarios = listOf(
                crearUsuario(id = 1, nombre = "Ana"),
                crearUsuario(id = 2, nombre = "Pedro")
            )

            val encontrado = usuarios.find { it.nombre == "Ana" }
            val noEncontrado = usuarios.find { it.nombre == "Inexistente" }

            encontrado shouldNotBe null
            encontrado?.nombre shouldBe "Ana"
            noEncontrado shouldBe null
        }

        @Test
        fun `debería permitir mapear usuarios a sus correos`() {
            val usuarios = listOf(
                crearUsuario(correo = "ana@test.com"),
                crearUsuario(correo = "pedro@test.com"),
                crearUsuario(correo = "maria@test.com")
            )

            val correos = usuarios.map { it.correo }

            correos shouldHaveSize 3
            correos shouldContain "ana@test.com"
            correos shouldContain "pedro@test.com"
            correos shouldContain "maria@test.com"
        }

        @Test
        fun `debería permitir filtrar usuarios por aceptación de términos`() {
            val usuarios = listOf(
                crearUsuario(aceptaTerminos = true),
                crearUsuario(aceptaTerminos = false),
                crearUsuario(aceptaTerminos = true)
            )

            val aceptaron = usuarios.filter { it.aceptaTerminos }

            aceptaron shouldHaveSize 2
            aceptaron.all { it.aceptaTerminos } shouldBe true
        }

        @Test
        fun `debería manejar usuarios con nombres con espacios múltiples`() {
            val usuario = crearUsuario(nombre = "Juan    Carlos    Pérez")

            usuario.nombre shouldBe "Juan    Carlos    Pérez"
        }

        @Test
        fun `debería manejar contraseñas con caracteres especiales`() {
            val usuario = crearUsuario(clave = "P@ssw0rd!#$%")

            usuario.clave shouldBe "P@ssw0rd!#$%"
            usuario.clave.shouldContain("@")
            usuario.clave.shouldContain("!")
        }

        @Test
        fun `debería obtener regiones únicas de usuarios`() {
            val usuarios = listOf(
                crearUsuario(region = "Metropolitana"),
                crearUsuario(region = "Metropolitana"),
                crearUsuario(region = "Valparaíso"),
                crearUsuario(region = "Biobío")
            )

            val regionesUnicas = usuarios.map { it.region }.distinct()

            regionesUnicas shouldHaveSize 3
            regionesUnicas shouldContain "Metropolitana"
            regionesUnicas shouldContain "Valparaíso"
            regionesUnicas shouldContain "Biobío"
        }

        @Test
        fun `debería validar estado cuando usuario cierra sesión`() {
            val estadoInicial = UserUiState(
                currentUser = crearUsuario(),
                isLoggedIn = true
            )
            val estadoLogout = estadoInicial.copy(
                currentUser = null,
                isLoggedIn = false
            )

            estadoInicial.isLoggedIn shouldBe true
            estadoLogout.isLoggedIn shouldBe false
            estadoLogout.currentUser shouldBe null
        }

        @Test
        fun `debería manejar múltiples errores simultáneos`() {
            val errores = UserError(
                nombre = "Error 1",
                correo = "Error 2",
                clave = "Error 3",
                confirmarClave = "Error 4"
            )

            val cantidadErrores = listOfNotNull(
                errores.nombre,
                errores.correo,
                errores.clave,
                errores.confirmarClave
            ).size

            cantidadErrores shouldBe 4
        }
    }
    // Función auxiliar para crear usuarios de prueba
    private fun crearUsuario(
        id: Int = 0,
        nombre: String = "Usuario Test",
        correo: String = "test@example.com",
        clave: String = "password123",
        confirmarClave: String = "password123",
        direccion: String = "Calle Test 123",
        region: String = "Metropolitana",
        aceptaTerminos: Boolean = true,
        fotopefil: String? = null
    ): User {
        return User(
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
    }
}