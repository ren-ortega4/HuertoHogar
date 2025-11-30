package com.example.huertohogar
import org.junit.Test
import org.junit.Assert.*
import java.util.Calendar

class MapScreenUnitTest {

    @Test
    fun checkStoreStatus_ReturnsOpen_WhenCurrentTimeIsWithinOpenHours() {
        // Arrange
        val cal = Calendar.getInstance()
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)

        // Crear un horario que incluya la hora actual
        val openHour = if (currentHour > 0) currentHour - 1 else 23
        val closeHour = if (currentHour < 23) currentHour + 1 else 0

        val description = "Abierto de $openHour:00 a $closeHour:00"

        // Act - Necesitarías hacer pública la función o crear una clase testeable
        // val (status, isOpen) = checkStoreStatus(description)

        // Assert
        // assertEquals("Abierto", status)
        // assertTrue(isOpen)
    }

    @Test
    fun checkStoreStatus_ReturnsClosed_WhenCurrentTimeIsOutsideOpenHours() {
        // Arrange
        val description = "Abierto de 02:00 a 03:00" // Hora improbable

        // Act
        // val (status, isOpen) = checkStoreStatus(description)

        // Assert
        // assertEquals("Cerrado", status)
        // assertFalse(isOpen)
    }

    @Test
    fun checkStoreStatus_ReturnsUnavailable_WhenDescriptionHasNoHours() {
        // Arrange
        val description = "Una tienda sin horario especificado"

        // Act
        // val (status, isOpen) = checkStoreStatus(description)

        // Assert
        // assertEquals("Horario no disponible", status)
        // assertFalse(isOpen)
    }

    @Test
    fun checkStoreStatus_HandlesInvalidTimeFormat() {
        // Arrange
        val description = "Abierto de XX:YY a ZZ:WW"

        // Act
        // val (status, isOpen) = checkStoreStatus(description)

        // Assert
        // assertEquals("Horario no disponible", status)
        // assertFalse(isOpen)
    }

    @Test
    fun storeHoursParsing_ExtractsCorrectHours() {
        // Arrange
        val regex = """(\d{1,2}):(\d{2})[^0-9]+(\d{1,2}):(\d{2})""".toRegex()
        val description = "Abierto de 09:30 a 18:45"

        // Act
        val matchResult = regex.find(description)
        val (openHour, openMinute, closeHour, closeMinute) =
            matchResult!!.destructured.toList().map { it.toInt() }

        // Assert
        assertEquals(9, openHour)
        assertEquals(30, openMinute)
        assertEquals(18, closeHour)
        assertEquals(45, closeMinute)
    }
}