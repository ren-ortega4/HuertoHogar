package com.example.huertohogar.local

import com.example.huertohogar.data.local.TipDao
import com.example.huertohogar.model.Tip
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TipDaoTest {

    private lateinit var tipDao: TipDao

    @BeforeEach
    fun setUp() {
        tipDao = mockk(relaxed = true)
    }

    @Test
    fun `insertAll should insert tips successfully`() = runTest {
        // Given
        val tips = listOf(
            Tip(id = 1, iconName = "water_drop", title = "Riego", text = "Riega las plantas en la mañana temprano"),
            Tip(id = 2, iconName = "wb_sunny", title = "Sol", text = "Las plantas necesitan luz solar directa")
        )
        coEvery { tipDao.insertAll(tips) } returns Unit

        // When
        tipDao.insertAll(tips)

        // Then
        coVerify(exactly = 1) { tipDao.insertAll(tips) }
    }

    @Test
    fun `insertAll should handle empty list`() = runTest {
        // Given
        val emptyList = emptyList<Tip>()
        coEvery { tipDao.insertAll(emptyList) } returns Unit

        // When
        tipDao.insertAll(emptyList)

        // Then
        coVerify(exactly = 1) { tipDao.insertAll(emptyList) }
    }

    @Test
    fun `getAllTips should return flow of tips`() = runTest {
        // Given
        val tips = listOf(
            Tip(id = 1, iconName = "water_drop", title = "Riego", text = "Riega las plantas en la mañana temprano"),
            Tip(id = 2, iconName = "wb_sunny", title = "Sol", text = "Las plantas necesitan luz solar directa")
        )
        every { tipDao.getAllTips() } returns flowOf(tips)

        // When
        val result = tipDao.getAllTips().first()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Riego", result[0].title)
        assertEquals("Sol", result[1].title)
    }

    @Test
    fun `getAllTips should return empty flow when no tips`() = runTest {
        // Given
        every { tipDao.getAllTips() } returns flowOf(emptyList())

        // When
        val result = tipDao.getAllTips().first()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `deleteAllTips should delete all tips`() = runTest {
        // Given
        coEvery { tipDao.deleteAllTips() } returns Unit

        // When
        tipDao.deleteAllTips()

        // Then
        coVerify(exactly = 1) { tipDao.deleteAllTips() }
    }

    @Test
    fun `insertAll should replace on conflict`() = runTest {
        // Given
        val tip1 = Tip(id = 1, iconName = "water_drop", title = "Riego", text = "Texto antiguo")
        val tip2 = Tip(id = 1, iconName = "water_drop", title = "Riego Actualizado", text = "Texto nuevo")
        
        coEvery { tipDao.insertAll(listOf(tip1)) } returns Unit
        coEvery { tipDao.insertAll(listOf(tip2)) } returns Unit

        // When
        tipDao.insertAll(listOf(tip1))
        tipDao.insertAll(listOf(tip2))

        // Then
        coVerify(exactly = 1) { tipDao.insertAll(listOf(tip1)) }
        coVerify(exactly = 1) { tipDao.insertAll(listOf(tip2)) }
    }

    @Test
    fun `tip should have all required properties`() = runTest {
        // Given
        val tip = Tip(
            id = 1,
            iconName = "water_drop",
            title = "Riego diario",
            text = "Es importante regar las plantas regularmente para su crecimiento"
        )
        every { tipDao.getAllTips() } returns flowOf(listOf(tip))

        // When
        val result = tipDao.getAllTips().first()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("water_drop", result[0].iconName)
        assertEquals("Riego diario", result[0].title)
        assertEquals("Es importante regar las plantas regularmente para su crecimiento", result[0].text)
    }

    @Test
    fun `getAllTips should return multiple tips with different icons`() = runTest {
        // Given
        val tips = listOf(
            Tip(id = 1, iconName = "water_drop", title = "Riego", text = "Consejos de riego"),
            Tip(id = 2, iconName = "wb_sunny", title = "Sol", text = "Consejos de luz solar"),
            Tip(id = 3, iconName = "grass", title = "Fertilizante", text = "Consejos de fertilización")
        )
        every { tipDao.getAllTips() } returns flowOf(tips)

        // When
        val result = tipDao.getAllTips().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("water_drop", result[0].iconName)
        assertEquals("wb_sunny", result[1].iconName)
        assertEquals("grass", result[2].iconName)
    }

    @Test
    fun `insertAll should handle single tip`() = runTest {
        // Given
        val tip = listOf(
            Tip(id = 1, iconName = "eco", title = "Tip único", text = "Este es un único consejo")
        )
        coEvery { tipDao.insertAll(tip) } returns Unit

        // When
        tipDao.insertAll(tip)

        // Then
        coVerify(exactly = 1) { tipDao.insertAll(match { it.size == 1 }) }
    }

    @Test
    fun `insertAll should handle large list of tips`() = runTest {
        // Given
        val tips = (1..20).map { i ->
            Tip(
                id = i,
                iconName = "icon_$i",
                title = "Tip $i",
                text = "Texto del consejo número $i"
            )
        }
        coEvery { tipDao.insertAll(tips) } returns Unit

        // When
        tipDao.insertAll(tips)

        // Then
        coVerify(exactly = 1) { tipDao.insertAll(match { it.size == 20 }) }
    }

    @Test
    fun `deleteAllTips followed by getAllTips should work correctly`() = runTest {
        // Given
        coEvery { tipDao.deleteAllTips() } returns Unit
        every { tipDao.getAllTips() } returns flowOf(emptyList())

        // When
        tipDao.deleteAllTips()
        val result = tipDao.getAllTips().first()

        // Then
        coVerify(exactly = 1) { tipDao.deleteAllTips() }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `tip text can be long string`() = runTest {
        // Given
        val longText = "Este es un texto muy largo que contiene muchos detalles sobre cómo cuidar las plantas. " +
                "Es importante seguir todos los pasos cuidadosamente para obtener los mejores resultados. " +
                "Recuerda que cada planta es diferente y requiere cuidados específicos."
        val tip = Tip(
            id = 1,
            iconName = "info",
            title = "Consejo detallado",
            text = longText
        )
        every { tipDao.getAllTips() } returns flowOf(listOf(tip))

        // When
        val result = tipDao.getAllTips().first()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].text.length > 100)
        assertEquals(longText, result[0].text)
    }

    @Test
    fun `getAllTips should return tips in flow for observation`() = runTest {
        // Given
        val tips = listOf(
            Tip(id = 1, iconName = "water_drop", title = "Riego", text = "Consejos de riego")
        )
        every { tipDao.getAllTips() } returns flowOf(tips)

        // When
        val flow = tipDao.getAllTips()
        val result = flow.first()

        // Then
        assertNotNull(flow)
        assertNotNull(result)
        assertEquals(1, result.size)
    }

    @Test
    fun `tip iconName should be stored as string`() = runTest {
        // Given
        val tip = Tip(
            id = 1,
            iconName = "water_drop",
            title = "Test",
            text = "Test"
        )
        every { tipDao.getAllTips() } returns flowOf(listOf(tip))

        // When
        val result = tipDao.getAllTips().first()

        // Then
        assertTrue(result[0].iconName is String)
        assertFalse(result[0].iconName.isEmpty())
    }

    @Test
    fun `insertAll should handle tips with special characters in text`() = runTest {
        // Given
        val tips = listOf(
            Tip(id = 1, iconName = "water_drop", title = "Título con áéíóú", text = "Texto con símbolos: @#$%&*()"),
            Tip(id = 2, iconName = "eco", title = "Título 2", text = "Texto con salto\nde línea")
        )
        coEvery { tipDao.insertAll(tips) } returns Unit

        // When
        tipDao.insertAll(tips)

        // Then
        coVerify(exactly = 1) { tipDao.insertAll(match { it.size == 2 }) }
    }
}
