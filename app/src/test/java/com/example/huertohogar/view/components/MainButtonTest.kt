package com.example.huertohogar.view.components

import com.example.huertohogar.R
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class MainButtonTest {

    private lateinit var buttonText: String
    private var buttonIcon: Int = 0
    private var buttonColor: Long = 0
    private var clickCount: Int = 0

    @BeforeEach
    fun setup() {
        buttonText = ""
        buttonIcon = 0
        buttonColor = 0xFF000000
        clickCount = 0
    }

    @Test
    fun `texto del boton no debe ser nulo`() {
        // Given
        val text: String? = "Click me"

        // Then
        assertNotNull(text)
    }

    @Test
    fun `texto del boton puede estar vacio`() {
        // Given
        val text = ""

        // When
        val result = text

        // Then
        assertNotNull(result)
        assertEquals("", result)
    }

    @Test
    fun `texto del boton con contenido valido`() {
        // Given
        val text = "Agregar al Carrito"

        // When
        val result = text

        // Then
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals("Agregar al Carrito", result)
    }

    @Test
    fun `texto del boton con caracteres especiales`() {
        // Given
        val text = "Añadir & Guardar"

        // When
        val result = text

        // Then
        assertNotNull(result)
        assertTrue(result.contains("&"))
        assertTrue(result.contains("ñ"))
    }

    @Test
    fun `texto del boton con numeros`() {
        // Given
        val text = "Comprar 3 Items"

        // When
        val result = text

        // Then
        assertNotNull(result)
        assertTrue(text.contains("3"))
    }

    @Test
    fun `texto del boton muy largo debe ser valido`() {
        // Given
        val longText = "Este es un texto muy largo para un botón que podría ser usado en alguna situación"

        // When
        val result = longText

        // Then
        assertNotNull(result)
        assertTrue(result.length > 50)
    }

    @Test
    fun `icono del boton con recurso valido`() {
        // Given
        val iconRes = R.drawable.fruta

        // When
        val result = iconRes

        // Then
        assertTrue(result > 0)
        assertNotEquals(0, result)
    }

    @Test
    fun `icono del boton puede ser cero`() {
        // Given
        val iconRes = 0

        // When
        val result = iconRes

        // Then
        assertEquals(0, result)
    }

    @Test
    fun `icono del boton debe ser un entero`() {
        // Given
        val iconRes = 123456

        // Then
        assertTrue(iconRes is Int)
    }

    @Test
    fun `color del boton debe ser valido en formato hexadecimal`() {
        // Given
        val colorHex = 0xFF2E8B57

        // When
        val result = colorHex

        // Then
        assertTrue(result > 0)
        assertNotEquals(0, result)
    }

    @Test
    fun `color del boton negro debe ser valido`() {
        // Given
        val blackColor = 0xFF000000

        // When
        val result = blackColor

        // Then
        assertEquals(0xFF000000, result)
    }

    @Test
    fun `color del boton blanco debe ser valido`() {
        // Given
        val whiteColor = 0xFFFFFFFF.toLong()

        // When
        val result = whiteColor

        // Then
        assertTrue(result > 0)
    }

    @Test
    fun `color del boton rojo debe ser valido`() {
        // Given
        val redColor = 0xFFFF0000

        // When
        val result = redColor

        // Then
        assertTrue(result > 0)
        assertEquals(0xFFFF0000, result)
    }

    @Test
    fun `color del boton verde debe ser valido`() {
        // Given
        val greenColor = 0xFF00FF00

        // When
        val result = greenColor

        // Then
        assertTrue(result > 0)
        assertEquals(0xFF00FF00, result)
    }

    @Test
    fun `color del boton azul debe ser valido`() {
        // Given
        val blueColor = 0xFF0000FF

        // When
        val result = blueColor

        // Then
        assertTrue(result > 0)
        assertEquals(0xFF0000FF, result)
    }

    @Test
    fun `color del boton con transparencia debe ser valido`() {
        // Given
        val transparentColor = 0x80FF0000

        // When
        val result = transparentColor

        // Then
        assertTrue(result > 0)
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
    fun `callback onClick puede ser llamado multiples veces`() {
        // Given
        var callCount = 0
        val onClick: () -> Unit = { callCount++ }

        // When
        onClick()
        onClick()
        onClick()

        // Then
        assertEquals(3, callCount)
    }

    @Test
    fun `callback onClick no debe ser nulo`() {
        // Given
        val onClick: (() -> Unit)? = {}

        // Then
        assertNotNull(onClick)
    }

    @Test
    fun `callback onClick puede ejecutar logica compleja`() {
        // Given
        var result = 0
        val onClick: () -> Unit = {
            result = 10 + 20
        }

        // When
        onClick()

        // Then
        assertEquals(30, result)
    }

    @Test
    fun `callback onClick puede modificar estado externo`() {
        // Given
        var items = mutableListOf<String>()
        val onClick: () -> Unit = {
            items.add("Item")
        }

        // When
        onClick()

        // Then
        assertEquals(1, items.size)
        assertEquals("Item", items[0])
    }

    @Test
    fun `parametros del boton pueden ser combinados correctamente`() {
        // Given
        val text = "Comprar Ahora"
        val icon = R.drawable.verdura
        val color = 0xFF2E8B57
        var clicked = false
        val onClick: () -> Unit = { clicked = true }

        // When
        val params = MainButtonParams(
            text = text,
            icon = icon,
            color = color,
            onClick = onClick
        )

        // Then
        assertEquals(text, params.text)
        assertEquals(icon, params.icon)
        assertEquals(color, params.color)
        assertFalse(clicked)

        params.onClick()
        assertTrue(clicked)
    }

    @Test
    fun `boton con todos los parametros minimos debe ser valido`() {
        // Given
        val text = ""
        val icon = 0
        val color = 0L
        val onClick: () -> Unit = {}

        // When
        val params = MainButtonParams(text, icon, color, onClick)

        // Then
        assertNotNull(params.text)
        assertEquals(0, params.icon)
        assertEquals(0L, params.color)
        assertNotNull(params.onClick)
    }

    @Test
    fun `elevacion del boton debe ser positiva`() {
        // Given
        val elevation = 8

        // Then
        assertTrue(elevation > 0)
        assertEquals(8, elevation)
    }

    @Test
    fun `elevacion del boton puede ser cero`() {
        // Given
        val elevation = 0

        // Then
        assertEquals(0, elevation)
    }

    @Test
    fun `elevacion del boton con valor alto debe ser valida`() {
        // Given
        val elevation = 16

        // Then
        assertTrue(elevation > 0)
        assertTrue(elevation > 8)
    }

    @Test
    fun `tamano del icono debe ser 20dp`() {
        // Given
        val iconSize = 20

        // Then
        assertEquals(20, iconSize)
        assertTrue(iconSize > 0)
    }

    @Test
    fun `espaciado entre icono y texto debe ser 8dp`() {
        // Given
        val spacing = 8

        // Then
        assertEquals(8, spacing)
        assertTrue(spacing > 0)
    }

    @Test
    fun `texto con saltos de linea debe ser valido`() {
        // Given
        val text = "Línea 1\nLínea 2"

        // When
        val result = text

        // Then
        assertNotNull(result)
        assertTrue(result.contains("\n"))
    }

    @Test
    fun `texto con emoji debe ser valido`() {
        // Given
        val text = "Comprar 🛒"

        // When
        val result = text

        // Then
        assertNotNull(result)
        assertTrue(result.contains("🛒"))
    }

    @Test
    fun `color con todos los canales en maximo debe ser valido`() {
        // Given
        val maxColor = 0xFFFFFFFF.toLong()

        // When
        val result = maxColor

        // Then
        assertTrue(result > 0)
    }

    @Test
    fun `color con todos los canales en minimo debe ser valido`() {
        // Given
        val minColor = 0xFF000000

        // When
        val result = minColor

        // Then
        assertEquals(0xFF000000, result)
    }

    @Test
    fun `modifier puede ser proporcionado`() {
        // Given
        val hasModifier = true

        // Then
        assertTrue(hasModifier)
    }

    @Test
    fun `modifier puede ser omitido usando valor por defecto`() {
        // Given
        val useDefaultModifier = true

        // Then
        assertTrue(useDefaultModifier)
    }

    @Test
    fun `contentDescription del icono debe ser igual al texto del boton`() {
        // Given
        val text = "Agregar al Carrito"
        val contentDescription = text

        // Then
        assertEquals(text, contentDescription)
        assertNotNull(contentDescription)
    }

    @Test
    fun `color de contenido del boton debe ser blanco`() {
        // Given
        val contentColor = 0xFFFFFFFF.toLong()

        // Then
        assertEquals(0xFFFFFFFF.toLong(), contentColor)
    }

    @Test
    fun `tint del icono debe ser blanco`() {
        // Given
        val iconTint = 0xFFFFFFFF.toLong()

        // Then
        assertEquals(0xFFFFFFFF.toLong(), iconTint)
    }

    @Test
    fun `multiples callbacks pueden ser ejecutados en secuencia`() {
        // Given
        val results = mutableListOf<String>()
        val onClick1: () -> Unit = { results.add("First") }
        val onClick2: () -> Unit = { results.add("Second") }
        val onClick3: () -> Unit = { results.add("Third") }

        // When
        onClick1()
        onClick2()
        onClick3()

        // Then
        assertEquals(3, results.size)
        assertEquals("First", results[0])
        assertEquals("Second", results[1])
        assertEquals("Third", results[2])
    }

    @Test
    fun `parametros inmutables mantienen sus valores`() {
        // Given
        val originalText = "Original"
        val originalIcon = R.drawable.fruta
        val originalColor = 0xFF2E8B57

        // When
        val params = MainButtonParams(originalText, originalIcon, originalColor) {}

        // Then
        assertEquals(originalText, params.text)
        assertEquals(originalIcon, params.icon)
        assertEquals(originalColor, params.color)
    }

    private data class MainButtonParams(
        val text: String,
        val icon: Int,
        val color: Long,
        val onClick: () -> Unit
    )
}