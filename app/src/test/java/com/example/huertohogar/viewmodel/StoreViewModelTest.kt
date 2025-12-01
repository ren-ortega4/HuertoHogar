package com.example.huertohogar.viewmodel

import com.example.huertohogar.data.local.TiendaDao
import com.example.huertohogar.model.Tienda
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StoreViewModelTest {

    private lateinit var viewModel: StoreViewModel
    private lateinit var mockDao: TiendaDao
    private val testDispatcher = StandardTestDispatcher()

    private fun createMockTienda(
        id: Int = 1,
        name: String = "Tienda Test",
        address: String = "Calle Test 123",
        phone: String = "+56912345678",
        latitude: Double = -33.4489,
        longitude: Double = -70.6693,
        description: String = "Descripción de prueba"
    ) = Tienda(
        id = id,
        name = name,
        address = address,
        phone = phone,
        latitude = latitude,
        longitude = longitude,
        description = description
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockDao = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initFetchesStoresFromDatabase() = runTest {
        // Arrange
        val expectedStores = listOf(
            createMockTienda(1, "Tienda 1"),
            createMockTienda(2, "Tienda 2"),
            createMockTienda(3, "Tienda 3")
        )
        coEvery { mockDao.getAllTiendas() } returns expectedStores

        // Act
        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Assert
        assertEquals(expectedStores, viewModel.stores.value)
        coVerify { mockDao.getAllTiendas() }
    }

    @Test
    fun storesStateIsEmptyWhenDatabaseIsEmpty() = runTest {
        // Arrange
        coEvery { mockDao.getAllTiendas() } returns emptyList()

        // Act
        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.stores.value.isEmpty())
        coVerify { mockDao.getAllTiendas() }
    }

    @Test
    fun addStoreInsertsStoreAndRefreshesList() = runTest {
        // Arrange
        val initialStores = listOf(createMockTienda(1, "Tienda 1"))
        val newStore = createMockTienda(2, "Nueva Tienda")
        val updatedStores = listOf(
            createMockTienda(1, "Tienda 1"),
            createMockTienda(2, "Nueva Tienda")
        )

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(initialStores, updatedStores)
        coEvery { mockDao.insertAll(listOf(newStore)) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(newStore)
        advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.stores.value.size)
        assertEquals("Nueva Tienda", viewModel.stores.value[1].name)
        coVerify { mockDao.insertAll(listOf(newStore)) }
        coVerify(exactly = 2) { mockDao.getAllTiendas() }
    }

    @Test
    fun addStoreWithValidDataInsertsCorrectly() = runTest {
        // Arrange
        val store = createMockTienda(
            id = 1,
            name = "HuertoHogar Central",
            address = "Av. Providencia 1234",
            phone = "+56912345678",
            latitude = -33.4489,
            longitude = -70.6693,
            description = "Tienda principal"
        )

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(emptyList(), listOf(store))
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(store)
        advanceUntilIdle()

        // Assert
        assertEquals(1, viewModel.stores.value.size)
        assertEquals("HuertoHogar Central", viewModel.stores.value.first().name)
        assertEquals("Av. Providencia 1234", viewModel.stores.value.first().address)
        assertEquals("+56912345678", viewModel.stores.value.first().phone)
    }

    @Test
    fun multipleStoresCanBeAdded() = runTest {
        // Arrange
        val store1 = createMockTienda(1, "Tienda 1")
        val store2 = createMockTienda(2, "Tienda 2")
        val store3 = createMockTienda(3, "Tienda 3")

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(
            emptyList(),
            listOf(store1),
            listOf(store1, store2),
            listOf(store1, store2, store3)
        )
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(store1)
        advanceUntilIdle()
        viewModel.addStore(store2)
        advanceUntilIdle()
        viewModel.addStore(store3)
        advanceUntilIdle()

        // Assert
        assertEquals(3, viewModel.stores.value.size)
        coVerify(exactly = 3) { mockDao.insertAll(any()) }
        coVerify(exactly = 4) { mockDao.getAllTiendas() }
    }

    @Test
    fun storesWithDifferentCoordinatesAreHandledCorrectly() = runTest {
        // Arrange
        val storeNorth = createMockTienda(1, "Tienda Norte", latitude = -33.4000, longitude = -70.6000)
        val storeSouth = createMockTienda(2, "Tienda Sur", latitude = -33.5000, longitude = -70.7000)

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(
            emptyList(),
            listOf(storeNorth, storeSouth)
        )
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(storeNorth)
        viewModel.addStore(storeSouth)
        advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.stores.value.size)
        assertTrue(viewModel.stores.value.any { it.latitude == -33.4000 })
        assertTrue(viewModel.stores.value.any { it.latitude == -33.5000 })
    }

    @Test
    fun storesMaintainsDataIntegrity() = runTest {
        // Arrange
        val originalStore = createMockTienda(
            id = 1,
            name = "Tienda Original",
            address = "Dirección Original",
            phone = "+56912345678",
            latitude = -33.4489,
            longitude = -70.6693,
            description = "Descripción Original"
        )

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(
            emptyList(),
            listOf(originalStore)
        )
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(originalStore)
        advanceUntilIdle()

        // Assert
        val retrievedStore = viewModel.stores.value.first()
        assertEquals(originalStore.id, retrievedStore.id)
        assertEquals(originalStore.name, retrievedStore.name)
        assertEquals(originalStore.address, retrievedStore.address)
        assertEquals(originalStore.phone, retrievedStore.phone)
        assertEquals(originalStore.latitude, retrievedStore.latitude, 0.0001)
        assertEquals(originalStore.longitude, retrievedStore.longitude, 0.0001)
        assertEquals(originalStore.description, retrievedStore.description)
    }

    @Test
    fun daoIsCalledWithCorrectParametersWhenAddingStore() = runTest {
        // Arrange
        val store = createMockTienda(5, "Tienda Test")
        coEvery { mockDao.getAllTiendas() } returns emptyList()
        coEvery { mockDao.insertAll(listOf(store)) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(store)
        advanceUntilIdle()

        // Assert
        coVerify { mockDao.insertAll(match { it.size == 1 && it.first() == store }) }
    }

    @Test
    fun storesStateUpdatesCorrectlyAfterEachOperation() = runTest {
        // Arrange
        val store1 = createMockTienda(1, "Store 1")
        val store2 = createMockTienda(2, "Store 2")

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(
            emptyList(),
            listOf(store1),
            listOf(store1, store2)
        )
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Assert initial state
        assertEquals(0, viewModel.stores.value.size)

        // Act & Assert - Add first store
        viewModel.addStore(store1)
        advanceUntilIdle()
        assertEquals(1, viewModel.stores.value.size)

        // Act & Assert - Add second store
        viewModel.addStore(store2)
        advanceUntilIdle()
        assertEquals(2, viewModel.stores.value.size)
    }

    @Test
    fun storesWithSpecialCharactersInNameAreHandled() = runTest {
        // Arrange
        val storeWithSpecialChars = createMockTienda(
            id = 1,
            name = "Tienda \"HuertoHogar\" & Cía.",
            address = "Calle O'Higgins #123",
            phone = "+56-9-1234-5678"
        )

        coEvery { mockDao.getAllTiendas() } returnsMany listOf(
            emptyList(),
            listOf(storeWithSpecialChars)
        )
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        // Act
        viewModel.addStore(storeWithSpecialChars)
        advanceUntilIdle()

        // Assert
        assertEquals(1, viewModel.stores.value.size)
        assertEquals("Tienda \"HuertoHogar\" & Cía.", viewModel.stores.value.first().name)
    }

    @Test
    fun storesListIsObservableViaStateFlow() = runTest {
        // Arrange
        val store = createMockTienda(1, "Observable Store")
        coEvery { mockDao.getAllTiendas() } returnsMany listOf(emptyList(), listOf(store))
        coEvery { mockDao.insertAll(any()) } returns Unit

        viewModel = StoreViewModel(mockDao)
        advanceUntilIdle()

        val collectedValues = mutableListOf<List<Tienda>>()
        
        // Act
        collectedValues.add(viewModel.stores.value)
        viewModel.addStore(store)
        advanceUntilIdle()
        collectedValues.add(viewModel.stores.value)

        // Assert
        assertEquals(2, collectedValues.size)
        assertEquals(0, collectedValues[0].size)
        assertEquals(1, collectedValues[1].size)
    }
}
