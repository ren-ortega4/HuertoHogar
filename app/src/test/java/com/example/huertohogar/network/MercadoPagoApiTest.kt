package com.example.huertohogar.network

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import com.example.huertohogar.model.CartRequest
import com.example.huertohogar.model.PreferenceResponse
import java.lang.reflect.ParameterizedType

class MercadoPagoApiTest {

    @Test
    fun createPreferenceTienePostYBodyYRutaCorrecta() {
        val method = MercadoPagoApi::class.java.methods.first { it.name == "createPreference" }

        val post = method.getAnnotation(POST::class.java)
        // assertNotNull: en JUnit5 el primer argumento es el valor real, el segundo (opcional) es el mensaje
        assertNotNull(post, "Debe tener anotación @POST")
        assertEquals("mercadopago/preference", post?.value, "Ruta incorrecta en @POST")

        val paramAnnos = method.parameterAnnotations
        assertTrue(paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Body }, "El primer parámetro debe tener @Body")

        // comprobar tipo de retorno genérico: Call<PreferenceResponse>
        val genericReturn = method.genericReturnType
        assertTrue(genericReturn is ParameterizedType, "El tipo de retorno debe ser un ParameterizedType")

        val pType = genericReturn as ParameterizedType
        val raw = pType.rawType as Class<*>
        assertEquals(Call::class.java, raw, "El tipo raw debe ser retrofit2.Call")

        val typeArgs = pType.actualTypeArguments
        assertEquals(1, typeArgs.size, "El tipo genérico debe tener un argumento")
        assertEquals(PreferenceResponse::class.java, typeArgs[0] as Class<*>, "El argumento genérico debe ser PreferenceResponse")
    }
}
