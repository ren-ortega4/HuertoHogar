package com.example.huertohogar.viewmodel

import com.example.huertohogar.model.Notificacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

/**
 * Pruebas unitarias para NotificacionesViewModel
 * 
 * Cubre:
 * - Estado inicial de notificaciones
 * - Marcar notificaciones como leídas
 * - Gestión de lista de notificaciones
 * - Inmutabilidad del estado
 */
@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("NotificacionesViewModel Tests")
class NotificacionesViewModelTest {

    private lateinit var viewModel: NotificacionesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NotificacionesViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ==================== PRUEBAS DE ESTADO INICIAL ====================

    @Nested
    @DisplayName("Estado Inicial Tests")
    inner class EstadoInicialTests {

        @Test
        fun `viewModel inicializa con lista de notificaciones predeterminadas`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()

            // Then
            assertNotNull(notificaciones, "La lista de notificaciones no debe ser null")
            assertEquals(3, notificaciones.size, "Debe haber 3 notificaciones iniciales")
        }

        @Test
        fun `primera notificación tiene el contenido correcto`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()
            val primeraNotificacion = notificaciones[0]

            // Then
            assertEquals(1, primeraNotificacion.id, "El ID debe ser 1")
            assertEquals("Bienvenido a HuertoHogar", primeraNotificacion.mensaje, "El mensaje debe coincidir")
            assertFalse(primeraNotificacion.leido, "La notificación debe estar no leída inicialmente")
        }

        @Test
        fun `segunda notificación tiene el contenido correcto`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()
            val segundaNotificacion = notificaciones[1]

            // Then
            assertEquals(2, segundaNotificacion.id, "El ID debe ser 2")
            assertEquals("Nueva promoción disponible.", segundaNotificacion.mensaje, "El mensaje debe coincidir")
            assertFalse(segundaNotificacion.leido, "La notificación debe estar no leída inicialmente")
        }

        @Test
        fun `tercera notificación está marcada como leída inicialmente`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()
            val terceraNotificacion = notificaciones[2]

            // Then
            assertEquals(3, terceraNotificacion.id, "El ID debe ser 3")
            assertEquals("Tu pedido fue enviado.", terceraNotificacion.mensaje, "El mensaje debe coincidir")
            assertTrue(terceraNotificacion.leido, "La notificación debe estar leída inicialmente")
        }

        @Test
        fun `cuenta correcta de notificaciones leídas y no leídas`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()
            val noLeidas = notificaciones.count { !it.leido }
            val leidas = notificaciones.count { it.leido }

            // Then
            assertEquals(2, noLeidas, "Debe haber 2 notificaciones no leídas")
            assertEquals(1, leidas, "Debe haber 1 notificación leída")
        }
    }

    // ==================== PRUEBAS DE MARCAR COMO LEÍDA ====================

    @Nested
    @DisplayName("Marcar Como Leída Tests")
    inner class MarcarComoLeidaTests {

        @Test
        fun `marcarComoLeida actualiza el estado de una notificación no leída`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()
            val notificacionAMarcar = notificacionesInicial.first { it.id == 1 }
            assertFalse(notificacionAMarcar.leido, "La notificación debe estar no leída inicialmente")

            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            val notificacionActualizada = notificacionesActualizada.first { it.id == 1 }
            assertTrue(notificacionActualizada.leido, "La notificación debe estar marcada como leída")
        }

        @Test
        fun `marcarComoLeida no afecta a otras notificaciones`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()
            val estadoOriginal = notificacionesInicial.map { it.id to it.leido }.toMap()

            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            
            // Verificar que la notificación 1 cambió
            assertTrue(notificacionesActualizada.first { it.id == 1 }.leido, "Notificación 1 debe estar leída")
            
            // Verificar que las demás no cambiaron
            assertEquals(estadoOriginal[2], notificacionesActualizada.first { it.id == 2 }.leido, 
                "Notificación 2 no debe cambiar")
            assertEquals(estadoOriginal[3], notificacionesActualizada.first { it.id == 3 }.leido, 
                "Notificación 3 no debe cambiar")
        }

        @Test
        fun `marcarComoLeida con ID inexistente no afecta la lista`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()

            // When
            viewModel.marcarComoLeida(999) // ID que no existe
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            assertEquals(notificacionesInicial.size, notificacionesActualizada.size, 
                "El tamaño de la lista no debe cambiar")
            
            notificacionesInicial.forEachIndexed { index, notificacion ->
                assertEquals(notificacion.leido, notificacionesActualizada[index].leido,
                    "El estado leído de la notificación ${notificacion.id} no debe cambiar")
            }
        }

        @Test
        fun `marcarComoLeida sobre una notificación ya leída mantiene el estado`() = runTest {
            // Given - La notificación 3 ya está leída
            val notificacionesInicial = viewModel.notificaciones.first()
            val notificacion3 = notificacionesInicial.first { it.id == 3 }
            assertTrue(notificacion3.leido, "La notificación 3 debe estar leída inicialmente")

            // When
            viewModel.marcarComoLeida(3)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            val notificacion3Actualizada = notificacionesActualizada.first { it.id == 3 }
            assertTrue(notificacion3Actualizada.leido, "La notificación 3 debe seguir leída")
        }

        @Test
        fun `marcar múltiples notificaciones como leídas`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()
            val noLeidasInicial = notificacionesInicial.count { !it.leido }
            assertEquals(2, noLeidasInicial, "Debe haber 2 notificaciones no leídas inicialmente")

            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()
            viewModel.marcarComoLeida(2)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            val noLeidasFinal = notificacionesActualizada.count { !it.leido }
            assertEquals(0, noLeidasFinal, "No debe haber notificaciones no leídas")
            assertTrue(notificacionesActualizada.all { it.leido }, 
                "Todas las notificaciones deben estar leídas")
        }
    }

    // ==================== PRUEBAS DE GESTIÓN DE LISTA ====================

    @Nested
    @DisplayName("Gestión de Lista Tests")
    inner class GestionListaTests {

        @Test
        fun `la lista mantiene el orden correcto de notificaciones`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()

            // Then
            assertEquals(1, notificaciones[0].id, "Primera notificación debe tener ID 1")
            assertEquals(2, notificaciones[1].id, "Segunda notificación debe tener ID 2")
            assertEquals(3, notificaciones[2].id, "Tercera notificación debe tener ID 3")
        }

        @Test
        fun `marcar como leída mantiene el orden de la lista`() = runTest {
            // Given
            val ordenInicial = viewModel.notificaciones.first().map { it.id }

            // When
            viewModel.marcarComoLeida(2)
            advanceUntilIdle()

            // Then
            val ordenFinal = viewModel.notificaciones.first().map { it.id }
            assertEquals(ordenInicial, ordenFinal, "El orden de las notificaciones debe mantenerse")
        }

        @Test
        fun `todas las notificaciones tienen IDs únicos`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()
            val ids = notificaciones.map { it.id }

            // Then
            assertEquals(ids.size, ids.toSet().size, "Todos los IDs deben ser únicos")
        }

        @Test
        fun `todas las notificaciones tienen mensajes no vacíos`() = runTest {
            // When
            val notificaciones = viewModel.notificaciones.first()

            // Then
            assertTrue(notificaciones.all { it.mensaje.isNotBlank() }, 
                "Todas las notificaciones deben tener mensajes no vacíos")
        }
    }

    // ==================== PRUEBAS DE INMUTABILIDAD ====================

    @Nested
    @DisplayName("Inmutabilidad Tests")
    inner class InmutabilidadTests {

        @Test
        fun `marcarComoLeida crea una nueva lista en lugar de modificar la existente`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()
            val referenciaInicial = notificacionesInicial

            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            assertNotSame(referenciaInicial, notificacionesActualizada, 
                "Debe crear una nueva lista, no modificar la existente")
        }

        @Test
        fun `los objetos Notificacion son inmutables con copy`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()
            val notificacionOriginal = notificacionesInicial.first { it.id == 1 }

            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            val notificacionActualizada = notificacionesActualizada.first { it.id == 1 }
            
            // Verificar que es una nueva instancia
            assertNotSame(notificacionOriginal, notificacionActualizada, 
                "Debe crear una nueva instancia de Notificacion")
            
            // Verificar que los campos inmutables se mantienen
            assertEquals(notificacionOriginal.id, notificacionActualizada.id, 
                "El ID debe mantenerse")
            assertEquals(notificacionOriginal.mensaje, notificacionActualizada.mensaje, 
                "El mensaje debe mantenerse")
        }
    }

    // ==================== PRUEBAS DE EDGE CASES ====================

    @Nested
    @DisplayName("Edge Cases Tests")
    inner class EdgeCasesTests {

        @Test
        fun `marcarComoLeida con ID 0 no afecta ninguna notificación`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()

            // When
            viewModel.marcarComoLeida(0)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            assertEquals(notificacionesInicial.size, notificacionesActualizada.size)
            notificacionesInicial.forEachIndexed { index, notif ->
                assertEquals(notif.leido, notificacionesActualizada[index].leido)
            }
        }

        @Test
        fun `marcarComoLeida con ID negativo no afecta ninguna notificación`() = runTest {
            // Given
            val notificacionesInicial = viewModel.notificaciones.first()

            // When
            viewModel.marcarComoLeida(-1)
            advanceUntilIdle()

            // Then
            val notificacionesActualizada = viewModel.notificaciones.first()
            assertEquals(notificacionesInicial.size, notificacionesActualizada.size)
            notificacionesInicial.forEachIndexed { index, notif ->
                assertEquals(notif.leido, notificacionesActualizada[index].leido)
            }
        }

        @Test
        fun `múltiples llamadas consecutivas a marcarComoLeida con el mismo ID`() = runTest {
            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()

            // Then
            val notificaciones = viewModel.notificaciones.first()
            val notificacion1 = notificaciones.first { it.id == 1 }
            assertTrue(notificacion1.leido, "La notificación debe estar leída")
            assertEquals(3, notificaciones.size, "La lista debe mantener el mismo tamaño")
        }
    }

    // ==================== PRUEBAS DE CONTEO Y FILTRADO ====================

    @Nested
    @DisplayName("Conteo y Filtrado Tests")
    inner class ConteoYFiltradoTests {

        @Test
        fun `contar notificaciones no leídas correctamente`() = runTest {
            // Given
            val notificaciones = viewModel.notificaciones.first()

            // When
            val noLeidas = notificaciones.count { !it.leido }

            // Then
            assertEquals(2, noLeidas, "Debe haber 2 notificaciones no leídas")
        }

        @Test
        fun `contar notificaciones leídas correctamente`() = runTest {
            // Given
            val notificaciones = viewModel.notificaciones.first()

            // When
            val leidas = notificaciones.count { it.leido }

            // Then
            assertEquals(1, leidas, "Debe haber 1 notificación leída")
        }

        @Test
        fun `filtrar solo notificaciones no leídas`() = runTest {
            // Given
            val notificaciones = viewModel.notificaciones.first()

            // When
            val noLeidas = notificaciones.filter { !it.leido }

            // Then
            assertEquals(2, noLeidas.size, "Debe haber 2 notificaciones no leídas")
            assertTrue(noLeidas.all { !it.leido }, "Todas deben estar no leídas")
            assertTrue(noLeidas.any { it.id == 1 }, "Debe incluir notificación 1")
            assertTrue(noLeidas.any { it.id == 2 }, "Debe incluir notificación 2")
        }

        @Test
        fun `después de marcar todas como leídas, el conteo de no leídas es cero`() = runTest {
            // When
            viewModel.marcarComoLeida(1)
            advanceUntilIdle()
            viewModel.marcarComoLeida(2)
            advanceUntilIdle()

            // Then
            val notificaciones = viewModel.notificaciones.first()
            val noLeidas = notificaciones.count { !it.leido }
            assertEquals(0, noLeidas, "No debe haber notificaciones no leídas")
        }
    }
}