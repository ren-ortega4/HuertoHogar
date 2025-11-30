package com.example.huertohogar.data.local

import com.example.huertohogar.model.Tip
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("TipDao Tests")
class TipDaoTest {
    
    private lateinit var tipDao: TipDao

    @BeforeEach
    fun setup() {
        tipDao = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSampleTip(
        id: Int = 1,
        iconName: String = "icon_water",
        title: String = "Riego adecuado",
        text: String = "Riega tus plantas temprano en la mañana para evitar la evaporación"
    ) = Tip(
        id = id,
        iconName = iconName,
        title = title,
        text = text
    )

    @Nested
    @DisplayName("insertAll Tests")
    inner class InsertAllTests {

        @Test
        @DisplayName("insertAll should insert single tip")
        fun `insertAll should insert single tip`() = runBlocking {
            val tip = createSampleTip()
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(listOf(tip)) }
        }

        @Test
        @DisplayName("insertAll should insert multiple tips")
        fun `insertAll should insert multiple tips`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, title = "Tip 1"),
                createSampleTip(id = 2, title = "Tip 2"),
                createSampleTip(id = 3, title = "Tip 3")
            )
            coEvery { tipDao.insertAll(tips) } just Runs

            tipDao.insertAll(tips)

            coVerify { tipDao.insertAll(tips) }
        }

        @Test
        @DisplayName("insertAll should insert empty list")
        fun `insertAll should insert empty list`() = runBlocking {
            val emptyList = emptyList<Tip>()
            coEvery { tipDao.insertAll(emptyList) } just Runs

            tipDao.insertAll(emptyList)

            coVerify { tipDao.insertAll(emptyList) }
        }

        @Test
        @DisplayName("insertAll should replace on conflict")
        fun `insertAll should replace on conflict`() = runBlocking {
            val tip1 = createSampleTip(id = 1, title = "Original")
            val tip2 = createSampleTip(id = 1, title = "Actualizado")
            
            coEvery { tipDao.insertAll(any()) } just Runs

            tipDao.insertAll(listOf(tip1))
            tipDao.insertAll(listOf(tip2))

            coVerify(exactly = 2) { tipDao.insertAll(any()) }
        }

        @Test
        @DisplayName("insertAll should handle tips with all fields")
        fun `insertAll should handle tips with all fields`() = runBlocking {
            val tips = listOf(
                createSampleTip(
                    id = 1,
                    iconName = "icon_sun",
                    title = "Luz solar",
                    text = "La mayoría de las plantas necesitan al menos 6 horas de luz solar directa"
                ),
                createSampleTip(
                    id = 2,
                    iconName = "icon_fertilizer",
                    title = "Fertilización",
                    text = "Fertiliza tus plantas cada 2-3 semanas durante la temporada de crecimiento"
                )
            )
            coEvery { tipDao.insertAll(tips) } just Runs

            tipDao.insertAll(tips)

            coVerify { tipDao.insertAll(match { it.size == 2 }) }
        }

        @Test
        @DisplayName("insertAll should handle tips with special characters")
        fun `insertAll should handle tips with special characters`() = runBlocking {
            val tip = createSampleTip(
                title = "Cuidado Orgánico 100%",
                text = "Usa compost & abono orgánico para mejorar el suelo"
            )
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().title.contains("Orgánico") }) }
        }

        @Test
        @DisplayName("insertAll should handle tips with long text")
        fun `insertAll should handle tips with long text`() = runBlocking {
            val longText = "Este es un consejo muy largo que contiene mucha información útil. ".repeat(10)
            val tip = createSampleTip(text = longText)
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().text.length > 100 }) }
        }

        @Test
        @DisplayName("insertAll should handle tips with empty title")
        fun `insertAll should handle tips with empty title`() = runBlocking {
            val tip = createSampleTip(title = "")
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().title.isEmpty() }) }
        }

        @Test
        @DisplayName("insertAll should handle tips with auto-generated id")
        fun `insertAll should handle tips with auto-generated id`() = runBlocking {
            val tip = createSampleTip(id = 0)
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().id == 0 }) }
        }

        @Test
        @DisplayName("insertAll should handle large batch of tips")
        fun `insertAll should handle large batch of tips`() = runBlocking {
            val tips = (1..50).map { index ->
                createSampleTip(id = index, title = "Tip $index")
            }
            coEvery { tipDao.insertAll(tips) } just Runs

            tipDao.insertAll(tips)

            coVerify { tipDao.insertAll(match { it.size == 50 }) }
        }
    }

    @Nested
    @DisplayName("getAllTips Tests")
    inner class GetAllTipsTests {

        @Test
        @DisplayName("getAllTips should return Flow of all tips")
        fun `getAllTips should return Flow of all tips`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, title = "Tip 1"),
                createSampleTip(id = 2, title = "Tip 2"),
                createSampleTip(id = 3, title = "Tip 3")
            )
            every { tipDao.getAllTips() } returns flowOf(tips)

            val result = tipDao.getAllTips().first()

            result shouldHaveSize 3
            result shouldBe tips
        }

        @Test
        @DisplayName("getAllTips should return empty list when no tips")
        fun `getAllTips should return empty list when no tips`() = runBlocking {
            every { tipDao.getAllTips() } returns flowOf(emptyList())

            val result = tipDao.getAllTips().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("getAllTips should return Flow that can be collected")
        fun `getAllTips should return Flow that can be collected`() = runBlocking {
            val tips = listOf(createSampleTip())
            every { tipDao.getAllTips() } returns flowOf(tips)

            val flow = tipDao.getAllTips()

            flow.shouldNotBeNull()
            val result = flow.first()
            result shouldHaveSize 1
        }

        @Test
        @DisplayName("getAllTips should preserve tip data")
        fun `getAllTips should preserve tip data`() = runBlocking {
            val tip = createSampleTip(
                id = 10,
                iconName = "icon_custom",
                title = "Consejo Especial",
                text = "Este es un consejo muy específico y detallado"
            )
            every { tipDao.getAllTips() } returns flowOf(listOf(tip))

            val result = tipDao.getAllTips().first()

            result.first().id shouldBe 10
            result.first().iconName shouldBe "icon_custom"
            result.first().title shouldBe "Consejo Especial"
            result.first().text shouldBe "Este es un consejo muy específico y detallado"
        }

        @Test
        @DisplayName("getAllTips should return all inserted tips")
        fun `getAllTips should return all inserted tips`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, iconName = "icon_water", title = "Riego"),
                createSampleTip(id = 2, iconName = "icon_sun", title = "Luz"),
                createSampleTip(id = 3, iconName = "icon_soil", title = "Suelo"),
                createSampleTip(id = 4, iconName = "icon_fertilizer", title = "Fertilizante"),
                createSampleTip(id = 5, iconName = "icon_pest", title = "Plagas")
            )
            every { tipDao.getAllTips() } returns flowOf(tips)

            val result = tipDao.getAllTips().first()

            result shouldHaveSize 5
            result shouldContain tips[0]
            result shouldContain tips[4]
        }

        @Test
        @DisplayName("getAllTips should maintain order of tips")
        fun `getAllTips should maintain order of tips`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, title = "Primero"),
                createSampleTip(id = 2, title = "Segundo"),
                createSampleTip(id = 3, title = "Tercero")
            )
            every { tipDao.getAllTips() } returns flowOf(tips)

            val result = tipDao.getAllTips().first()

            result[0].title shouldBe "Primero"
            result[1].title shouldBe "Segundo"
            result[2].title shouldBe "Tercero"
        }

        @Test
        @DisplayName("getAllTips should return tips with different icon names")
        fun `getAllTips should return tips with different icon names`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, iconName = "icon_a"),
                createSampleTip(id = 2, iconName = "icon_b"),
                createSampleTip(id = 3, iconName = "icon_c")
            )
            every { tipDao.getAllTips() } returns flowOf(tips)

            val result = tipDao.getAllTips().first()

            result.map { it.iconName }.distinct() shouldHaveSize 3
        }

        @Test
        @DisplayName("getAllTips should handle tips with same title but different ids")
        fun `getAllTips should handle tips with same title but different ids`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, title = "Mismo Título"),
                createSampleTip(id = 2, title = "Mismo Título")
            )
            every { tipDao.getAllTips() } returns flowOf(tips)

            val result = tipDao.getAllTips().first()

            result shouldHaveSize 2
            result[0].id shouldNotBe result[1].id
        }

        @Test
        @DisplayName("getAllTips should return consistent results")
        fun `getAllTips should return consistent results`() = runBlocking {
            val tips = listOf(createSampleTip(id = 1), createSampleTip(id = 2))
            every { tipDao.getAllTips() } returns flowOf(tips)

            val result1 = tipDao.getAllTips().first()
            val result2 = tipDao.getAllTips().first()

            result1 shouldBe result2
        }
    }

    @Nested
    @DisplayName("deleteAllTips Tests")
    inner class DeleteAllTipsTests {

        @Test
        @DisplayName("deleteAllTips should delete all tips")
        fun `deleteAllTips should delete all tips`() = runBlocking {
            coEvery { tipDao.deleteAllTips() } just Runs

            tipDao.deleteAllTips()

            coVerify { tipDao.deleteAllTips() }
        }

        @Test
        @DisplayName("deleteAllTips should be callable multiple times")
        fun `deleteAllTips should be callable multiple times`() = runBlocking {
            coEvery { tipDao.deleteAllTips() } just Runs

            tipDao.deleteAllTips()
            tipDao.deleteAllTips()
            tipDao.deleteAllTips()

            coVerify(exactly = 3) { tipDao.deleteAllTips() }
        }

        @Test
        @DisplayName("deleteAllTips should work on empty database")
        fun `deleteAllTips should work on empty database`() = runBlocking {
            coEvery { tipDao.deleteAllTips() } just Runs
            every { tipDao.getAllTips() } returns flowOf(emptyList())

            tipDao.deleteAllTips()
            val result = tipDao.getAllTips().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("deleteAllTips should remove all tips after insertion")
        fun `deleteAllTips should remove all tips after insertion`() = runBlocking {
            val tips = listOf(createSampleTip(id = 1), createSampleTip(id = 2))
            coEvery { tipDao.insertAll(tips) } just Runs
            coEvery { tipDao.deleteAllTips() } just Runs
            every { tipDao.getAllTips() } returnsMany listOf(
                flowOf(tips),
                flowOf(emptyList())
            )

            tipDao.insertAll(tips)
            val beforeDelete = tipDao.getAllTips().first()
            tipDao.deleteAllTips()
            val afterDelete = tipDao.getAllTips().first()

            beforeDelete shouldHaveSize 2
            afterDelete.shouldBeEmpty()
        }

        @Test
        @DisplayName("deleteAllTips should not throw exception")
        fun `deleteAllTips should not throw exception`() = runBlocking {
            coEvery { tipDao.deleteAllTips() } just Runs

            var exceptionThrown = false
            try {
                tipDao.deleteAllTips()
            } catch (e: Exception) {
                exceptionThrown = true
            }

            exceptionThrown shouldBe false
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    inner class IntegrationScenariosTests {

        @Test
        @DisplayName("should insert tips and then retrieve all")
        fun `should insert tips and then retrieve all`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, title = "Tip 1"),
                createSampleTip(id = 2, title = "Tip 2")
            )
            coEvery { tipDao.insertAll(tips) } just Runs
            every { tipDao.getAllTips() } returns flowOf(tips)

            tipDao.insertAll(tips)
            val result = tipDao.getAllTips().first()

            result shouldHaveSize 2
            result shouldBe tips
        }

        @Test
        @DisplayName("should insert, delete all, and verify empty")
        fun `should insert, delete all, and verify empty`() = runBlocking {
            val tips = listOf(createSampleTip(id = 1), createSampleTip(id = 2))
            coEvery { tipDao.insertAll(tips) } just Runs
            coEvery { tipDao.deleteAllTips() } just Runs
            every { tipDao.getAllTips() } returnsMany listOf(
                flowOf(tips),
                flowOf(emptyList())
            )

            tipDao.insertAll(tips)
            tipDao.deleteAllTips()
            val result = tipDao.getAllTips().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("should replace tips on conflict and retrieve updated")
        fun `should replace tips on conflict and retrieve updated`() = runBlocking {
            val originalTips = listOf(createSampleTip(id = 1, title = "Original"))
            val updatedTips = listOf(createSampleTip(id = 1, title = "Actualizado"))
            
            coEvery { tipDao.insertAll(originalTips) } just Runs
            coEvery { tipDao.insertAll(updatedTips) } just Runs
            every { tipDao.getAllTips() } returnsMany listOf(
                flowOf(originalTips),
                flowOf(updatedTips)
            )

            tipDao.insertAll(originalTips)
            val beforeUpdate = tipDao.getAllTips().first()
            tipDao.insertAll(updatedTips)
            val afterUpdate = tipDao.getAllTips().first()

            beforeUpdate.first().title shouldBe "Original"
            afterUpdate.first().title shouldBe "Actualizado"
        }

        @Test
        @DisplayName("should handle full CRUD cycle")
        fun `should handle full CRUD cycle`() = runBlocking {
            // Create
            val tips = listOf(createSampleTip(id = 1), createSampleTip(id = 2))
            coEvery { tipDao.insertAll(tips) } just Runs
            every { tipDao.getAllTips() } returnsMany listOf(
                flowOf(emptyList()),
                flowOf(tips),
                flowOf(emptyList())
            )

            // Read (empty)
            val emptyResult = tipDao.getAllTips().first()
            emptyResult.shouldBeEmpty()

            // Create
            tipDao.insertAll(tips)
            val insertResult = tipDao.getAllTips().first()
            insertResult shouldHaveSize 2

            // Delete
            coEvery { tipDao.deleteAllTips() } just Runs
            tipDao.deleteAllTips()
            val deleteResult = tipDao.getAllTips().first()
            deleteResult.shouldBeEmpty()
        }

        @Test
        @DisplayName("should insert empty list and verify no change")
        fun `should insert empty list and verify no change`() = runBlocking {
            coEvery { tipDao.insertAll(emptyList()) } just Runs
            every { tipDao.getAllTips() } returns flowOf(emptyList())

            tipDao.insertAll(emptyList())
            val result = tipDao.getAllTips().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("should handle multiple insertions without deletion")
        fun `should handle multiple insertions without deletion`() = runBlocking {
            val tips1 = listOf(createSampleTip(id = 1))
            val tips2 = listOf(createSampleTip(id = 2))
            val tips3 = listOf(createSampleTip(id = 3))
            
            coEvery { tipDao.insertAll(any()) } just Runs
            every { tipDao.getAllTips() } returns flowOf(tips1 + tips2 + tips3)

            tipDao.insertAll(tips1)
            tipDao.insertAll(tips2)
            tipDao.insertAll(tips3)
            val result = tipDao.getAllTips().first()

            result shouldHaveSize 3
        }
    }

    @Nested
    @DisplayName("Edge Cases and Data Validation")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("should handle tip with very long title")
        fun `should handle tip with very long title`() = runBlocking {
            val longTitle = "Este es un título extremadamente largo que podría causar problemas ".repeat(5)
            val tip = createSampleTip(title = longTitle)
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().title.length > 100 }) }
        }

        @Test
        @DisplayName("should handle tip with multiline text")
        fun `should handle tip with multiline text`() = runBlocking {
            val multilineText = """
                Primera línea del consejo
                Segunda línea del consejo
                Tercera línea del consejo
            """.trimIndent()
            val tip = createSampleTip(text = multilineText)
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().text.contains("\n") }) }
        }

        @Test
        @DisplayName("should handle tip with unicode characters")
        fun `should handle tip with unicode characters`() = runBlocking {
            val tip = createSampleTip(
                title = "Emojis y símbolos 🌱🌿🌳",
                text = "Usa símbolos especiales: ☀️ 💧 ℃"
            )
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(any()) }
        }

        @Test
        @DisplayName("should handle tip with numbers in text")
        fun `should handle tip with numbers in text`() = runBlocking {
            val tip = createSampleTip(
                title = "Riego 3 veces por semana",
                text = "Riega 2-3 veces por semana con 5 litros de agua"
            )
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { 
                it.first().title.contains("3") && it.first().text.contains("5")
            }) }
        }

        @Test
        @DisplayName("should handle tip with empty text")
        fun `should handle tip with empty text`() = runBlocking {
            val tip = createSampleTip(text = "")
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().text.isEmpty() }) }
        }

        @Test
        @DisplayName("should handle tips with same icon name")
        fun `should handle tips with same icon name`() = runBlocking {
            val tips = listOf(
                createSampleTip(id = 1, iconName = "icon_same", title = "Tip 1"),
                createSampleTip(id = 2, iconName = "icon_same", title = "Tip 2")
            )
            coEvery { tipDao.insertAll(tips) } just Runs

            tipDao.insertAll(tips)

            coVerify { tipDao.insertAll(match { 
                it.all { tip -> tip.iconName == "icon_same" }
            }) }
        }

        @Test
        @DisplayName("should handle tip with maximum integer id")
        fun `should handle tip with maximum integer id`() = runBlocking {
            val tip = createSampleTip(id = Int.MAX_VALUE)
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().id == Int.MAX_VALUE }) }
        }

        @Test
        @DisplayName("should handle tip with URL in text")
        fun `should handle tip with URL in text`() = runBlocking {
            val tip = createSampleTip(
                text = "Visita https://www.ejemplo.com/plantas para más información"
            )
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().text.contains("https://") }) }
        }

        @Test
        @DisplayName("should handle tip with HTML-like characters")
        fun `should handle tip with HTML-like characters`() = runBlocking {
            val tip = createSampleTip(
                text = "Temperatura ideal: <20°C && >10°C"
            )
            coEvery { tipDao.insertAll(listOf(tip)) } just Runs

            tipDao.insertAll(listOf(tip))

            coVerify { tipDao.insertAll(match { it.first().text.contains("<") }) }
        }
    }

    @Nested
    @DisplayName("Flow Behavior Tests")
    inner class FlowBehaviorTests {

        @Test
        @DisplayName("getAllTips Flow should emit latest data")
        fun `getAllTips Flow should emit latest data`() = runBlocking {
            val tips = listOf(createSampleTip(id = 1))
            every { tipDao.getAllTips() } returns flowOf(tips)

            val flow = tipDao.getAllTips()
            val result = flow.first()

            result shouldBe tips
        }

        @Test
        @DisplayName("getAllTips Flow should be reusable")
        fun `getAllTips Flow should be reusable`() = runBlocking {
            val tips = listOf(createSampleTip(id = 1))
            every { tipDao.getAllTips() } returns flowOf(tips)

            val flow = tipDao.getAllTips()
            val result1 = flow.first()
            val result2 = flow.first()

            result1 shouldBe result2
        }

        @Test
        @DisplayName("getAllTips should work with collect")
        fun `getAllTips should work with collect`() = runBlocking {
            val tips = listOf(createSampleTip(id = 1), createSampleTip(id = 2))
            every { tipDao.getAllTips() } returns flowOf(tips)

            var collectedTips: List<Tip>? = null
            tipDao.getAllTips().collect { tips ->
                collectedTips = tips
            }

            collectedTips shouldBe tips
        }
    }
}