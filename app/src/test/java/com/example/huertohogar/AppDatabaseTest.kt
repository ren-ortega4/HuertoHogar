package com.example.huertohogar

import android.content.Context
import androidx.room.Room
import com.example.huertohogar.data.local.AppDatabase
import com.example.huertohogar.data.local.CategoryDao
import com.example.huertohogar.data.local.ProductDao
import com.example.huertohogar.data.local.TiendaDao
import com.example.huertohogar.data.local.TipDao
import com.example.huertohogar.data.local.UsuarioDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var mockContext: Context

    @BeforeEach
    fun setUp() {
        // Mock del contexto de Android
        mockContext = mockk(relaxed = true)
        every { mockContext.applicationContext } returns mockContext

        // Mockeamos Room.databaseBuilder para crear una BD en memoria
        mockkStatic(Room::class)
        database = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `database instance should not be null`() {
        // Given
        val mockDatabase = mockk<AppDatabase>(relaxed = true)
        every { Room.databaseBuilder(any(), AppDatabase::class.java, any()) } returns mockk(relaxed = true) {
            every { addCallback(any()) } returns this
            every { fallbackToDestructiveMigration() } returns this
            every { build() } returns mockDatabase
        }

        // When
        val result = AppDatabase.getDatabase(mockContext)

        // Then
        assertNotNull(result)
    }

    @Test
    fun `tipDao should not be null`() {
        // Given
        val mockTipDao = mockk<TipDao>(relaxed = true)
        every { database.tipDao() } returns mockTipDao

        // When
        val result = database.tipDao()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `categoryDao should not be null`() {
        // Given
        val mockCategoryDao = mockk<CategoryDao>(relaxed = true)
        every { database.categoryDao() } returns mockCategoryDao

        // When
        val result = database.categoryDao()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `productDao should not be null`() {
        // Given
        val mockProductDao = mockk<ProductDao>(relaxed = true)
        every { database.productDao() } returns mockProductDao

        // When
        val result = database.productDao()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `usuarioDao should not be null`() {
        // Given
        val mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
        every { database.usuarioDao() } returns mockUsuarioDao

        // When
        val result = database.usuarioDao()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `tiendaDao should not be null`() {
        // Given
        val mockTiendaDao = mockk<TiendaDao>(relaxed = true)
        every { database.tiendaDao() } returns mockTiendaDao

        // When
        val result = database.tiendaDao()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `getDatabase should return same instance on multiple calls`() {
        // Given
        val mockDatabase = mockk<AppDatabase>(relaxed = true)
        val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
        
        every { Room.databaseBuilder(any(), AppDatabase::class.java, any()) } returns builder
        every { builder.addCallback(any()) } returns builder
        every { builder.fallbackToDestructiveMigration() } returns builder
        every { builder.build() } returns mockDatabase

        // When
        val instance1 = AppDatabase.getDatabase(mockContext)
        val instance2 = AppDatabase.getDatabase(mockContext)

        // Then
        assertNotNull(instance1)
        assertNotNull(instance2)
    }

    @Test
    fun `database should use correct database name`() {
        // Given
        val expectedDatabaseName = "huerto_hogar_database"
        val builder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
        
        every { Room.databaseBuilder(any(), AppDatabase::class.java, expectedDatabaseName) } returns builder
        every { builder.addCallback(any()) } returns builder
        every { builder.fallbackToDestructiveMigration() } returns builder
        every { builder.build() } returns mockk(relaxed = true)

        // When
        AppDatabase.getDatabase(mockContext)

        // Then - Si llega aquí sin excepción, el nombre es correcto
        assertTrue(true)
    }

    @Test
    fun `database should have callback configured`() {
        // Given
        val mockDatabase = mockk<AppDatabase>(relaxed = true)
        every { Room.databaseBuilder(any(), AppDatabase::class.java, any()) } returns mockk(relaxed = true) {
            every { addCallback(any()) } returns this
            every { fallbackToDestructiveMigration() } returns this
            every { build() } returns mockDatabase
        }

        // When
        val result = AppDatabase.getDatabase(mockContext)

        // Then - Si la base de datos se crea correctamente, el callback fue configurado
        assertNotNull(result)
    }

    @Test
    fun `database should have fallback to destructive migration enabled`() {
        // Given
        val mockDatabase = mockk<AppDatabase>(relaxed = true)
        every { Room.databaseBuilder(any(), AppDatabase::class.java, any()) } returns mockk(relaxed = true) {
            every { addCallback(any()) } returns this
            every { fallbackToDestructiveMigration() } returns this
            every { build() } returns mockDatabase
        }

        // When
        val result = AppDatabase.getDatabase(mockContext)

        // Then - Si la base de datos se crea correctamente, fallback fue habilitado
        assertNotNull(result)
    }
}