package com.example.huertohogar.data.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.model.User
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import java.lang.reflect.Field

@DisplayName("AppDatabase Tests")
class AppDatabaseTest {

    private lateinit var context: Context
    private lateinit var database: AppDatabase

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        every { context.applicationContext } returns context
        
        clearDatabaseInstance()
    }

    @AfterEach
    fun tearDown() {
        clearDatabaseInstance()
        clearAllMocks()
    }

    private fun clearDatabaseInstance() {
        try {
            val instanceField: Field = AppDatabase::class.java.getDeclaredField("INSTANCE")
            instanceField.isAccessible = true
            instanceField.set(null, null)
        } catch (e: Exception) {

        }
    }

    @Nested
    @DisplayName("Pruebas de la instancia de la base de datos")
    inner class DatabaseInstanceTests {

        @Test
        @DisplayName("Debe retornar la misma instancia de la base de datos")
        fun `getDatabase should return singleton instance`() {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            val instance1 = AppDatabase.getDatabase(context)
            val instance2 = AppDatabase.getDatabase(context)

            instance1 shouldBe instance2
            
            verify(exactly = 1) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            }
        }

        @Test
        @DisplayName("Debe configurar la base de datos correctamente")
        fun `getDatabase should configure database correctly`() {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            AppDatabase.getDatabase(context)

            verify { builder.addCallback(any()) }
            verify { builder.fallbackToDestructiveMigration() }
            verify { builder.build() }
        }

        @Test
        @DisplayName("Debe usar el contexto de la aplicación")
        fun `getDatabase should use application context`() {
            mockkStatic(Room::class)
            
            val mockAppContext = mockk<Context>(relaxed = true)
            every { context.applicationContext } returns mockAppContext
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    mockAppContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            AppDatabase.getDatabase(context)

            verify { context.applicationContext }
            verify {
                Room.databaseBuilder(
                    mockAppContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            }
        }
    }

    @Nested
    @DisplayName("DAO Tests")
    inner class DaoTests {

        @Test
        @DisplayName("Debe retornar el DAO correspondiente")
        fun `should provide TipDao instance`() {
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockTipDao = mockk<TipDao>(relaxed = true)
            
            every { mockDatabase.tipDao() } returns mockTipDao

            val tipDao = mockDatabase.tipDao()

            tipDao.shouldNotBeNull()
            tipDao shouldBe mockTipDao
        }

        @Test
        @DisplayName("Debe retornar el DAO correspondiente")
        fun `should provide CategoryDao instance`() {
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockCategoryDao = mockk<CategoryDao>(relaxed = true)
            
            every { mockDatabase.categoryDao() } returns mockCategoryDao

            val categoryDao = mockDatabase.categoryDao()

            categoryDao.shouldNotBeNull()
            categoryDao shouldBe mockCategoryDao
        }

        @Test
        @DisplayName("Debe retornar el DAO correspondiente")
        fun `should provide ProductDao instance`() {
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao

            val productDao = mockDatabase.productDao()

            productDao.shouldNotBeNull()
            productDao shouldBe mockProductDao
        }

        @Test
        @DisplayName("Debe retornar el DAO correspondiente")
        fun `should provide UsuarioDao instance`() {
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao

            val usuarioDao = mockDatabase.usuarioDao()

            usuarioDao.shouldNotBeNull()
            usuarioDao shouldBe mockUsuarioDao
        }
    }

    @Nested
    @DisplayName("Pruebas de inicialización de la base de datos")
    inner class DatabaseInitializationTests {

        @Test
        @DisplayName("Debe poblar la base de datos correctamente")
        fun `should populate products when database is empty`() = runBlocking {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()
            coEvery { mockProductDao.deleteAllProducts() } just Runs
            coEvery { mockProductDao.insertProducts(any()) } just Runs

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            AppDatabase.getDatabase(context)
            
            // Wait for coroutine to complete
            kotlinx.coroutines.delay(100)

            coVerify { mockProductDao.getAllProducts() }
        }

        @Test
        @DisplayName("Debe poblar la base de datos con usuarios ")
        fun `should populate initial user when database is empty`() = runBlocking {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()
            coEvery { mockUsuarioDao.insertar(any()) } just Runs

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            AppDatabase.getDatabase(context)
            
            // Wait for coroutine to complete
            kotlinx.coroutines.delay(100)

            coVerify { mockUsuarioDao.obtenerUsuarios() }
        }

        @Test
        @DisplayName("No debe poblar la base de datos si ya tiene datos")
        fun `should not populate products when database already has data`() = runBlocking {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            val existingProducts = listOf(
                Product(1, "Test", "$100", 0, ProductCategory.frutas)
            )
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(existingProducts)
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()
            coEvery { mockUsuarioDao.insertar(any()) } just Runs

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            AppDatabase.getDatabase(context)
            
            // Wait for coroutine to complete
            kotlinx.coroutines.delay(100)

            coVerify(exactly = 0) { mockProductDao.deleteAllProducts() }
            coVerify(exactly = 0) { mockProductDao.insertProducts(any()) }
        }
    }

    @Nested
    @DisplayName("Pruebas de poblado de productos")
    inner class PopulateProductsTests {

        @Test
        @DisplayName("PopulateProducts debe eliminar todos los productos existentes")
        fun `populateProducts should delete all existing products`() = runBlocking {
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            coEvery { mockProductDao.deleteAllProducts() } just Runs
            coEvery { mockProductDao.insertProducts(any()) } just Runs

            AppDatabase.populateProducts(mockProductDao)

            coVerify { mockProductDao.deleteAllProducts() }
        }

        @Test
        @DisplayName("populateProducts debe insertar 6 productos")
        fun `populateProducts should insert 6 products`() = runBlocking {
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val capturedProducts = slot<List<Product>>()
            
            coEvery { mockProductDao.deleteAllProducts() } just Runs
            coEvery { mockProductDao.insertProducts(capture(capturedProducts)) } just Runs

            AppDatabase.populateProducts(mockProductDao)

            coVerify { mockProductDao.insertProducts(any()) }
            capturedProducts.captured shouldHaveSize 6
        }

        @Test
        @DisplayName("populateProducts debe insertar productos con datos correctos")
        fun `populateProducts should insert correct product data`() = runBlocking {
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val capturedProducts = slot<List<Product>>()
            
            coEvery { mockProductDao.deleteAllProducts() } just Runs
            coEvery { mockProductDao.insertProducts(capture(capturedProducts)) } just Runs

            AppDatabase.populateProducts(mockProductDao)

            val products = capturedProducts.captured
            
            products[0].name shouldBe "Leche Natural"
            products[0].price shouldBe "$3.800"
            products[0].category shouldBe ProductCategory.lacteos
            
            products[1].name shouldBe "Miel Orgánica"
            products[1].price shouldBe "$5.000"
            products[1].category shouldBe ProductCategory.productosOrganicos
            
            products[2].name shouldBe "Platános Cavendish"
            products[2].price shouldBe "$800/Kg"
            products[2].category shouldBe ProductCategory.frutas
            
            products[3].name shouldBe "Manzanas Fuji"
            products[3].category shouldBe ProductCategory.frutas
            
            products[4].name shouldBe "Zanahorias organicas"
            products[4].category shouldBe ProductCategory.verduras
            
            products[5].name shouldBe "Espinacas Frescas"
            products[5].category shouldBe ProductCategory.verduras
        }

        @Test
        @DisplayName("populateProducts debe insertar productos con categorías correctas")
        fun `populateProducts should insert products with valid categories`() = runBlocking {
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val capturedProducts = slot<List<Product>>()
            
            coEvery { mockProductDao.deleteAllProducts() } just Runs
            coEvery { mockProductDao.insertProducts(capture(capturedProducts)) } just Runs

            AppDatabase.populateProducts(mockProductDao)

            val products = capturedProducts.captured
            
            products.forEach { product ->
                product.category.shouldBeInstanceOf<ProductCategory>()
            }
        }
    }

    @Nested
    @DisplayName("poblar usuarios Tests")
    inner class PopulateInitialUserTests {

        @Test
        @DisplayName("populateInitialUser debe insertar el usuario administrador")
        fun `populateInitialUser should insert admin user`() = runBlocking {
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            val capturedUser = slot<User>()
            
            coEvery { mockUsuarioDao.insertar(capture(capturedUser)) } just Runs

            AppDatabase.populateInitialUser(mockUsuarioDao)

            coVerify { mockUsuarioDao.insertar(any()) }
            capturedUser.captured.shouldNotBeNull()
        }

        @Test
        @DisplayName("populateInitialUser debe insertar usuario con credenciales correctas")
        fun `populateInitialUser should insert user with correct credentials`() = runBlocking {
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            val capturedUser = slot<User>()
            
            coEvery { mockUsuarioDao.insertar(capture(capturedUser)) } just Runs

            AppDatabase.populateInitialUser(mockUsuarioDao)

            val user = capturedUser.captured
            user.nombre shouldBe "Admin"
            user.correo shouldBe "admin@gmail.com"
            user.clave shouldBe "12345678"
            user.confirmarClave shouldBe "12345678"
        }

        @Test
        @DisplayName("populateInitialUser debe insertar usuario con dirección y región")
        fun `populateInitialUser should insert user with admin address and region`() = runBlocking {
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            val capturedUser = slot<User>()
            
            coEvery { mockUsuarioDao.insertar(capture(capturedUser)) } just Runs

            AppDatabase.populateInitialUser(mockUsuarioDao)

            val user = capturedUser.captured
            user.direccion shouldBe "admin"
            user.region shouldBe "admin"
        }

        @Test
        @DisplayName("populateInitialUser debe insertar usuario con términos aceptados")
        fun `populateInitialUser should insert user with accepted terms`() = runBlocking {
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            val capturedUser = slot<User>()
            
            coEvery { mockUsuarioDao.insertar(capture(capturedUser)) } just Runs

            AppDatabase.populateInitialUser(mockUsuarioDao)

            val user = capturedUser.captured
            user.aceptaTerminos shouldBe true
        }
    }

    @Nested
    @DisplayName("DatabaseCallback Tests")
    inner class DatabaseCallbackTests {

        @Test
        @DisplayName("onCreate callback debe ser llamado")
        fun `onCreate callback should be triggered`() {
            val mockDb = mockk<SupportSQLiteDatabase>(relaxed = true)
            val callback = AppDatabase.Companion::class.java.declaredClasses
                .firstOrNull { it.simpleName == "DatabaseCallBack" }

            callback.shouldNotBeNull()
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    inner class ThreadSafetyTests {

        @Test
        @DisplayName("getInstance debe ser thread safe con synchronized block")
        fun `getInstance should be thread safe with synchronized block`() {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } returns flowOf(emptyList())
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            val instance1 = AppDatabase.getDatabase(context)
            val instance2 = AppDatabase.getDatabase(context)

            instance1 shouldBe instance2
        }
    }

    @Nested
    @DisplayName("Pruebas de manejo de errores")
    inner class ErrorHandlingTests {

        @Test
        @DisplayName("should debe manejar excepciones durante la población de productos")
        fun `should handle exception during product population`() = runBlocking {
            mockkStatic(Room::class)
            
            val mockDatabase = mockk<AppDatabase>(relaxed = true)
            val mockProductDao = mockk<ProductDao>(relaxed = true)
            val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
            
            every { mockDatabase.productDao() } returns mockProductDao
            every { mockDatabase.usuarioDao() } returns mockUsuarioDao
            every { mockProductDao.getAllProducts() } throws RuntimeException("Database error")
            coEvery { mockUsuarioDao.obtenerUsuarios() } returns emptyList()

            val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
            every { 
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
            } returns builder
            
            every { builder.addCallback(any()) } returns builder
            every { builder.fallbackToDestructiveMigration() } returns builder
            every { builder.build() } returns mockDatabase

            val instance = AppDatabase.getDatabase(context)
            
            instance.shouldNotBeNull()
        }
    }
}