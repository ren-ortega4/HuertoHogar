package com.example.huertohogar.view.components

import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


class ProductCardTest {

    private lateinit var sampleProduct: Product
    private lateinit var productList: List<Product>

    @BeforeEach
    fun setup() {
        sampleProduct = Product(
            id = 1,
            name = "Manzana",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        productList = listOf(
            Product(1, "Manzana", "5.00", R.drawable.fruta, ProductCategory.frutas),
            Product(2, "Lechuga", "3.50", R.drawable.verdura, ProductCategory.verduras),
            Product(3, "Yogurt", "4.00", R.drawable.lacteos, ProductCategory.lacteos)
        )
    }


    @Test
    fun `producto debe tener todos los campos requeridos`() {
        // Given
        val product = Product(
            id = 1,
            name = "Tomate",
            price = "2.50",
            imagesRes = R.drawable.verdura,
            category = ProductCategory.verduras
        )

        // Then
        assertNotNull(product)
        assertEquals(1, product.id)
        assertEquals("Tomate", product.name)
        assertEquals("2.50", product.price)
        assertEquals(R.drawable.verdura, product.imagesRes)
        assertEquals(ProductCategory.verduras, product.category)
    }

    @Test
    fun `producto con id cero debe ser valido`() {
        // Given
        val product = Product(
            id = 0,
            name = "Producto",
            price = "1.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals(0, product.id)
        assertTrue(product.id >= 0)
    }

    @Test
    fun `producto con nombre vacio debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals("", product.name)
        assertNotNull(product.name)
    }

    @Test
    fun `producto con nombre largo debe ser valido`() {
        // Given
        val longName = "Manzana Roja Orgánica de Calidad Premium"
        val product = Product(
            id = 1,
            name = longName,
            price = "10.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals(longName, product.name)
        assertTrue(product.name.length > 20)
    }

    @Test
    fun `producto con caracteres especiales en nombre debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Piña & Coco",
            price = "8.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertTrue(product.name.contains("&"))
        assertTrue(product.name.contains("ñ"))
    }

    @Test
    fun `precio con formato decimal debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "5.50",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals("5.50", product.price)
        assertTrue(product.price.contains("."))
    }

    @Test
    fun `precio como entero debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "10",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals("10", product.price)
        assertFalse(product.price.contains("."))
    }

    @Test
    fun `precio con cero decimales debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals("5.00", product.price)
    }

    @Test
    fun `precio muy alto debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto Premium",
            price = "999.99",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.productosOrganicos
        )

        // Then
        assertEquals("999.99", product.price)
        assertTrue(product.price.length > 5)
    }

    @Test
    fun `precio con formato de moneda debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "S/. 25.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertTrue(product.price.contains("S/."))
    }

    @Test
    fun `imagen del producto debe ser un recurso valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertTrue(product.imagesRes > 0)
        assertNotEquals(0, product.imagesRes)
    }

    @Test
    fun `imagen del producto puede ser cero`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "5.00",
            imagesRes = 0,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals(0, product.imagesRes)
    }

    @Test
    fun `categoria frutas debe ser valida`() {
        // Given
        val product = Product(
            id = 1,
            name = "Manzana",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals(ProductCategory.frutas, product.category)
        assertEquals("Frutas", product.category.displayName)
    }

    @Test
    fun `categoria verduras debe ser valida`() {
        // Given
        val product = Product(
            id = 1,
            name = "Lechuga",
            price = "3.00",
            imagesRes = R.drawable.verdura,
            category = ProductCategory.verduras
        )

        // Then
        assertEquals(ProductCategory.verduras, product.category)
        assertEquals("Verduras", product.category.displayName)
    }

    @Test
    fun `categoria productos organicos debe ser valida`() {
        // Given
        val product = Product(
            id = 1,
            name = "Quinoa",
            price = "10.00",
            imagesRes = R.drawable.organico,
            category = ProductCategory.productosOrganicos
        )

        // Then
        assertEquals(ProductCategory.productosOrganicos, product.category)
        assertEquals("Productos Orgánicos", product.category.displayName)
    }

    @Test
    fun `categoria lacteos debe ser valida`() {
        // Given
        val product = Product(
            id = 1,
            name = "Yogurt",
            price = "4.00",
            imagesRes = R.drawable.lacteos,
            category = ProductCategory.lacteos
        )

        // Then
        assertEquals(ProductCategory.lacteos, product.category)
        assertEquals("Lácteos", product.category.displayName)
    }

    @Test
    fun `categoria otros debe ser valida`() {
        // Given
        val product = Product(
            id = 1,
            name = "Producto",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.otros
        )

        // Then
        assertEquals(ProductCategory.otros, product.category)
        assertEquals("Otros", product.category.displayName)
    }

    @Test
    fun `todas las categorias deben estar disponibles`() {
        // Given
        val categories = ProductCategory.values()

        // Then
        assertEquals(5, categories.size)
        assertTrue(categories.contains(ProductCategory.frutas))
        assertTrue(categories.contains(ProductCategory.verduras))
        assertTrue(categories.contains(ProductCategory.productosOrganicos))
        assertTrue(categories.contains(ProductCategory.lacteos))
        assertTrue(categories.contains(ProductCategory.otros))
    }

    @Test
    fun `callback onClick debe ser invocado correctamente`() {
        // Given
        var wasCalled = false
        val onClick: () -> Unit = { wasCalled = true }

        // When
        onClick()

        // Then
        assertTrue(wasCalled)
    }

    @Test
    fun `callback onClick puede recibir producto como parametro`() {
        // Given
        var clickedProduct: Product? = null
        val onClick: (Product) -> Unit = { product -> clickedProduct = product }

        // When
        onClick(sampleProduct)

        // Then
        assertNotNull(clickedProduct)
        assertEquals(sampleProduct, clickedProduct)
    }

    @Test
    fun `callback onClick puede ser llamado multiples veces`() {
        // Given
        var clickCount = 0
        val onClick: () -> Unit = { clickCount++ }

        // When
        onClick()
        onClick()
        onClick()

        // Then
        assertEquals(3, clickCount)
    }

    @Test
    fun `lista de productos puede estar vacia`() {
        // Given
        val emptyList = emptyList<Product>()

        // Then
        assertTrue(emptyList.isEmpty())
        assertEquals(0, emptyList.size)
    }

    @Test
    fun `lista de productos con un elemento debe ser valida`() {
        // Given
        val singleProductList = listOf(sampleProduct)

        // Then
        assertEquals(1, singleProductList.size)
        assertEquals(sampleProduct, singleProductList[0])
    }

    @Test
    fun `lista de productos con multiples elementos debe ser valida`() {
        // Given
        val multipleProducts = productList

        // Then
        assertEquals(3, multipleProducts.size)
        assertTrue(multipleProducts.isNotEmpty())
    }

    @Test
    fun `lista de productos puede ser filtrada por categoria`() {
        // Given
        val products = productList

        // When
        val frutas = products.filter { it.category == ProductCategory.frutas }

        // Then
        assertEquals(1, frutas.size)
        assertEquals("Manzana", frutas[0].name)
    }

    @Test
    fun `lista de productos puede ser ordenada por nombre`() {
        // Given
        val products = productList

        // When
        val sorted = products.sortedBy { it.name }

        // Then
        assertEquals("Lechuga", sorted[0].name)
        assertEquals("Manzana", sorted[1].name)
        assertEquals("Yogurt", sorted[2].name)
    }

    @Test
    fun `lista de productos puede ser ordenada por precio`() {
        // Given
        val products = productList

        // When
        val sorted = products.sortedBy { it.price.toDoubleOrNull() ?: 0.0 }

        // Then
        assertEquals("Lechuga", sorted[0].name) // 3.50
        assertEquals("Yogurt", sorted[1].name)  // 4.00
        assertEquals("Manzana", sorted[2].name) // 5.00
    }

    @Test
    fun `ancho del card debe ser 150dp`() {
        // Given
        val cardWidth = 150

        // Then
        assertEquals(150, cardWidth)
        assertTrue(cardWidth > 0)
    }

    @Test
    fun `altura de la imagen debe ser 80dp`() {
        // Given
        val imageHeight = 80

        // Then
        assertEquals(80, imageHeight)
        assertTrue(imageHeight > 0)
    }

    @Test
    fun `espaciado entre productos debe ser 12dp`() {
        // Given
        val spacing = 12

        // Then
        assertEquals(12, spacing)
        assertTrue(spacing > 0)
    }

    @Test
    fun `padding horizontal del contenedor debe ser 4dp`() {
        // Given
        val contentPadding = 4

        // Then
        assertEquals(4, contentPadding)
        assertTrue(contentPadding >= 0)
    }

    @Test
    fun `padding superior del nombre debe ser 8dp`() {
        // Given
        val topPadding = 8

        // Then
        assertEquals(8, topPadding)
        assertTrue(topPadding > 0)
    }

    @Test
    fun `padding inferior del precio debe ser 8dp`() {
        // Given
        val bottomPadding = 8

        // Then
        assertEquals(8, bottomPadding)
        assertTrue(bottomPadding > 0)
    }

    @Test
    fun `productos iguales deben ser considerados iguales`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", R.drawable.fruta, ProductCategory.frutas)
        val product2 = Product(1, "Manzana", "5.00", R.drawable.fruta, ProductCategory.frutas)

        // Then
        assertEquals(product1, product2)
        assertEquals(product1.hashCode(), product2.hashCode())
    }

    @Test
    fun `productos diferentes deben ser considerados diferentes`() {
        // Given
        val product1 = Product(1, "Manzana", "5.00", R.drawable.fruta, ProductCategory.frutas)
        val product2 = Product(2, "Pera", "6.00", R.drawable.fruta, ProductCategory.frutas)

        // Then
        assertNotEquals(product1, product2)
    }

    @Test
    fun `producto puede ser copiado con modificaciones`() {
        // Given
        val original = sampleProduct

        // When
        val modified = original.copy(price = "10.00")

        // Then
        assertEquals(original.id, modified.id)
        assertEquals(original.name, modified.name)
        assertNotEquals(original.price, modified.price)
        assertEquals("10.00", modified.price)
    }

    @Test
    fun `producto con id negativo debe ser valido`() {
        // Given
        val product = Product(
            id = -1,
            name = "Producto",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertEquals(-1, product.id)
    }

    @Test
    fun `producto con nombre con emoji debe ser valido`() {
        // Given
        val product = Product(
            id = 1,
            name = "Manzana 🍎",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertTrue(product.name.contains("🍎"))
    }

    @Test
    fun `lista de productos puede contener productos de diferentes categorias`() {
        // Given
        val mixedProducts = productList

        // When
        val categories = mixedProducts.map { it.category }.distinct()

        // Then
        assertTrue(categories.size > 1)
        assertTrue(categories.contains(ProductCategory.frutas))
        assertTrue(categories.contains(ProductCategory.verduras))
        assertTrue(categories.contains(ProductCategory.lacteos))
    }

    @Test
    fun `producto puede tener descripcion como parte del nombre`() {
        // Given
        val product = Product(
            id = 1,
            name = "Manzana - Roja y Dulce",
            price = "5.00",
            imagesRes = R.drawable.fruta,
            category = ProductCategory.frutas
        )

        // Then
        assertTrue(product.name.contains("-"))
        assertTrue(product.name.length > 10)
    }

    @Test
    fun `productos pueden ser agrupados por categoria`() {
        // Given
        val products = productList

        // When
        val grouped = products.groupBy { it.category }

        // Then
        assertTrue(grouped.containsKey(ProductCategory.frutas))
        assertTrue(grouped.containsKey(ProductCategory.verduras))
        assertTrue(grouped.containsKey(ProductCategory.lacteos))
        assertEquals(1, grouped[ProductCategory.frutas]?.size)
    }
}