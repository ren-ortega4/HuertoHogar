package com.example.huertohogar.network

import org.junit.Test
import org.junit.Assert.*
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
        assertNotNull("Debe tener anotación @POST", post)
        assertEquals("/api/v1/usuario/guardar", post.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue("El primer parámetro debe tener @Body", paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Body })
    }

    @Test
    fun loginTienePostYBodyYRetornaLoginResponse() {
        val method = ApiService::class.java.methods.first { it.name == "login" }
        val post = method.getAnnotation(POST::class.java)
        assertNotNull(post)
        assertEquals("/api/v1/usuario/login", post.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue("El primer parámetro debe tener @Body", paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Body })
    }

    @Test
    fun listarUsuariosTieneGetYHeaderAuthorization() {
        val method = ApiService::class.java.methods.first { it.name == "getallUser" }
        val get = method.getAnnotation(GET::class.java)
        assertNotNull(get)
        assertEquals("/api/v1/usuario/listar", get.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue("El parámetro debe tener @Header", paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Header })

        val headerAnn = method.getParameterAnnotations()[0].firstOrNull { it is Header } as? Header
        assertNotNull(headerAnn)
        assertEquals("Authorization", headerAnn!!.value)
    }

    @Test
    fun eliminarUsuarioTieneDeleteYPathId() {
        val method = ApiService::class.java.methods.first { it.name == "eliminarUsuario" }
        val delete = method.getAnnotation(DELETE::class.java)
        assertNotNull(delete)
        assertEquals("/api/v1/usuario/{id}/eliminar", delete.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue("El parámetro debe tener @Path", paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Path })

        val pathAnn = method.getParameterAnnotations()[0].firstOrNull { it is Path } as? Path
        assertNotNull(pathAnn)
        assertEquals("id", pathAnn!!.value)
    }
}
