package com.example.huertohogar.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TipTest {

    @Test
    fun `crear tip con todos los parametros`() {
        // Given
        val id = 1
        val iconName = "ic_water"
        val title = "Riego adecuado"
        val text = "Riega tus plantas temprano en la mañana"

        // When
        val tip = Tip(id, iconName, title, text)

        // Then
        assertEquals(id, tip.id)
        assertEquals(iconName, tip.iconName)
        assertEquals(title, tip.title)
        assertEquals(text, tip.text)
    }

    @Test
    fun `crear tip sin especificar id debe tener valor 0 por defecto`() {
        // Given
        val iconName = "ic_sun"
        val title = "Luz solar"
        val text = "Asegúrate de que tus plantas reciban suficiente luz"

        // When
        val tip = Tip(iconName = iconName, title = title, text = text)

        // Then
        assertEquals(0, tip.id)
        assertEquals(iconName, tip.iconName)
        assertEquals(title, tip.title)
        assertEquals(text, tip.text)
    }

    @Test
    fun `crear tip con id cero`() {
        // Given
        val tip = Tip(0, "ic_leaf", "Hojas", "Cuida las hojas de tus plantas")

        // Then
        assertEquals(0, tip.id)
    }

    @Test
    fun `crear tip con id positivo`() {
        // Given
        val tip = Tip(5, "ic_flower", "Flores", "Cómo cuidar las flores")

        // Then
        assertEquals(5, tip.id)
    }

    @Test
    fun `crear tip con id negativo`() {
        // Given
        val tip = Tip(-1, "ic_soil", "Suelo", "Mantén el suelo húmedo")

        // Then
        assertEquals(-1, tip.id)
    }

    @Test
    fun `crear tip con iconName vacio`() {
        // Given
        val tip = Tip(1, "", "Título", "Texto del consejo")

        // Then
        assertEquals("", tip.iconName)
    }

    @Test
    fun `crear tip con title vacio`() {
        // Given
        val tip = Tip(1, "ic_test", "", "Texto del consejo")

        // Then
        assertEquals("", tip.title)
    }

    @Test
    fun `crear tip con text vacio`() {
        // Given
        val tip = Tip(1, "ic_test", "Título", "")

        // Then
        assertEquals("", tip.text)
    }

    @Test
    fun `crear tip con title largo`() {
        // Given
        val title = "Este es un título muy largo que contiene mucha información sobre el cuidado de plantas"
        val tip = Tip(1, "ic_info", title, "Texto")

        // Then
        assertEquals(title, tip.title)
    }

    @Test
    fun `crear tip con text largo`() {
        // Given
        val text = "Este es un texto muy largo que proporciona información detallada sobre cómo cuidar tus plantas, " +
                "incluyendo consejos sobre riego, luz solar, temperatura, humedad y otros aspectos importantes " +
                "para mantener tus plantas saludables y hermosas durante todo el año."
        val tip = Tip(1, "ic_guide", "Guía completa", text)

        // Then
        assertEquals(text, tip.text)
    }

    @Test
    fun `crear tip con caracteres especiales en iconName`() {
        // Given
        val iconName = "ic_water_drop_💧"
        val tip = Tip(1, iconName, "Riego", "Consejo de riego")

        // Then
        assertEquals(iconName, tip.iconName)
    }

    @Test
    fun `crear tip con caracteres especiales en title`() {
        // Given
        val title = "¡Atención! Cuidado 100%"
        val tip = Tip(1, "ic_alert", title, "Texto")

        // Then
        assertEquals(title, tip.title)
    }

    @Test
    fun `crear tip con caracteres especiales en text`() {
        // Given
        val text = "Temperatura: 20-25°C, Humedad: 60-70%, pH: 6.0-7.0"
        val tip = Tip(1, "ic_temp", "Condiciones", text)

        // Then
        assertEquals(text, tip.text)
    }

    @Test
    fun `crear tip con saltos de linea en text`() {
        // Given
        val text = "Paso 1: Regar\nPaso 2: Esperar\nPaso 3: Observar"
        val tip = Tip(1, "ic_steps", "Pasos", text)

        // Then
        assertEquals(text, tip.text)
    }

    @Test
    fun `dos tips con los mismos valores son iguales`() {
        // Given
        val tip1 = Tip(1, "ic_water", "Riego", "Riega tus plantas")
        val tip2 = Tip(1, "ic_water", "Riego", "Riega tus plantas")

        // Then
        assertEquals(tip1, tip2)
    }

    @Test
    fun `dos tips con diferentes ids no son iguales`() {
        // Given
        val tip1 = Tip(1, "ic_water", "Riego", "Riega tus plantas")
        val tip2 = Tip(2, "ic_water", "Riego", "Riega tus plantas")

        // Then
        assertNotEquals(tip1, tip2)
    }

    @Test
    fun `dos tips con diferentes iconNames no son iguales`() {
        // Given
        val tip1 = Tip(1, "ic_water", "Riego", "Riega tus plantas")
        val tip2 = Tip(1, "ic_sun", "Riego", "Riega tus plantas")

        // Then
        assertNotEquals(tip1, tip2)
    }

    @Test
    fun `dos tips con diferentes titles no son iguales`() {
        // Given
        val tip1 = Tip(1, "ic_water", "Riego", "Riega tus plantas")
        val tip2 = Tip(1, "ic_water", "Luz", "Riega tus plantas")

        // Then
        assertNotEquals(tip1, tip2)
    }

    @Test
    fun `dos tips con diferentes texts no son iguales`() {
        // Given
        val tip1 = Tip(1, "ic_water", "Riego", "Riega tus plantas")
        val tip2 = Tip(1, "ic_water", "Riego", "Cuida tus plantas")

        // Then
        assertNotEquals(tip1, tip2)
    }

    @Test
    fun `dos tips iguales tienen el mismo hashCode`() {
        // Given
        val tip1 = Tip(1, "ic_water", "Riego", "Riega tus plantas")
        val tip2 = Tip(1, "ic_water", "Riego", "Riega tus plantas")

        // Then
        assertEquals(tip1.hashCode(), tip2.hashCode())
    }

    @Test
    fun `toString debe contener todos los valores`() {
        // Given
        val tip = Tip(1, "ic_water", "Riego", "Riega tus plantas")

        // When
        val resultado = tip.toString()

        // Then
        assertTrue(resultado.contains("1"))
        assertTrue(resultado.contains("ic_water"))
        assertTrue(resultado.contains("Riego"))
        assertTrue(resultado.contains("Riega tus plantas"))
    }

    @Test
    fun `copy debe crear una nueva instancia con los mismos valores`() {
        // Given
        val original = Tip(1, "ic_sun", "Luz solar", "Proporciona luz adecuada")

        // When
        val copia = original.copy()

        // Then
        assertEquals(original, copia)
        assertNotSame(original, copia)
    }

    @Test
    fun `copy con cambio de id`() {
        // Given
        val original = Tip(1, "ic_sun", "Luz solar", "Proporciona luz adecuada")

        // When
        val modificado = original.copy(id = 99)

        // Then
        assertEquals(99, modificado.id)
        assertEquals(original.iconName, modificado.iconName)
        assertEquals(original.title, modificado.title)
        assertEquals(original.text, modificado.text)
    }

    @Test
    fun `copy con cambio de iconName`() {
        // Given
        val original = Tip(1, "ic_sun", "Luz solar", "Proporciona luz adecuada")

        // When
        val modificado = original.copy(iconName = "ic_moon")

        // Then
        assertEquals(original.id, modificado.id)
        assertEquals("ic_moon", modificado.iconName)
        assertEquals(original.title, modificado.title)
        assertEquals(original.text, modificado.text)
    }

    @Test
    fun `copy con cambio de title`() {
        // Given
        val original = Tip(1, "ic_sun", "Luz solar", "Proporciona luz adecuada")

        // When
        val modificado = original.copy(title = "Nuevo título")

        // Then
        assertEquals(original.id, modificado.id)
        assertEquals(original.iconName, modificado.iconName)
        assertEquals("Nuevo título", modificado.title)
        assertEquals(original.text, modificado.text)
    }

    @Test
    fun `copy con cambio de text`() {
        // Given
        val original = Tip(1, "ic_sun", "Luz solar", "Proporciona luz adecuada")

        // When
        val modificado = original.copy(text = "Nuevo texto de consejo")

        // Then
        assertEquals(original.id, modificado.id)
        assertEquals(original.iconName, modificado.iconName)
        assertEquals(original.title, modificado.title)
        assertEquals("Nuevo texto de consejo", modificado.text)
    }

    @Test
    fun `copy con cambio de todos los parametros`() {
        // Given
        val original = Tip(1, "ic_sun", "Luz solar", "Proporciona luz adecuada")

        // When
        val modificado = original.copy(
            id = 50,
            iconName = "ic_water_new",
            title = "Riego inteligente",
            text = "Utiliza sensores para optimizar el riego"
        )

        // Then
        assertEquals(50, modificado.id)
        assertEquals("ic_water_new", modificado.iconName)
        assertEquals("Riego inteligente", modificado.title)
        assertEquals("Utiliza sensores para optimizar el riego", modificado.text)
    }

    @Test
    fun `crear tip con iconName con guiones bajos`() {
        // Given
        val iconName = "ic_water_drop_outline"
        val tip = Tip(1, iconName, "Título", "Texto")

        // Then
        assertEquals(iconName, tip.iconName)
    }

    @Test
    fun `crear tip con iconName en mayusculas`() {
        // Given
        val iconName = "IC_WATER"
        val tip = Tip(1, iconName, "Título", "Texto")

        // Then
        assertEquals(iconName, tip.iconName)
    }

    @Test
    fun `crear tip con title con numeros`() {
        // Given
        val title = "5 consejos para el riego"
        val tip = Tip(1, "ic_list", title, "Texto")

        // Then
        assertEquals(title, tip.title)
    }

    @Test
    fun `crear tip con text con URLs`() {
        // Given
        val text = "Más información en https://ejemplo.com/plantas"
        val tip = Tip(1, "ic_link", "Recursos", text)

        // Then
        assertEquals(text, tip.text)
    }

    @Test
    fun `crear tip con text con tabulaciones`() {
        // Given
        val text = "Opción 1:\tRegar\nOpción 2:\tPodar"
        val tip = Tip(1, "ic_options", "Opciones", text)

        // Then
        assertEquals(text, tip.text)
    }

    @Test
    fun `crear tip con todos los campos vacios excepto id`() {
        // Given
        val tip = Tip(1, "", "", "")

        // Then
        assertEquals(1, tip.id)
        assertEquals("", tip.iconName)
        assertEquals("", tip.title)
        assertEquals("", tip.text)
    }
}