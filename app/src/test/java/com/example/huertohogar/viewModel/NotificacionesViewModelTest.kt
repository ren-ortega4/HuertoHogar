package com.example.huertohogar.viewModel

import com.example.huertohogar.viewmodel.NotificacionesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificacionesViewModelTest {

    private lateinit var viewModel: NotificacionesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NotificacionesViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `notificaciones should have initial 3 notifications`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun `notificaciones should have correct initial data`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        assertEquals(1, result[0].id)
        assertEquals("Bienvenido a HuertoHogar", result[0].mensaje)
        assertFalse(result[0].leido)

        assertEquals(2, result[1].id)
        assertEquals("Nueva promoción disponible.", result[1].mensaje)
        assertFalse(result[1].leido)

        assertEquals(3, result[2].id)
        assertEquals("Tu pedido fue enviado.", result[2].mensaje)
        assertTrue(result[2].leido)
    }

    @Test
    fun `marcarComoLeida should mark notification as read`() {
        // Given
        val notificationId = 1

        // When
        viewModel.marcarComoLeida(notificationId)

        // Then
        val result = viewModel.notificaciones.value
        val notification = result.find { it.id == notificationId }
        assertNotNull(notification)
        assertTrue(notification!!.leido)
    }

    @Test
    fun `marcarComoLeida should not affect other notifications`() {
        // Given
        val notificationId = 1

        // When
        viewModel.marcarComoLeida(notificationId)

        // Then
        val result = viewModel.notificaciones.value
        assertFalse(result[1].leido) // Segunda notificación sigue sin leer
    }

    @Test
    fun `marcarComoLeida should work for already read notification`() {
        // Given
        val notificationId = 3 // Ya está leída

        // When
        viewModel.marcarComoLeida(notificationId)

        // Then
        val result = viewModel.notificaciones.value
        val notification = result.find { it.id == notificationId }
        assertTrue(notification!!.leido)
    }

    @Test
    fun `marcarComoLeida should do nothing for non-existent id`() {
        // Given
        val nonExistentId = 999
        val initialCount = viewModel.notificaciones.value.size

        // When
        viewModel.marcarComoLeida(nonExistentId)

        // Then
        val result = viewModel.notificaciones.value
        assertEquals(initialCount, result.size)
        assertEquals(3, result.size)
    }

    @Test
    fun `marcarComoLeida should work multiple times on same notification`() {
        // Given
        val notificationId = 1

        // When
        viewModel.marcarComoLeida(notificationId)
        viewModel.marcarComoLeida(notificationId)
        viewModel.marcarComoLeida(notificationId)

        // Then
        val result = viewModel.notificaciones.value
        val notification = result.find { it.id == notificationId }
        assertTrue(notification!!.leido)
    }

    @Test
    fun `marcarComoLeida should work for all notifications sequentially`() {
        // When
        viewModel.marcarComoLeida(1)
        viewModel.marcarComoLeida(2)
        viewModel.marcarComoLeida(3)

        // Then
        val result = viewModel.notificaciones.value
        assertTrue(result.all { it.leido })
    }

    @Test
    fun `notificaciones should maintain order after marking as read`() {
        // Given
        viewModel.marcarComoLeida(2)

        // When
        val result = viewModel.notificaciones.value

        // Then
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
        assertEquals(3, result[2].id)
    }

    @Test
    fun `notificaciones should not lose data after marking as read`() {
        // Given
        val originalMessage = viewModel.notificaciones.value[0].mensaje

        // When
        viewModel.marcarComoLeida(1)

        // Then
        val result = viewModel.notificaciones.value
        assertEquals(originalMessage, result[0].mensaje)
    }

    @Test
    fun `notificaciones should preserve all fields except leido`() {
        // Given
        val notification = viewModel.notificaciones.value[0]

        // When
        viewModel.marcarComoLeida(1)

        // Then
        val updatedNotification = viewModel.notificaciones.value[0]
        assertEquals(notification.id, updatedNotification.id)
        assertEquals(notification.mensaje, updatedNotification.mensaje)
        assertTrue(updatedNotification.leido)
    }

    @Test
    fun `notificaciones flow should emit updated values`() {
        // Given
        val initialValue = viewModel.notificaciones.value

        // When
        viewModel.marcarComoLeida(1)
        val updatedValue = viewModel.notificaciones.value

        // Then
        assertNotEquals(initialValue, updatedValue)
        assertFalse(initialValue[0].leido)
        assertTrue(updatedValue[0].leido)
    }

    @Test
    fun `initial state should have 2 unread notifications`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        val unreadCount = result.count { !it.leido }
        assertEquals(2, unreadCount)
    }

    @Test
    fun `initial state should have 1 read notification`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        val readCount = result.count { it.leido }
        assertEquals(1, readCount)
    }

    @Test
    fun `marcarComoLeida should update unread count`() {
        // Given
        val initialUnreadCount = viewModel.notificaciones.value.count { !it.leido }

        // When
        viewModel.marcarComoLeida(1)

        // Then
        val updatedUnreadCount = viewModel.notificaciones.value.count { !it.leido }
        assertEquals(initialUnreadCount - 1, updatedUnreadCount)
    }

    @Test
    fun `notificaciones list should not be empty`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `all notifications should have unique ids`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        val uniqueIds = result.map { it.id }.toSet()
        assertEquals(result.size, uniqueIds.size)
    }

    @Test
    fun `all notifications should have non-empty messages`() {
        // When
        val result = viewModel.notificaciones.value

        // Then
        assertTrue(result.all { it.mensaje.isNotEmpty() })
    }

    @Test
    fun `marcarComoLeida with multiple ids should update correctly`() {
        // When
        viewModel.marcarComoLeida(1)
        viewModel.marcarComoLeida(2)

        // Then
        val result = viewModel.notificaciones.value
        assertTrue(result[0].leido)
        assertTrue(result[1].leido)
        assertTrue(result[2].leido) // Ya estaba leída
    }

    @Test
    fun `notificaciones state should be consistent after multiple operations`() {
        // When
        viewModel.marcarComoLeida(1)
        viewModel.marcarComoLeida(999) // ID inexistente
        viewModel.marcarComoLeida(2)
        viewModel.marcarComoLeida(1) // Duplicado

        // Then
        val result = viewModel.notificaciones.value
        assertEquals(3, result.size)
        assertTrue(result[0].leido)
        assertTrue(result[1].leido)
        assertTrue(result[2].leido)
    }
}
