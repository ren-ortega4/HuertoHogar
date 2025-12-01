package com.example.huertohogar.network

import org.junit.Test
import org.junit.Assert.*

class ApiClienteTest {

    @Test
    fun instanciaNoEsNula() {
        val servicio = ApiCliente.instance
        assertNotNull(servicio)
    }

    @Test
    fun instanciaEsSingleton() {
        val a = ApiCliente.instance
        val b = ApiCliente.instance
        assertSame(a, b)
    }

    @Test
    fun instanciaImplementaApiService() {
        val servicio = ApiCliente.instance
        assertTrue(servicio is ApiService)
    }
}