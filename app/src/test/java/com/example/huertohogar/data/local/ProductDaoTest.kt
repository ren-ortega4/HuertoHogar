package com.example.huertohogar.data.local

import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("ProductDao Tests")
class ProductDaoTest {
    
    private lateinit var productDao: ProductDao

    @BeforeEach
    fun setup() {
        productDao = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSampleProduct(
        id: Int = 1,
        name: String = "Manzana",
        price: String = "$1.500/Kg",
        imagesRes: Int = 123,
        category: ProductCategory = ProductCategory.frutas
    ) = Product(
        id = id,
        name = name,
        price = price,
        imagesRes = imagesRes,
        category = category
    )

    @Nested
    @DisplayName("getAllProducts Tests")
    inner class GetAllProductsTests {

        @Test
        @DisplayName("getAllProducts should return Flow of all products")
        fun `getAllProducts should return Flow of all products`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, name = "Manzana"),
                createSampleProduct(id = 2, name = "Banana"),
                createSampleProduct(id = 3, name = "Zanahoria", category = ProductCategory.verduras)
            )
            every { productDao.getAllProducts() } returns flowOf(products)

            val result = productDao.getAllProducts().first()

            result shouldHaveSize 3
            result shouldBe products
        }

        @Test
        @DisplayName("getAllProducts should return empty list when no products")
        fun `getAllProducts should return empty list when no products`() = runBlocking {
            every { productDao.getAllProducts() } returns flowOf(emptyList())

            val result = productDao.getAllProducts().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("getAllProducts should return Flow that can be collected")
        fun `getAllProducts should return Flow that can be collected`() = runBlocking {
            val products = listOf(createSampleProduct())
            every { productDao.getAllProducts() } returns flowOf(products)

            val flow = productDao.getAllProducts()

            flow.shouldNotBeNull()
            val result = flow.first()
            result shouldHaveSize 1
        }

        @Test
        @DisplayName("getAllProducts should preserve product data")
        fun `getAllProducts should preserve product data`() = runBlocking {
            val product = createSampleProduct(
                id = 10,
                name = "Producto Especial",
                price = "$5.000",
                imagesRes = 999,
                category = ProductCategory.productosOrganicos
            )
            every { productDao.getAllProducts() } returns flowOf(listOf(product))

            val result = productDao.getAllProducts().first()

            result.first().id shouldBe 10
            result.first().name shouldBe "Producto Especial"
            result.first().price shouldBe "$5.000"
            result.first().imagesRes shouldBe 999
            result.first().category shouldBe ProductCategory.productosOrganicos
        }
    }

    @Nested
    @DisplayName("getProductsByCategory Tests")
    inner class GetProductsByCategoryTests {

        @Test
        @DisplayName("getProductsByCategory should return products of specific category")
        fun `getProductsByCategory should return products of specific category`() = runBlocking {
            val frutasProducts = listOf(
                createSampleProduct(id = 1, name = "Manzana", category = ProductCategory.frutas),
                createSampleProduct(id = 2, name = "Banana", category = ProductCategory.frutas)
            )
            every { productDao.getProductsByCategory(ProductCategory.frutas) } returns flowOf(frutasProducts)

            val result = productDao.getProductsByCategory(ProductCategory.frutas).first()

            result shouldHaveSize 2
            result.all { it.category == ProductCategory.frutas } shouldBe true
        }

        @Test
        @DisplayName("getProductsByCategory should return empty list for category with no products")
        fun `getProductsByCategory should return empty list for category with no products`() = runBlocking {
            every { productDao.getProductsByCategory(ProductCategory.lacteos) } returns flowOf(emptyList())

            val result = productDao.getProductsByCategory(ProductCategory.lacteos).first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("getProductsByCategory should filter correctly by verduras")
        fun `getProductsByCategory should filter correctly by verduras`() = runBlocking {
            val verdurasProducts = listOf(
                createSampleProduct(id = 3, name = "Zanahoria", category = ProductCategory.verduras),
                createSampleProduct(id = 4, name = "Espinaca", category = ProductCategory.verduras)
            )
            every { productDao.getProductsByCategory(ProductCategory.verduras) } returns flowOf(verdurasProducts)

            val result = productDao.getProductsByCategory(ProductCategory.verduras).first()

            result shouldHaveSize 2
            result.forEach { it.category shouldBe ProductCategory.verduras }
        }

        @Test
        @DisplayName("getProductsByCategory should work for all categories")
        fun `getProductsByCategory should work for all categories`() = runBlocking {
            ProductCategory.entries.forEach { category ->
                val products = listOf(createSampleProduct(category = category))
                every { productDao.getProductsByCategory(category) } returns flowOf(products)

                val result = productDao.getProductsByCategory(category).first()

                result.all { it.category == category } shouldBe true
            }
        }

        @Test
        @DisplayName("getProductsByCategory should not return products from other categories")
        fun `getProductsByCategory should not return products from other categories`() = runBlocking {
            val frutasProducts = listOf(
                createSampleProduct(id = 1, category = ProductCategory.frutas)
            )
            every { productDao.getProductsByCategory(ProductCategory.frutas) } returns flowOf(frutasProducts)

            val result = productDao.getProductsByCategory(ProductCategory.frutas).first()

            result.none { it.category == ProductCategory.verduras } shouldBe true
        }
    }

    @Nested
    @DisplayName("getProductById Tests")
    inner class GetProductByIdTests {

        @Test
        @DisplayName("getProductById should return product when exists")
        fun `getProductById should return product when exists`() = runBlocking {
            val product = createSampleProduct(id = 5, name = "Producto Test")
            coEvery { productDao.getProductById(5) } returns product

            val result = productDao.getProductById(5)

            result.shouldNotBeNull()
            result.id shouldBe 5
            result.name shouldBe "Producto Test"
        }

        @Test
        @DisplayName("getProductById should return null when product does not exist")
        fun `getProductById should return null when product does not exist`() = runBlocking {
            coEvery { productDao.getProductById(999) } returns null

            val result = productDao.getProductById(999)

            result.shouldBeNull()
        }

        @Test
        @DisplayName("getProductById should return correct product for different ids")
        fun `getProductById should return correct product for different ids`() = runBlocking {
            val product1 = createSampleProduct(id = 1, name = "Producto 1")
            val product2 = createSampleProduct(id = 2, name = "Producto 2")
            
            coEvery { productDao.getProductById(1) } returns product1
            coEvery { productDao.getProductById(2) } returns product2

            val result1 = productDao.getProductById(1)
            val result2 = productDao.getProductById(2)

            result1?.name shouldBe "Producto 1"
            result2?.name shouldBe "Producto 2"
        }

        @Test
        @DisplayName("getProductById should handle id zero")
        fun `getProductById should handle id zero`() = runBlocking {
            val product = createSampleProduct(id = 0)
            coEvery { productDao.getProductById(0) } returns product

            val result = productDao.getProductById(0)

            result.shouldNotBeNull()
            result.id shouldBe 0
        }

        @Test
        @DisplayName("getProductById should handle negative id")
        fun `getProductById should handle negative id`() = runBlocking {
            coEvery { productDao.getProductById(-1) } returns null

            val result = productDao.getProductById(-1)

            result.shouldBeNull()
        }
    }

    @Nested
    @DisplayName("searchProducts Tests")
    inner class SearchProductsTests {

        @Test
        @DisplayName("searchProducts should find products by name")
        fun `searchProducts should find products by name`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, name = "Manzana Roja"),
                createSampleProduct(id = 2, name = "Manzana Verde")
            )
            every { productDao.searchProducts("Manzana") } returns flowOf(products)

            val result = productDao.searchProducts("Manzana").first()

            result shouldHaveSize 2
            result.all { it.name.contains("Manzana") } shouldBe true
        }

        @Test
        @DisplayName("searchProducts should return empty list for no matches")
        fun `searchProducts should return empty list for no matches`() = runBlocking {
            every { productDao.searchProducts("ProductoInexistente") } returns flowOf(emptyList())

            val result = productDao.searchProducts("ProductoInexistente").first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("searchProducts should be case insensitive")
        fun `searchProducts should be case insensitive`() = runBlocking {
            val products = listOf(createSampleProduct(name = "Banana"))
            every { productDao.searchProducts("banana") } returns flowOf(products)
            every { productDao.searchProducts("BANANA") } returns flowOf(products)

            val result1 = productDao.searchProducts("banana").first()
            val result2 = productDao.searchProducts("BANANA").first()

            result1 shouldHaveSize 1
            result2 shouldHaveSize 1
        }

        @Test
        @DisplayName("searchProducts should find partial matches")
        fun `searchProducts should find partial matches`() = runBlocking {
            val products = listOf(createSampleProduct(name = "Zanahoria Orgánica"))
            every { productDao.searchProducts("horia") } returns flowOf(products)

            val result = productDao.searchProducts("horia").first()

            result shouldHaveSize 1
            result.first().name shouldBe "Zanahoria Orgánica"
        }

        @Test
        @DisplayName("searchProducts should handle empty query")
        fun `searchProducts should handle empty query`() = runBlocking {
            val allProducts = listOf(
                createSampleProduct(id = 1),
                createSampleProduct(id = 2)
            )
            every { productDao.searchProducts("") } returns flowOf(allProducts)

            val result = productDao.searchProducts("").first()

            result shouldHaveSize 2
        }

        @Test
        @DisplayName("searchProducts should handle special characters")
        fun `searchProducts should handle special characters`() = runBlocking {
            val products = listOf(createSampleProduct(name = "Piña"))
            every { productDao.searchProducts("Piña") } returns flowOf(products)

            val result = productDao.searchProducts("Piña").first()

            result shouldHaveSize 1
        }
    }

    @Nested
    @DisplayName("insertProduct Tests")
    inner class InsertProductTests {

        @Test
        @DisplayName("insertProduct should insert single product")
        fun `insertProduct should insert single product`() = runBlocking {
            val product = createSampleProduct()
            coEvery { productDao.insertProduct(product) } just Runs

            productDao.insertProduct(product)

            coVerify { productDao.insertProduct(product) }
        }

        @Test
        @DisplayName("insertProduct should replace on conflict")
        fun `insertProduct should replace on conflict`() = runBlocking {
            val product1 = createSampleProduct(id = 1, name = "Original")
            val product2 = createSampleProduct(id = 1, name = "Actualizado")
            
            coEvery { productDao.insertProduct(any()) } just Runs

            productDao.insertProduct(product1)
            productDao.insertProduct(product2)

            coVerify(exactly = 2) { productDao.insertProduct(any()) }
        }

        @Test
        @DisplayName("insertProduct should handle product with all fields")
        fun `insertProduct should handle product with all fields`() = runBlocking {
            val product = createSampleProduct(
                id = 100,
                name = "Producto Completo",
                price = "$10.000",
                imagesRes = 456,
                category = ProductCategory.lacteos
            )
            coEvery { productDao.insertProduct(product) } just Runs

            productDao.insertProduct(product)

            coVerify { productDao.insertProduct(match { 
                it.id == 100 && it.name == "Producto Completo" 
            }) }
        }
    }

    @Nested
    @DisplayName("insertProducts Tests")
    inner class InsertProductsTests {

        @Test
        @DisplayName("insertProducts should insert multiple products")
        fun `insertProducts should insert multiple products`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, name = "Producto 1"),
                createSampleProduct(id = 2, name = "Producto 2"),
                createSampleProduct(id = 3, name = "Producto 3")
            )
            coEvery { productDao.insertProducts(products) } just Runs

            productDao.insertProducts(products)

            coVerify { productDao.insertProducts(products) }
        }

        @Test
        @DisplayName("insertProducts should insert empty list")
        fun `insertProducts should insert empty list`() = runBlocking {
            val emptyList = emptyList<Product>()
            coEvery { productDao.insertProducts(emptyList) } just Runs

            productDao.insertProducts(emptyList)

            coVerify { productDao.insertProducts(emptyList) }
        }

        @Test
        @DisplayName("insertProducts should insert single product in list")
        fun `insertProducts should insert single product in list`() = runBlocking {
            val products = listOf(createSampleProduct())
            coEvery { productDao.insertProducts(products) } just Runs

            productDao.insertProducts(products)

            coVerify { productDao.insertProducts(match { it.size == 1 }) }
        }

        @Test
        @DisplayName("insertProducts should handle products with different categories")
        fun `insertProducts should handle products with different categories`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, category = ProductCategory.frutas),
                createSampleProduct(id = 2, category = ProductCategory.verduras),
                createSampleProduct(id = 3, category = ProductCategory.lacteos)
            )
            coEvery { productDao.insertProducts(products) } just Runs

            productDao.insertProducts(products)

            coVerify { productDao.insertProducts(match { it.size == 3 }) }
        }
    }

    @Nested
    @DisplayName("updateProduct Tests")
    inner class UpdateProductTests {

        @Test
        @DisplayName("updateProduct should update existing product")
        fun `updateProduct should update existing product`() = runBlocking {
            val product = createSampleProduct(id = 1, name = "Actualizado")
            coEvery { productDao.updateProduct(product) } just Runs

            productDao.updateProduct(product)

            coVerify { productDao.updateProduct(product) }
        }

        @Test
        @DisplayName("updateProduct should update product name")
        fun `updateProduct should update product name`() = runBlocking {
            val product = createSampleProduct(id = 1, name = "Nuevo Nombre")
            coEvery { productDao.updateProduct(product) } just Runs

            productDao.updateProduct(product)

            coVerify { productDao.updateProduct(match { it.name == "Nuevo Nombre" }) }
        }

        @Test
        @DisplayName("updateProduct should update product price")
        fun `updateProduct should update product price`() = runBlocking {
            val product = createSampleProduct(id = 1, price = "$2.500/Kg")
            coEvery { productDao.updateProduct(product) } just Runs

            productDao.updateProduct(product)

            coVerify { productDao.updateProduct(match { it.price == "$2.500/Kg" }) }
        }

        @Test
        @DisplayName("updateProduct should update product category")
        fun `updateProduct should update product category`() = runBlocking {
            val product = createSampleProduct(id = 1, category = ProductCategory.otros)
            coEvery { productDao.updateProduct(product) } just Runs

            productDao.updateProduct(product)

            coVerify { productDao.updateProduct(match { it.category == ProductCategory.otros }) }
        }

        @Test
        @DisplayName("updateProduct should update all fields")
        fun `updateProduct should update all fields`() = runBlocking {
            val product = createSampleProduct(
                id = 1,
                name = "Nuevo",
                price = "$999",
                imagesRes = 777,
                category = ProductCategory.productosOrganicos
            )
            coEvery { productDao.updateProduct(product) } just Runs

            productDao.updateProduct(product)

            coVerify { productDao.updateProduct(product) }
        }
    }

    @Nested
    @DisplayName("deleteProduct Tests")
    inner class DeleteProductTests {

        @Test
        @DisplayName("deleteProduct should delete specific product")
        fun `deleteProduct should delete specific product`() = runBlocking {
            val product = createSampleProduct(id = 1)
            coEvery { productDao.deleteProduct(product) } just Runs

            productDao.deleteProduct(product)

            coVerify { productDao.deleteProduct(product) }
        }

        @Test
        @DisplayName("deleteProduct should delete product by id")
        fun `deleteProduct should delete product by id`() = runBlocking {
            val product = createSampleProduct(id = 5)
            coEvery { productDao.deleteProduct(product) } just Runs

            productDao.deleteProduct(product)

            coVerify { productDao.deleteProduct(match { it.id == 5 }) }
        }

        @Test
        @DisplayName("deleteProduct should work with any product")
        fun `deleteProduct should work with any product`() = runBlocking {
            val product = createSampleProduct(
                id = 10,
                name = "Para Eliminar",
                category = ProductCategory.otros
            )
            coEvery { productDao.deleteProduct(product) } just Runs

            productDao.deleteProduct(product)

            coVerify { productDao.deleteProduct(product) }
        }
    }

    @Nested
    @DisplayName("deleteAllProducts Tests")
    inner class DeleteAllProductsTests {

        @Test
        @DisplayName("deleteAllProducts should delete all products")
        fun `deleteAllProducts should delete all products`() = runBlocking {
            coEvery { productDao.deleteAllProducts() } just Runs

            productDao.deleteAllProducts()

            coVerify { productDao.deleteAllProducts() }
        }

        @Test
        @DisplayName("deleteAllProducts should be callable multiple times")
        fun `deleteAllProducts should be callable multiple times`() = runBlocking {
            coEvery { productDao.deleteAllProducts() } just Runs

            productDao.deleteAllProducts()
            productDao.deleteAllProducts()
            productDao.deleteAllProducts()

            coVerify(exactly = 3) { productDao.deleteAllProducts() }
        }

        @Test
        @DisplayName("deleteAllProducts should work on empty database")
        fun `deleteAllProducts should work on empty database`() = runBlocking {
            coEvery { productDao.deleteAllProducts() } just Runs
            every { productDao.getAllProducts() } returns flowOf(emptyList())

            productDao.deleteAllProducts()
            val result = productDao.getAllProducts().first()

            result.shouldBeEmpty()
        }
    }

    @Nested
    @DisplayName("getAllCategories Tests")
    inner class GetAllCategoriesTests {

        @Test
        @DisplayName("getAllCategories should return distinct categories")
        fun `getAllCategories should return distinct categories`() = runBlocking {
            val categories = listOf(
                ProductCategory.frutas,
                ProductCategory.verduras,
                ProductCategory.lacteos
            )
            every { productDao.getAllCategories() } returns flowOf(categories)

            val result = productDao.getAllCategories().first()

            result shouldHaveSize 3
            result shouldContain ProductCategory.frutas
            result shouldContain ProductCategory.verduras
            result shouldContain ProductCategory.lacteos
        }

        @Test
        @DisplayName("getAllCategories should return empty list when no products")
        fun `getAllCategories should return empty list when no products`() = runBlocking {
            every { productDao.getAllCategories() } returns flowOf(emptyList())

            val result = productDao.getAllCategories().first()

            result.shouldBeEmpty()
        }

        @Test
        @DisplayName("getAllCategories should return ordered categories")
        fun `getAllCategories should return ordered categories`() = runBlocking {
            val categories = listOf(
                ProductCategory.frutas,
                ProductCategory.lacteos,
                ProductCategory.otros,
                ProductCategory.productosOrganicos,
                ProductCategory.verduras
            )
            every { productDao.getAllCategories() } returns flowOf(categories)

            val result = productDao.getAllCategories().first()

            result shouldHaveSize 5
        }

        @Test
        @DisplayName("getAllCategories should not contain duplicates")
        fun `getAllCategories should not contain duplicates`() = runBlocking {
            val categories = listOf(ProductCategory.frutas, ProductCategory.verduras)
            every { productDao.getAllCategories() } returns flowOf(categories)

            val result = productDao.getAllCategories().first()

            result.distinct().size shouldBe result.size
        }

        @Test
        @DisplayName("getAllCategories should return Flow that can be collected")
        fun `getAllCategories should return Flow that can be collected`() = runBlocking {
            val categories = listOf(ProductCategory.frutas)
            every { productDao.getAllCategories() } returns flowOf(categories)

            val flow = productDao.getAllCategories()

            flow.shouldNotBeNull()
            val result = flow.first()
            result shouldHaveSize 1
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    inner class IntegrationScenariosTests {

        @Test
        @DisplayName("should insert product and then retrieve by id")
        fun `should insert product and then retrieve by id`() = runBlocking {
            val product = createSampleProduct(id = 1, name = "Test")
            coEvery { productDao.insertProduct(product) } just Runs
            coEvery { productDao.getProductById(1) } returns product

            productDao.insertProduct(product)
            val result = productDao.getProductById(1)

            result.shouldNotBeNull()
            result.name shouldBe "Test"
        }

        @Test
        @DisplayName("should insert products and retrieve by category")
        fun `should insert products and retrieve by category`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, category = ProductCategory.frutas),
                createSampleProduct(id = 2, category = ProductCategory.frutas)
            )
            coEvery { productDao.insertProducts(products) } just Runs
            every { productDao.getProductsByCategory(ProductCategory.frutas) } returns flowOf(products)

            productDao.insertProducts(products)
            val result = productDao.getProductsByCategory(ProductCategory.frutas).first()

            result shouldHaveSize 2
        }

        @Test
        @DisplayName("should insert, update, and retrieve product")
        fun `should insert, update, and retrieve product`() = runBlocking {
            val originalProduct = createSampleProduct(id = 1, name = "Original")
            val updatedProduct = createSampleProduct(id = 1, name = "Actualizado")
            
            coEvery { productDao.insertProduct(originalProduct) } just Runs
            coEvery { productDao.updateProduct(updatedProduct) } just Runs
            coEvery { productDao.getProductById(1) } returns updatedProduct

            productDao.insertProduct(originalProduct)
            productDao.updateProduct(updatedProduct)
            val result = productDao.getProductById(1)

            result?.name shouldBe "Actualizado"
        }

        @Test
        @DisplayName("should insert, search, and find products")
        fun `should insert, search, and find products`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, name = "Manzana Roja"),
                createSampleProduct(id = 2, name = "Manzana Verde")
            )
            coEvery { productDao.insertProducts(products) } just Runs
            every { productDao.searchProducts("Manzana") } returns flowOf(products)

            productDao.insertProducts(products)
            val result = productDao.searchProducts("Manzana").first()

            result shouldHaveSize 2
        }

        @Test
        @DisplayName("should delete product and verify removal")
        fun `should delete product and verify removal`() = runBlocking {
            val product = createSampleProduct(id = 1)
            coEvery { productDao.deleteProduct(product) } just Runs
            coEvery { productDao.getProductById(1) } returns null

            productDao.deleteProduct(product)
            val result = productDao.getProductById(1)

            result.shouldBeNull()
        }

        @Test
        @DisplayName("should delete all products and verify empty")
        fun `should delete all products and verify empty`() = runBlocking {
            coEvery { productDao.deleteAllProducts() } just Runs
            every { productDao.getAllProducts() } returns flowOf(emptyList())

            productDao.deleteAllProducts()
            val result = productDao.getAllProducts().first()

            result.shouldBeEmpty()
        }
    }

    @Nested
    @DisplayName("Edge Cases and Data Validation")
    inner class EdgeCasesTests {

        @Test
        @DisplayName("should handle product with special characters in name")
        fun `should handle product with special characters in name`() = runBlocking {
            val product = createSampleProduct(name = "Piña & Coco 100%")
            coEvery { productDao.insertProduct(product) } just Runs

            productDao.insertProduct(product)

            coVerify { productDao.insertProduct(match { it.name.contains("&") }) }
        }

        @Test
        @DisplayName("should handle product with very long name")
        fun `should handle product with very long name`() = runBlocking {
            val longName = "Producto con un nombre extremadamente largo ".repeat(5)
            val product = createSampleProduct(name = longName)
            coEvery { productDao.insertProduct(product) } just Runs

            productDao.insertProduct(product)

            coVerify { productDao.insertProduct(match { it.name.length > 100 }) }
        }

        @Test
        @DisplayName("should handle product with empty price")
        fun `should handle product with empty price`() = runBlocking {
            val product = createSampleProduct(price = "")
            coEvery { productDao.insertProduct(product) } just Runs

            productDao.insertProduct(product)

            coVerify { productDao.insertProduct(match { it.price.isEmpty() }) }
        }

        @Test
        @DisplayName("should handle product with large image resource id")
        fun `should handle product with large image resource id`() = runBlocking {
            val product = createSampleProduct(imagesRes = Int.MAX_VALUE)
            coEvery { productDao.insertProduct(product) } just Runs

            productDao.insertProduct(product)

            coVerify { productDao.insertProduct(match { it.imagesRes == Int.MAX_VALUE }) }
        }

        @Test
        @DisplayName("should handle multiple products with same name")
        fun `should handle multiple products with same name`() = runBlocking {
            val products = listOf(
                createSampleProduct(id = 1, name = "Manzana"),
                createSampleProduct(id = 2, name = "Manzana")
            )
            coEvery { productDao.insertProducts(products) } just Runs

            productDao.insertProducts(products)

            coVerify { productDao.insertProducts(match { it.size == 2 }) }
        }
    }
}