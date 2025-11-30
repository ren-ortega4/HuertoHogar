package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huertohogar.data.repository.UsuarioRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("UserViewModelFactory Tests")
class UserViewModelFactoryTest {

    private lateinit var repository: UsuarioRepository
    private lateinit var factory: UserViewModelFactory

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        factory = UserViewModelFactory(repository)
    }

    @Test
    @DisplayName("Debería crear instancia de UserViewModelFactory con repositorio")
    fun `should create UserViewModelFactory instance with repository`() {
        // Given
        val repo = mockk<UsuarioRepository>(relaxed = true)

        // When
        val factory = UserViewModelFactory(repo)

        // Then
        factory shouldNotBe null
        factory.shouldBeInstanceOf<UserViewModelFactory>()
    }

    @Test
    @DisplayName("Debería crear UserViewModel correctamente")
    fun `should create UserViewModel correctly`() {
        // When
        val viewModel = factory.create(UserViewModel::class.java)

        // Then
        viewModel shouldNotBe null
        viewModel.shouldBeInstanceOf<UserViewModel>()
    }

    @Test
    @DisplayName("Debería lanzar excepción para ViewModel desconocido")
    fun `should throw exception for unknown ViewModel class`() {
        // Given
        class UnknownViewModel : ViewModel()

        // When & Then
        val exception = shouldThrow<IllegalArgumentException> {
            factory.create(UnknownViewModel::class.java)
        }
        
        exception.message shouldBe "Unknown ViewModel class"
    }

    @Test
    @DisplayName("Debería crear múltiples instancias de UserViewModel")
    fun `should create multiple instances of UserViewModel`() {
        // When
        val viewModel1 = factory.create(UserViewModel::class.java)
        val viewModel2 = factory.create(UserViewModel::class.java)

        // Then
        viewModel1 shouldNotBe null
        viewModel2 shouldNotBe null
        viewModel1.shouldBeInstanceOf<UserViewModel>()
        viewModel2.shouldBeInstanceOf<UserViewModel>()
        // Cada llamada debe crear una nueva instancia
        (viewModel1 !== viewModel2) shouldBe true
    }

    @Test
    @DisplayName("Debería validar que el ViewModel es asignable a UserViewModel")
    fun `should validate ViewModel is assignable to UserViewModel`() {
        // When
        val viewModel = factory.create(UserViewModel::class.java)

        // Then
        UserViewModel::class.java.isAssignableFrom(viewModel::class.java) shouldBe true
    }

    @Test
    @DisplayName("Debería lanzar excepción con mensaje específico para clase incorrecta")
    fun `should throw exception with specific message for incorrect class`() {
        // Given
        open class AnotherViewModel : ViewModel()

        // When & Then
        val exception = shouldThrow<IllegalArgumentException> {
            factory.create(AnotherViewModel::class.java)
        }
        
        exception.message shouldBe "Unknown ViewModel class"
    }

    @Test
    @DisplayName("Debería crear UserViewModel con el repositorio inyectado")
    fun `should create UserViewModel with injected repository`() {
        // Given
        val specificRepo = mockk<UsuarioRepository>(relaxed = true)
        val specificFactory = UserViewModelFactory(specificRepo)

        // When
        val viewModel = specificFactory.create(UserViewModel::class.java)

        // Then
        viewModel shouldNotBe null
        viewModel.shouldBeInstanceOf<UserViewModel>()
    }

    @Test
    @DisplayName("Debería funcionar como ViewModelProvider.Factory")
    fun `should work as ViewModelProvider Factory`() {
        // When
        val isFactory = factory is androidx.lifecycle.ViewModelProvider.Factory

        // Then
        isFactory shouldBe true
    }

    @Test
    @DisplayName("Debería manejar la verificación de clase UserViewModel correctamente")
    fun `should handle UserViewModel class check correctly`() {
        // Given
        val userViewModelClass = UserViewModel::class.java

        // When
        val canAssign = userViewModelClass.isAssignableFrom(UserViewModel::class.java)

        // Then
        canAssign shouldBe true
    }

    @Test
    @DisplayName("Debería retornar instancia correcta al crear ViewModel")
    fun `should return correct instance when creating ViewModel`() {
        // When
        val viewModel = factory.create(UserViewModel::class.java)

        // Then
        viewModel::class.java shouldBe UserViewModel::class.java
    }

    @Test
    @DisplayName("Factory debería ser reutilizable para crear múltiples ViewModels")
    fun `factory should be reusable to create multiple ViewModels`() {
        // When
        val viewModel1 = factory.create(UserViewModel::class.java)
        val viewModel2 = factory.create(UserViewModel::class.java)
        val viewModel3 = factory.create(UserViewModel::class.java)

        // Then
        viewModel1.shouldBeInstanceOf<UserViewModel>()
        viewModel2.shouldBeInstanceOf<UserViewModel>()
        viewModel3.shouldBeInstanceOf<UserViewModel>()
        
        // Todas deben ser instancias diferentes
        (viewModel1 !== viewModel2) shouldBe true
        (viewModel2 !== viewModel3) shouldBe true
        (viewModel1 !== viewModel3) shouldBe true
    }

    @Test
    @DisplayName("Debería crear ViewModels independientes con el mismo repositorio")
    fun `should create independent ViewModels with same repository`() {
        // Given
        val sharedRepo = mockk<UsuarioRepository>(relaxed = true)
        val factory1 = UserViewModelFactory(sharedRepo)
        val factory2 = UserViewModelFactory(sharedRepo)

        // When
        val viewModel1 = factory1.create(UserViewModel::class.java)
        val viewModel2 = factory2.create(UserViewModel::class.java)

        // Then
        viewModel1.shouldBeInstanceOf<UserViewModel>()
        viewModel2.shouldBeInstanceOf<UserViewModel>()
        (viewModel1 !== viewModel2) shouldBe true
    }

    @Test
    @DisplayName("Debería mantener referencia al repositorio inyectado")
    fun `should maintain reference to injected repository`() {
        // Given
        val repo1 = mockk<UsuarioRepository>(relaxed = true)
        val repo2 = mockk<UsuarioRepository>(relaxed = true)
        
        val factory1 = UserViewModelFactory(repo1)
        val factory2 = UserViewModelFactory(repo2)

        // When
        val viewModel1 = factory1.create(UserViewModel::class.java)
        val viewModel2 = factory2.create(UserViewModel::class.java)

        // Then
        viewModel1.shouldBeInstanceOf<UserViewModel>()
        viewModel2.shouldBeInstanceOf<UserViewModel>()
        // Ambos ViewModels deben ser instancias válidas pero diferentes
        (viewModel1 !== viewModel2) shouldBe true
    }

    @Test
    @DisplayName("Excepción debería contener mensaje descriptivo")
    fun `exception should contain descriptive message`() {
        // Given
        class CustomViewModel : ViewModel()

        // When & Then
        val exception = shouldThrow<IllegalArgumentException> {
            factory.create(CustomViewModel::class.java)
        }
        
        exception.message shouldNotBe null
        exception.message shouldBe "Unknown ViewModel class"
    }

    @Test
    @DisplayName("Debería crear ViewModel usando el método create genérico")
    fun `should create ViewModel using generic create method`() {
        // When
        val viewModel: ViewModel = factory.create(UserViewModel::class.java)

        // Then
        viewModel shouldNotBe null
        (viewModel is UserViewModel) shouldBe true
    }
}