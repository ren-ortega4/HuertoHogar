package com.example.huertohogar.network

import org.junit.Test
import org.junit.Assert.*

class RetrofitInstanceTest {

    @Test
    fun retrofitInstanciaNoEsNulaYBaseUrlEsCorrecta() {
        val retrofit = RetrofitInstance.retrofit
        assertNotNull(retrofit)
        // baseUrl() devuelve una HttpUrl; comparamos su String
        assertEquals("http://3.225.35.217:8080/", retrofit.baseUrl().toString())
    }

    @Test
    fun mercadoPagoApiSeCreaYEsSingleton() {
        val a = RetrofitInstance.mercadoPagoApi
        val b = RetrofitInstance.mercadoPagoApi

        assertNotNull(a)
        assertSame(a, b)
        assertTrue(a is MercadoPagoApi)
    }
}
