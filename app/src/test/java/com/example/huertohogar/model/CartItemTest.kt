package com.example.huertohogar.model

import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("CartItem Tests")
class CartItemTest {
    
    private fun createSampleProduct(
        id: Int = 1,
        name: String = "Manzana",
        price: String = "$1.500",
        imagesRes: Int = 123,
        category: ProductCategory = ProductCategory.frutas
    ) = Product(
        id = id,
        name = name,
        price = price,
        imagesRes = imagesRes,
        category = category
    )

    private fun createCartItem(
        product: Product = createSampleProduct(),
        quantity: Int = 1
    ) = CartItem(
        product = product,
        quantity = quantity
    )

    @Nested
    @DisplayName("Constructor and Properties Tests")
    inner class ConstructorTests {

        @Test
        @DisplayName("should create CartItem with product and quantity")
        fun `should create CartItem with product and quantity`() {
            val product = createSampleProduct(name = "Banana")
            val cartItem = CartItem(product, 3)

            cartItem.product shouldBe product
            cartItem.quantity shouldBe 3
        }

        @Test
        @DisplayName("should create CartItem with quantity 1")
        fun `should create CartItem with quantity 1`() {
            val product = createSampleProduct()
            val cartItem = CartItem(product, 1)

            cartItem.quantity shouldBe 1
        }

        @Test
        @DisplayName("should create CartItem with any product")
        fun `should create CartItem with any product`() {
            val product = createSampleProduct(
                id = 10,
                name = "Zanahoria",
                price = "$900/Kg",
                category = ProductCategory.verduras
            )
            val cartItem = CartItem(product, 2)

            cartItem.product.name shouldBe "Zanahoria"
            cartItem.product.category shouldBe ProductCategory.verduras
        }

        @Test
        @DisplayName("should allow zero quantity")
        fun `should allow zero quantity`() {
            val product = createSampleProduct()
            val cartItem = CartItem(product, 0)

            cartItem.quantity shouldBe 0
        }

        @Test
        @DisplayName("should allow negative quantity")
        fun `should allow negative quantity`() {
            val product = createSampleProduct()
            val cartItem = CartItem(product, -1)

            cartItem.quantity shouldBe -1
        }

        @Test
        @DisplayName("should allow large quantity")
        fun `should allow large quantity`() {
            val product = createSampleProduct()
            val cartItem = CartItem(product, 1000)

            cartItem.quantity shouldBe 1000
        }
    }

    @Nested
    @DisplayName("Subtotal Calculation Tests")
    inner class SubtotalCalculationTests {

        @Test
        @DisplayName("subtotal should calculate correctly for simple price")
        fun `subtotal should calculate correctly for simple price`() {
            val product = createSampleProduct(price = "$1.500")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000.0
        }

        @Test
        @DisplayName("subtotal should calculate correctly for price with /Kg")
        fun `subtotal should calculate correctly for price with Kg`() {
            val product = createSampleProduct(price = "$900/Kg")
            val cartItem = CartItem(product, 3)

            cartItem.subtotal shouldBe 2700.0
        }

        @Test
        @DisplayName("subtotal should calculate correctly for price with /bolsa")
        fun `subtotal should calculate correctly for price with bolsa`() {
            val product = createSampleProduct(price = "$700/bolsa")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 1400.0
        }

        @Test
        @DisplayName("subtotal should handle price without decimal point")
        fun `subtotal should handle price without decimal point`() {
            val product = createSampleProduct(price = "$3800")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 3800.0
        }

        @Test
        @DisplayName("subtotal should be zero when quantity is zero")
        fun `subtotal should be zero when quantity is zero`() {
            val product = createSampleProduct(price = "$1.500")
            val cartItem = CartItem(product, 0)

            cartItem.subtotal shouldBe 0.0
        }

        @Test
        @DisplayName("subtotal should be negative when quantity is negative")
        fun `subtotal should be negative when quantity is negative`() {
            val product = createSampleProduct(price = "$1.000")
            val cartItem = CartItem(product, -2)

            cartItem.subtotal shouldBe -2000.0
        }

        @Test
        @DisplayName("subtotal should calculate for quantity 1")
        fun `subtotal should calculate for quantity 1`() {
            val product = createSampleProduct(price = "$5.000")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 5000.0
        }

        @Test
        @DisplayName("subtotal should calculate for large quantity")
        fun `subtotal should calculate for large quantity`() {
            val product = createSampleProduct(price = "$1.000")
            val cartItem = CartItem(product, 100)

            cartItem.subtotal shouldBe 100000.0
        }

        @Test
        @DisplayName("subtotal should handle price with spaces")
        fun `subtotal should handle price with spaces`() {
            val product = createSampleProduct(price = "$ 1.500 ")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000.0
        }

        @Test
        @DisplayName("subtotal should remove dollar sign and dots correctly")
        fun `subtotal should remove dollar sign and dots correctly`() {
            val product = createSampleProduct(price = "$10.500")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 10500.0
        }

        @Test
        @DisplayName("subtotal should handle multiple dots in price")
        fun `subtotal should handle multiple dots in price`() {
            val product = createSampleProduct(price = "$1.000.000")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 1000000.0
        }

        @Test
        @DisplayName("subtotal should split by forward slash correctly")
        fun `subtotal should split by forward slash correctly`() {
            val product = createSampleProduct(price = "$1.200/Kg")
            val cartItem = CartItem(product, 5)

            cartItem.subtotal shouldBe 6000.0
        }
    }

    @Nested
    @DisplayName("Subtotal Edge Cases Tests")
    inner class SubtotalEdgeCasesTests {

        @Test
        @DisplayName("subtotal should return 0.0 for invalid price format")
        fun `subtotal should return 0 for invalid price format`() {
            val product = createSampleProduct(price = "invalid")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 0.0
        }

        @Test
        @DisplayName("subtotal should return 0.0 for empty price")
        fun `subtotal should return 0 for empty price`() {
            val product = createSampleProduct(price = "")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 0.0
        }

        @Test
        @DisplayName("subtotal should return 0.0 for price with only dollar sign")
        fun `subtotal should return 0 for price with only dollar sign`() {
            val product = createSampleProduct(price = "$")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 0.0
        }

        @Test
        @DisplayName("subtotal should return 0.0 for price with letters")
        fun `subtotal should return 0 for price with letters`() {
            val product = createSampleProduct(price = "0")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 0.0
        }

        @Test
        @DisplayName("subtotal should handle price with only numbers")
        fun `subtotal should handle price with only numbers`() {
            val product = createSampleProduct(price = "1500")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000.0
        }

        @Test
        @DisplayName("subtotal should handle price with special characters")
        fun `subtotal should handle price with special characters`() {
            val product = createSampleProduct(price = "$@1.500!")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 0.0
        }

        @Test
        @DisplayName("subtotal should handle price ending with slash")
        fun `subtotal should handle price ending with slash`() {
            val product = createSampleProduct(price = "$1.500/")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000.0
        }

        @Test
        @DisplayName("subtotal should handle price with multiple slashes")
        fun `subtotal should handle price with multiple slashes`() {
            val product = createSampleProduct(price = "$1.500/Kg/pack")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000.0
        }

        @Test
        @DisplayName("subtotal should be computed property")
        fun `subtotal should be computed property`() {
            val product = createSampleProduct(price = "$1.000")
            val cartItem = CartItem(product, 3)

            val subtotal1 = cartItem.subtotal
            val subtotal2 = cartItem.subtotal

            subtotal1 shouldBe subtotal2
            subtotal1 shouldBe 3000.0
        }
    }

    @Nested
    @DisplayName("Data Class Functionality Tests")
    inner class DataClassFunctionalityTests {

        @Test
        @DisplayName("should support copy with modified quantity")
        fun `should support copy with modified quantity`() {
            val product = createSampleProduct()
            val cartItem = CartItem(product, 2)
            val copied = cartItem.copy(quantity = 5)

            copied.quantity shouldBe 5
            copied.product shouldBe product
            cartItem.quantity shouldBe 2 // Original unchanged
        }

        @Test
        @DisplayName("should support copy with modified product")
        fun `should support copy with modified product`() {
            val product1 = createSampleProduct(name = "Product 1")
            val product2 = createSampleProduct(name = "Product 2")
            val cartItem = CartItem(product1, 2)
            val copied = cartItem.copy(product = product2)

            copied.product shouldBe product2
            copied.quantity shouldBe 2
            cartItem.product shouldBe product1 // Original unchanged
        }

        @Test
        @DisplayName("should support equals comparison")
        fun `should support equals comparison`() {
            val product = createSampleProduct()
            val cartItem1 = CartItem(product, 2)
            val cartItem2 = CartItem(product, 2)

            (cartItem1 == cartItem2) shouldBe true
        }

        @Test
        @DisplayName("should not be equal with different quantity")
        fun `should not be equal with different quantity`() {
            val product = createSampleProduct()
            val cartItem1 = CartItem(product, 2)
            val cartItem2 = CartItem(product, 3)

            (cartItem1 == cartItem2) shouldBe false
        }

        @Test
        @DisplayName("should not be equal with different product")
        fun `should not be equal with different product`() {
            val product1 = createSampleProduct(id = 1)
            val product2 = createSampleProduct(id = 2)
            val cartItem1 = CartItem(product1, 2)
            val cartItem2 = CartItem(product2, 2)

            (cartItem1 == cartItem2) shouldBe false
        }

        @Test
        @DisplayName("should support hashCode")
        fun `should support hashCode`() {
            val product = createSampleProduct()
            val cartItem1 = CartItem(product, 2)
            val cartItem2 = CartItem(product, 2)

            cartItem1.hashCode() shouldBe cartItem2.hashCode()
        }

        @Test
        @DisplayName("should have different hashCode for different items")
        fun `should have different hashCode for different items`() {
            val product = createSampleProduct()
            val cartItem1 = CartItem(product, 2)
            val cartItem2 = CartItem(product, 3)

            cartItem1.hashCode() shouldNotBe cartItem2.hashCode()
        }

        @Test
        @DisplayName("should support toString")
        fun `should support toString`() {
            val product = createSampleProduct(name = "Test Product")
            val cartItem = CartItem(product, 2)

            val toString = cartItem.toString()

            toString.shouldBeInstanceOf<String>()
            toString.length shouldBeGreaterThan 0
        }

        @Test
        @DisplayName("should support destructuring")
        fun `should support destructuring`() {
            val product = createSampleProduct(name = "Test")
            val cartItem = CartItem(product, 5)

            val (p, q) = cartItem

            p shouldBe product
            q shouldBe 5
        }

        @Test
        @DisplayName("should be immutable through copy")
        fun `should be immutable through copy`() {
            val product = createSampleProduct()
            val original = CartItem(product, 2)
            val modified = original.copy(quantity = 10)

            original.quantity shouldBe 2
            modified.quantity shouldBe 10
        }
    }

    @Nested
    @DisplayName("Real-world Scenarios Tests")
    inner class RealWorldScenariosTests {

        @Test
        @DisplayName("should calculate subtotal for milk product")
        fun `should calculate subtotal for milk product`() {
            val product = createSampleProduct(
                name = "Leche Natural",
                price = "$3.800",
                category = ProductCategory.lacteos
            )
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 7600.0
        }

        @Test
        @DisplayName("should calculate subtotal for honey product")
        fun `should calculate subtotal for honey product`() {
            val product = createSampleProduct(
                name = "Miel Orgánica",
                price = "$5.000",
                category = ProductCategory.productosOrganicos
            )
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 5000.0
        }

        @Test
        @DisplayName("should calculate subtotal for bananas with unit")
        fun `should calculate subtotal for bananas with unit`() {
            val product = createSampleProduct(
                name = "Plátanos Cavendish",
                price = "$800/Kg",
                category = ProductCategory.frutas
            )
            val cartItem = CartItem(product, 5)

            cartItem.subtotal shouldBe 4000.0
        }

        @Test
        @DisplayName("should calculate subtotal for apples")
        fun `should calculate subtotal for apples`() {
            val product = createSampleProduct(
                name = "Manzanas Fuji",
                price = "$1.200/Kg",
                category = ProductCategory.frutas
            )
            val cartItem = CartItem(product, 3)

            cartItem.subtotal shouldBe 3600.0
        }

        @Test
        @DisplayName("should calculate subtotal for organic carrots")
        fun `should calculate subtotal for organic carrots`() {
            val product = createSampleProduct(
                name = "Zanahorias orgánicas",
                price = "$900/Kg",
                category = ProductCategory.verduras
            )
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 1800.0
        }

        @Test
        @DisplayName("should calculate subtotal for spinach with bag unit")
        fun `should calculate subtotal for spinach with bag unit`() {
            val product = createSampleProduct(
                name = "Espinacas Frescas",
                price = "$700/bolsa",
                category = ProductCategory.verduras
            )
            val cartItem = CartItem(product, 4)

            cartItem.subtotal shouldBe 2800.0
        }

        @Test
        @DisplayName("should handle cart with single item")
        fun `should handle cart with single item`() {
            val product = createSampleProduct(price = "$2.500")
            val cartItem = CartItem(product, 1)

            cartItem.quantity shouldBe 1
            cartItem.subtotal shouldBe 2500.0
        }

        @Test
        @DisplayName("should handle bulk purchase")
        fun `should handle bulk purchase`() {
            val product = createSampleProduct(price = "$1.000/Kg")
            val cartItem = CartItem(product, 50)

            cartItem.quantity shouldBe 50
            cartItem.subtotal shouldBe 50000.0
        }

        @Test
        @DisplayName("should calculate subtotal correctly for different products")
        fun `should calculate subtotal correctly for different products`() {
            val product1 = createSampleProduct(name = "Product 1", price = "$1.500")
            val product2 = createSampleProduct(name = "Product 2", price = "$2.000")
            
            val cartItem1 = CartItem(product1, 3)
            val cartItem2 = CartItem(product2, 2)

            cartItem1.subtotal shouldBe 4500.0
            cartItem2.subtotal shouldBe 4000.0
        }
    }

    @Nested
    @DisplayName("Price Parsing Tests")
    inner class PriceParsingTests {

        @Test
        @DisplayName("should parse price with thousand separator")
        fun `should parse price with thousand separator`() {
            val product = createSampleProduct(price = "$10.500")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 10500.0
        }

        @Test
        @DisplayName("should parse price with multiple thousand separators")
        fun `should parse price with multiple thousand separators`() {
            val product = createSampleProduct(price = "$1.500.000")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000000.0
        }

        @Test
        @DisplayName("should ignore text after slash")
        fun `should ignore text after slash`() {
            val product1 = createSampleProduct(price = "$1.000/Kg")
            val product2 = createSampleProduct(price = "$1.000/unidad")
            val product3 = createSampleProduct(price = "$1.000/paquete")

            val cartItem1 = CartItem(product1, 1)
            val cartItem2 = CartItem(product2, 1)
            val cartItem3 = CartItem(product3, 1)

            cartItem1.subtotal shouldBe 1000.0
            cartItem2.subtotal shouldBe 1000.0
            cartItem3.subtotal shouldBe 1000.0
        }

        @Test
        @DisplayName("should trim whitespace from price")
        fun `should trim whitespace from price`() {
            val product = createSampleProduct(price = "  $1.500  ")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal shouldBe 3000.0
        }

        @Test
        @DisplayName("should handle price with tabs and newlines")
        fun `should handle price with tabs and newlines`() {
            val product = createSampleProduct(price = "$1.500\n")
            val cartItem = CartItem(product, 1)

            cartItem.subtotal shouldBe 1500.0
        }

        @Test
        @DisplayName("subtotal should return Double type")
        fun `subtotal should return Double type`() {
            val product = createSampleProduct(price = "$1.500")
            val cartItem = CartItem(product, 2)

            cartItem.subtotal.shouldBeInstanceOf<Double>()
        }

        @Test
        @DisplayName("subtotal should handle zero price")
        fun `subtotal should handle zero price`() {
            val product = createSampleProduct(price = "$0")
            val cartItem = CartItem(product, 5)

            cartItem.subtotal shouldBe 0.0
        }
    }

    @Nested
    @DisplayName("Quantity Validation Tests")
    inner class QuantityValidationTests {

        @Test
        @DisplayName("quantity should be exactly as specified")
        fun `quantity should be exactly as specified`() {
            val product = createSampleProduct()
            val quantities = listOf(1, 2, 5, 10, 50, 100)

            quantities.forEach { qty ->
                val cartItem = CartItem(product, qty)
                cartItem.quantity shouldBe qty
            }
        }

        @Test
        @DisplayName("should maintain quantity in calculations")
        fun `should maintain quantity in calculations`() {
            val product = createSampleProduct(price = "$100")
            val cartItem = CartItem(product, 7)

            cartItem.subtotal shouldBe 700.0
            cartItem.quantity shouldBe 7
        }

        @Test
        @DisplayName("should allow maximum integer quantity")
        fun `should allow maximum integer quantity`() {
            val product = createSampleProduct(price = "$1")
            val cartItem = CartItem(product, Int.MAX_VALUE)

            cartItem.quantity shouldBe Int.MAX_VALUE
        }
    }
} 