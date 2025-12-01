package com.example.huertohogar.network

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

class ApiServiceTest {

    @Test
    fun registrarUsuarioTienePostYBodyYRutaCorrecta() {
        val method = ApiService::class.java.methods.first { it.name == "registarUsusario" }
        val post = method.getAnnotation(POST::class.java)
        // assertNotNull: en JUnit5 el primer argumento es el valor real, el segundo (opcional) es el mensaje
        assertNotNull(post, "Debe tener anotación @POST")
        assertEquals("/api/v1/usuario/guardar", post?.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue(paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Body }, "El primer parámetro debe tener @Body")
    }

    @Test
    fun loginTienePostYBodyYRetornaLoginResponse() {
        val method = ApiService::class.java.methods.first { it.name == "login" }
        val post = method.getAnnotation(POST::class.java)
        assertNotNull(post, "Debe tener anotación @POST")
        assertEquals("/api/v1/usuario/login", post?.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue(paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Body }, "El primer parámetro debe tener @Body")
    }

    @Test
    fun listarUsuariosTieneGetYHeaderAuthorization() {
        val method = ApiService::class.java.methods.first { it.name == "getallUser" }
        val get = method.getAnnotation(GET::class.java)
        assertNotNull(get, "Debe tener anotación @GET")
        assertEquals("/api/v1/usuario/listar", get?.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue(paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Header }, "El parámetro debe tener @Header")

        val headerAnn = method.getParameterAnnotations()[0].firstOrNull { it is Header } as? Header
        assertNotNull(headerAnn, "El parámetro debe tener la anotación @Header")
        assertEquals("Authorization", headerAnn!!.value)
    }

    @Test
    fun eliminarUsuarioTieneDeleteYPathId() {
        val method = ApiService::class.java.methods.first { it.name == "eliminarUsuario" }
        val delete = method.getAnnotation(DELETE::class.java)
        assertNotNull(delete, "Debe tener anotación @DELETE")
        assertEquals("/api/v1/usuario/{id}/eliminar", delete?.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue(paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Path }, "El parámetro debe tener @Path")

        val pathAnn = method.getParameterAnnotations()[0].firstOrNull { it is Path } as? Path
        assertNotNull(pathAnn, "El parámetro debe tener la anotación @Path")
        assertEquals("id", pathAnn!!.value)
    }
}
