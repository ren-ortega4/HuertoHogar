package com.example.huertohogar.local

import com.example.huertohogar.data.local.TiendaDao
import com.example.huertohogar.model.Tienda
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TiendaDaoTest {

    private lateinit var tiendaDao: TiendaDao

    @BeforeEach
    fun setUp() {
        tiendaDao = mockk(relaxed = true)
    }

    @Test
    fun `insertAll should insert tiendas successfully`() = runTest {
        // Given
        val tiendas = listOf(
            Tienda(id = 1, name = "HuertoHogar Santiago", address = "Salomon Sumal 3420", phone = "+56987654321", latitude = -33.664826756779775, longitude = -70.83641809450158, description = "Tienda principal"),
            Tienda(id = 2, name = "HuertoHogar Viña", address = "Calle Valparaíso 463", phone = "+56987654322", latitude = -33.024143063501725, longitude = -71.55606707327293, description = "Sucursal Viña")
        )
        coEvery { tiendaDao.insertAll(tiendas) } returns Unit

        // When
        tiendaDao.insertAll(tiendas)

        // Then
        coVerify(exactly = 1) { tiendaDao.insertAll(tiendas) }
    }

    @Test
    fun `insertAll should handle empty list`() = runTest {
        // Given
        val emptyList = emptyList<Tienda>()
        coEvery { tiendaDao.insertAll(emptyList) } returns Unit

        // When
        tiendaDao.insertAll(emptyList)

        // Then
        coVerify(exactly = 1) { tiendaDao.insertAll(emptyList) }
    }

    @Test
    fun `getAllTiendas should return list of tiendas`() = runTest {
        // Given
        val tiendas = listOf(
            Tienda(id = 1, name = "HuertoHogar Santiago", address = "Salomon Sumal 3420", phone = "+56987654321", latitude = -33.664826756779775, longitude = -70.83641809450158, description = "Tienda principal"),
            Tienda(id = 2, name = "HuertoHogar Viña", address = "Calle Valparaíso 463", phone = "+56987654322", latitude = -33.024143063501725, longitude = -71.55606707327293, description = "Sucursal Viña")
        )
        coEvery { tiendaDao.getAllTiendas() } returns tiendas

        // When
        val result = tiendaDao.getAllTiendas()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("HuertoHogar Santiago", result[0].name)
        assertEquals("HuertoHogar Viña", result[1].name)
    }

    @Test
    fun `getAllTiendas should return empty list when no tiendas`() = runTest {
        // Given
        coEvery { tiendaDao.getAllTiendas() } returns emptyList()

        // When
        val result = tiendaDao.getAllTiendas()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `deleteAllTiendas should delete all tiendas`() = runTest {
        // Given
        coEvery { tiendaDao.deleteAllTiendas() } returns Unit

        // When
        tiendaDao.deleteAllTiendas()

        // Then
        coVerify(exactly = 1) { tiendaDao.deleteAllTiendas() }
    }

    @Test
    fun `insertAll should replace on conflict`() = runTest {
        // Given
        val tienda1 = Tienda(id = 1, name = "HuertoHogar Santiago", address = "Dirección antigua", phone = "+56987654321", latitude = -33.664826756779775, longitude = -70.83641809450158, description = "Descripción antigua")
        val tienda2 = Tienda(id = 1, name = "HuertoHogar Santiago Centro", address = "Dirección nueva", phone = "+56987654321", latitude = -33.664826756779775, longitude = -70.83641809450158, description = "Descripción nueva")
        
        coEvery { tiendaDao.insertAll(listOf(tienda1)) } returns Unit
        coEvery { tiendaDao.insertAll(listOf(tienda2)) } returns Unit

        // When
        tiendaDao.insertAll(listOf(tienda1))
        tiendaDao.insertAll(listOf(tienda2))

        // Then
        coVerify(exactly = 1) { tiendaDao.insertAll(listOf(tienda1)) }
        coVerify(exactly = 1) { tiendaDao.insertAll(listOf(tienda2)) }
    }

    @Test
    fun `tienda should have all required properties`() = runTest {
        // Given
        val tienda = Tienda(
            id = 1,
            name = "HuertoHogar Santiago",
            address = "Salomon Sumal 3420, San Joaquin",
            phone = "+569 87654321",
            latitude = -33.664826756779775,
            longitude = -70.83641809450158,
            description = "Región Metropolitana"
        )
        coEvery { tiendaDao.getAllTiendas() } returns listOf(tienda)

        // When
        val result = tiendaDao.getAllTiendas()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("HuertoHogar Santiago", result[0].name)
        assertEquals("Salomon Sumal 3420, San Joaquin", result[0].address)
        assertEquals("+569 87654321", result[0].phone)
        assertEquals(-33.664826756779775, result[0].latitude, 0.0001)
        assertEquals(-70.83641809450158, result[0].longitude, 0.0001)
        assertEquals("Región Metropolitana", result[0].description)
    }

    @Test
    fun `getAllTiendas should return multiple tiendas with different locations`() = runTest {
        // Given
        val tiendas = listOf(
            Tienda(id = 1, name = "Tienda Norte", address = "Dirección Norte", phone = "+56987654321", latitude = -33.4, longitude = -70.5, description = "Norte"),
            Tienda(id = 2, name = "Tienda Sur", address = "Dirección Sur", phone = "+56987654322", latitude = -33.6, longitude = -70.7, description = "Sur"),
            Tienda(id = 3, name = "Tienda Este", address = "Dirección Este", phone = "+56987654323", latitude = -33.5, longitude = -70.4, description = "Este")
        )
        coEvery { tiendaDao.getAllTiendas() } returns tiendas

        // When
        val result = tiendaDao.getAllTiendas()

        // Then
        assertEquals(3, result.size)
        assertEquals("Tienda Norte", result[0].name)
        assertEquals("Tienda Sur", result[1].name)
        assertEquals("Tienda Este", result[2].name)
    }

    @Test
    fun `insertAll should handle single tienda`() = runTest {
        // Given
        val tienda = listOf(
            Tienda(id = 1, name = "HuertoHogar Único", address = "Dirección única", phone = "+56987654321", latitude = -33.5, longitude = -70.6, description = "Única tienda")
        )
        coEvery { tiendaDao.insertAll(tienda) } returns Unit

        // When
        tiendaDao.insertAll(tienda)

        // Then
        coVerify(exactly = 1) { tiendaDao.insertAll(match { it.size == 1 }) }
    }

    @Test
    fun `tienda coordinates should be valid doubles`() = runTest {
        // Given
        val tienda = Tienda(
            id = 1,
            name = "Test Tienda",
            address = "Test Address",
            phone = "+56987654321",
            latitude = -33.664826756779775,
            longitude = -70.83641809450158,
            description = "Test"
        )
        coEvery { tiendaDao.getAllTiendas() } returns listOf(tienda)

        // When
        val result = tiendaDao.getAllTiendas()

        // Then
        assertTrue(result[0].latitude < 0) // Chile está en hemisferio sur
        assertTrue(result[0].longitude < 0) // Chile está en hemisferio oeste
        assertTrue(result[0].latitude > -90 && result[0].latitude < 90)
        assertTrue(result[0].longitude > -180 && result[0].longitude < 180)
    }

    @Test
    fun `insertAll should handle large list of tiendas`() = runTest {
        // Given
        val tiendas = (1..10).map { i ->
            Tienda(
                id = i,
                name = "Tienda $i",
                address = "Dirección $i",
                phone = "+5698765432$i",
                latitude = -33.0 - (i * 0.1),
                longitude = -70.0 - (i * 0.1),
                description = "Descripción $i"
            )
        }
        coEvery { tiendaDao.insertAll(tiendas) } returns Unit

        // When
        tiendaDao.insertAll(tiendas)

        // Then
        coVerify(exactly = 1) { tiendaDao.insertAll(match { it.size == 10 }) }
    }

    @Test
    fun `deleteAllTiendas followed by getAllTiendas should work correctly`() = runTest {
        // Given
        coEvery { tiendaDao.deleteAllTiendas() } returns Unit
        coEvery { tiendaDao.getAllTiendas() } returns emptyList()

        // When
        tiendaDao.deleteAllTiendas()
        val result = tiendaDao.getAllTiendas()

        // Then
        coVerify(exactly = 1) { tiendaDao.deleteAllTiendas() }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `tienda phone should be stored as string`() = runTest {
        // Given
        val tienda = Tienda(
            id = 1,
            name = "Test",
            address = "Test",
            phone = "+569 87654321",
            latitude = -33.5,
            longitude = -70.6,
            description = "Test"
        )
        coEvery { tiendaDao.getAllTiendas() } returns listOf(tienda)

        // When
        val result = tiendaDao.getAllTiendas()

        // Then
        assertTrue(result[0].phone is String)
        assertTrue(result[0].phone.startsWith("+"))
    }
}
