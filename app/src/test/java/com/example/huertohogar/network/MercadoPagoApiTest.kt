package com.example.huertohogar.network

import org.junit.Test
import org.junit.Assert.*
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
        assertNotNull("Debe tener anotación @POST", post)
        assertEquals("mercadopago/preference", post.value)

        val paramAnnos = method.parameterAnnotations
        assertTrue("El primer parámetro debe tener @Body", paramAnnos.isNotEmpty() && paramAnnos[0].any { it is Body })

        // comprobar tipo de retorno genérico: Call<PreferenceResponse>
        val genericReturn = method.genericReturnType
        assertTrue("El tipo de retorno debe ser un ParameterizedType", genericReturn is ParameterizedType)

        val pType = genericReturn as ParameterizedType
        val raw = pType.rawType as Class<*>
        assertEquals("El tipo raw debe ser retrofit2.Call", Call::class.java, raw)

        val typeArgs = pType.actualTypeArguments
        assertEquals(1, typeArgs.size)
        assertEquals(PreferenceResponse::class.java, typeArgs[0] as Class<*>)
    }
}
