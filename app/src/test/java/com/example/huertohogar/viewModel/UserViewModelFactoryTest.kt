package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huertohogar.data.repository.UsuarioRepository
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*

class UserViewModelFactoryTest {

    @Test
    fun devuelveUserViewModelCuandoSeSolicita() {
        // Mock del repositorio (no necesitamos comportamiento concreto aquí)
        val repo: UsuarioRepository = mockk(relaxed = true)

        val factory = UserViewModelFactory(repo)
        val viewModel = factory.create(UserViewModel::class.java)

        assertNotNull(viewModel)
        assertTrue(viewModel is UserViewModel)
    }

    @Test
    fun lanzaIllegalArgumentExceptionParaClaseDesconocida() {
        val repo: UsuarioRepository = mockk(relaxed = true)
        val factory = UserViewModelFactory(repo)

        try {
            // Usamos una ViewModel de prueba que no está soportada por la factory
            factory.create(DummyViewModel::class.java)
            fail("Se esperaba IllegalArgumentException al solicitar una clase desconocida")
        } catch (e: IllegalArgumentException) {
            // OK: se lanzó la excepción esperada
        }
    }

    // Clase auxiliar para forzar la condición de clase desconocida
    class DummyViewModel : ViewModel()
}
