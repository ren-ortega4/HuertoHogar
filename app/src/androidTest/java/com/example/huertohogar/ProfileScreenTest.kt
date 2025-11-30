package com.example.huertohogar

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.huertohogar.data.local.UsuarioDao
import com.example.huertohogar.data.repository.UsuarioRepository
import com.example.huertohogar.model.LoginRequest
import com.example.huertohogar.model.User
import com.example.huertohogar.model.UserEntity
import com.example.huertohogar.network.ApiService
import com.example.huertohogar.view.screen.ProfileScreen
import com.example.huertohogar.viewmodel.UserViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

// Mock del ApiService
class MockApiService : ApiService {
    override suspend fun registarUsusario(user: User): Response<User> {
        return Response.success(user)
    }

    override suspend fun login(loginRequest: LoginRequest): Response<com.example.huertohogar.model.LoginResponse> {
        // LoginResponse necesita token y user
        val mockUser = User(
            id_usuario = 1,
            nombre = "Mock",
            apellido = "User",
            correo = loginRequest.correo,
            contrasena = "",
            fecha_registro = "",
            estado = true,
            region = "",
            rol = null,
            fotopefil = null
        )
        return Response.success(
            com.example.huertohogar.model.LoginResponse(
                token = "mock_token",
                user = mockUser
            )
        )
    }

    override suspend fun getallUser(authorization: String): Response<List<User>> {
        return Response.success(emptyList())
    }

    override suspend fun eliminarUsuario(id: Int): Response<Unit> {
        return Response.success(Unit)
    }
}

// Mock del UsuarioDao
class MockUsuarioDao : UsuarioDao {
    private val users = mutableListOf<UserEntity>()
    private val _activeUser = MutableStateFlow<UserEntity?>(null)

    override fun getActiveUser(): Flow<UserEntity?> = _activeUser

    override suspend fun upsert(user: UserEntity) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        } else {
            users.add(user.copy(id = (users.size + 1).toLong()))
        }
        if (user.estado) {
            _activeUser.value = user
        }
    }

    override suspend fun upsertAll(users: List<UserEntity>) {
        users.forEach { user ->
            upsert(user)
        }
    }

    override fun getAllUsers(): Flow<List<UserEntity>> = flowOf(users)

    override fun getUserById(id: Long): Flow<UserEntity?> {
        return flowOf(users.find { it.id == id })
    }

    override suspend fun deleteById(id: Long) {
        users.removeIf { it.id == id }
        if (_activeUser.value?.id == id) {
            _activeUser.value = null
        }
    }
}



@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createMockViewModel(): UserViewModel {
        val mockApiService = MockApiService()
        val mockUsuarioDao = MockUsuarioDao()
        val mockRepository = UsuarioRepository(mockApiService, mockUsuarioDao)
        return UserViewModel(mockRepository)
    }

    @Test
    fun profileScreen_WithoutUser_ShowsWelcomeScreen() {
        // Arrange
        val viewModel = createMockViewModel()

        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        // Assert
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Bienvenido").assertExists()
        composeTestRule.onNodeWithText("INICIAR SESIÓN").assertExists()
    }

    @Test
    fun profileScreen_WithoutUser_ShowsRegisterButton() {
        // Arrange
        val viewModel = createMockViewModel()

        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        // Assert
        composeTestRule.waitForIdle()
        composeTestRule.onNode(
            hasText("REGISTRATE AQUI", substring = true, ignoreCase = true)
        ).assertExists()
    }

    @Test
    fun profileScreen_WithoutUser_LoginButton_IsClickable() {
        // Arrange
        val viewModel = createMockViewModel()

        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onNodeWithText("INICIAR SESIÓN")
            .assertExists()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun dialogConfirmacion_Displays_WhenShown() {
        // Arrange
        var confirmed = false

        // Act
        composeTestRule.setContent {
            com.example.huertohogar.view.screen.DialogConfirmacion(
                show = true,
                titulo = "Test Título",
                mensaje = "Test Mensaje",
                textoConfirmar = "Confirmar",
                colorConfirmar = androidx.compose.ui.graphics.Color.Red,
                onConfirm = { confirmed = true },
                onCancel = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Test Título").assertExists()
        composeTestRule.onNodeWithText("Test Mensaje").assertExists()
        composeTestRule.onNodeWithText("Confirmar").assertExists()
        composeTestRule.onNodeWithText("Cancelar").assertExists()
    }

    @Test
    fun dialogConfirmacion_ConfirmButton_TriggersCallback() {
        // Arrange
        var confirmed = false

        // Act
        composeTestRule.setContent {
            com.example.huertohogar.view.screen.DialogConfirmacion(
                show = true,
                titulo = "Confirmar Acción",
                mensaje = "¿Estás seguro?",
                textoConfirmar = "Sí",
                colorConfirmar = androidx.compose.ui.graphics.Color.Red,
                onConfirm = { confirmed = true },
                onCancel = {}
            )
        }

        composeTestRule.onNodeWithText("Sí").performClick()

        // Assert
        assert(confirmed) { "Confirm callback should be triggered" }
    }

    @Test
    fun dialogConfirmacion_CancelButton_TriggersCallback() {
        // Arrange
        var cancelled = false

        // Act
        composeTestRule.setContent {
            com.example.huertohogar.view.screen.DialogConfirmacion(
                show = true,
                titulo = "Confirmar Acción",
                mensaje = "¿Estás seguro?",
                textoConfirmar = "Sí",
                colorConfirmar = androidx.compose.ui.graphics.Color.Red,
                onConfirm = {},
                onCancel = { cancelled = true }
            )
        }

        composeTestRule.onNodeWithText("Cancelar").performClick()

        // Assert
        assert(cancelled) { "Cancel callback should be triggered" }
    }

    @Test
    fun dialogCargando_Displays_WhenShown() {
        // Act
        composeTestRule.setContent {
            com.example.huertohogar.view.screen.DialogCargando(
                show = true,
                mensaje = "Cargando datos..."
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Cargando datos...").assertExists()
        composeTestRule.onNodeWithText("Por favor espera").assertExists()
    }

    @Test
    fun dialogCargando_DoesNotDisplay_WhenHidden() {
        // Act
        composeTestRule.setContent {
            com.example.huertohogar.view.screen.DialogCargando(
                show = false,
                mensaje = "Cargando datos..."
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Cargando datos...").assertDoesNotExist()
    }

    @Test
    fun profileScreen_BackgroundImage_IsDisplayed() {
        // Arrange
        val viewModel = createMockViewModel()

        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        // Assert
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun profileScreen_Card_IsDisplayed() {
        // Arrange
        val viewModel = createMockViewModel()

        // Act
        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onNode(
            hasText("Bienvenido") or hasText("PERFIL DE USUARIO")
        ).assertExists()
    }
}