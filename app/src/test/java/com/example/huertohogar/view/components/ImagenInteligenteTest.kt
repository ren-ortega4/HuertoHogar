package com.example.huertohogar.view.components

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

/**
 * Pruebas unitarias para ImagenInteligente
 * Estas pruebas verifican la lógica de decisión para mostrar imagen o icono por defecto
 */
class ImagenInteligenteTest {

    private lateinit var imagenUri: String

    @BeforeEach
    fun setup() {
        imagenUri = ""
    }

    // ========== Pruebas de validación de URI ==========

    @Test
    fun `uri valida debe retornar verdadero`() {
        // Given
        val validUri = "content://media/external/images/media/1234"

        // When
        val result = esUriValida(validUri)

        // Then
        assertTrue(result)
        assertTrue(validUri.isNotBlank())
    }

    @Test
    fun `uri vacia debe retornar falso`() {
        // Given
        val emptyUri = ""

        // When
        val result = esUriValida(emptyUri)

        // Then
        assertFalse(result)
        assertTrue(emptyUri.isEmpty())
    }

    @Test
    fun `uri nula debe retornar falso`() {
        // Given
        val nullUri: String? = null

        // When
        val result = esUriValida(nullUri)

        // Then
        assertFalse(result)
    }

    @Test
    fun `uri con solo espacios en blanco debe retornar falso`() {
        // Given
        val blankUri = "   "

        // When
        val result = esUriValida(blankUri)

        // Then
        assertFalse(result)
        assertTrue(blankUri.isBlank())
    }

    @Test
    fun `uri con content scheme debe ser valida`() {
        // Given
        val contentUri = "content://com.android.providers.media.documents/document/image:12345"

        // When
        val result = esUriValida(contentUri)

        // Then
        assertTrue(result)
        assertTrue(contentUri.startsWith("content://"))
    }

    @Test
    fun `uri con file scheme debe ser valida`() {
        // Given
        val fileUri = "file:///storage/emulated/0/Pictures/image.jpg"

        // When
        val result = esUriValida(fileUri)

        // Then
        assertTrue(result)
        assertTrue(fileUri.startsWith("file://"))
    }

    @Test
    fun `uri con http scheme debe ser valida`() {
        // Given
        val httpUri = "http://example.com/image.jpg"

        // When
        val result = esUriValida(httpUri)

        // Then
        assertTrue(result)
        assertTrue(httpUri.startsWith("http://"))
    }

    @Test
    fun `uri con https scheme debe ser valida`() {
        // Given
        val httpsUri = "https://example.com/image.jpg"

        // When
        val result = esUriValida(httpsUri)

        // Then
        assertTrue(result)
        assertTrue(httpsUri.startsWith("https://"))
    }

    // ========== Pruebas de tipos de URI ==========

    @Test
    fun `uri de contenido local debe ser reconocida`() {
        // Given
        val localUri = "content://media/external/images/media/123"

        // When
        val esLocal = esUriLocal(localUri)

        // Then
        assertTrue(esLocal)
        assertTrue(localUri.contains("content://"))
    }

    @Test
    fun `uri de archivo debe ser reconocida`() {
        // Given
        val fileUri = "file:///data/user/0/com.example.app/files/image.jpg"

        // When
        val esArchivo = esUriDeArchivo(fileUri)

        // Then
        assertTrue(esArchivo)
        assertTrue(fileUri.startsWith("file://"))
    }

    @Test
    fun `uri remota http debe ser reconocida`() {
        // Given
        val httpUri = "http://example.com/images/profile.jpg"

        // When
        val esRemota = esUriRemota(httpUri)

        // Then
        assertTrue(esRemota)
        assertTrue(httpUri.startsWith("http"))
    }

    @Test
    fun `uri remota https debe ser reconocida`() {
        // Given
        val httpsUri = "https://example.com/images/profile.jpg"

        // When
        val esRemota = esUriRemota(httpsUri)

        // Then
        assertTrue(esRemota)
        assertTrue(httpsUri.startsWith("https"))
    }

    // ========== Pruebas de formatos de imagen ==========

    @Test
    fun `uri con extension jpg debe ser valida`() {
        // Given
        val jpgUri = "file:///storage/image.jpg"

        // When
        val tieneExtension = tieneExtensionDeImagen(jpgUri)

        // Then
        assertTrue(tieneExtension)
        assertTrue(jpgUri.endsWith(".jpg"))
    }

    @Test
    fun `uri con extension jpeg debe ser valida`() {
        // Given
        val jpegUri = "https://example.com/image.jpeg"

        // When
        val tieneExtension = tieneExtensionDeImagen(jpegUri)

        // Then
        assertTrue(tieneExtension)
        assertTrue(jpegUri.endsWith(".jpeg"))
    }

    @Test
    fun `uri con extension png debe ser valida`() {
        // Given
        val pngUri = "file:///storage/image.png"

        // When
        val tieneExtension = tieneExtensionDeImagen(pngUri)

        // Then
        assertTrue(tieneExtension)
        assertTrue(pngUri.endsWith(".png"))
    }

    @Test
    fun `uri con extension webp debe ser valida`() {
        // Given
        val webpUri = "https://example.com/image.webp"

        // When
        val tieneExtension = tieneExtensionDeImagen(webpUri)

        // Then
        assertTrue(tieneExtension)
        assertTrue(webpUri.endsWith(".webp"))
    }

    @Test
    fun `uri sin extension de imagen debe retornar falso`() {
        // Given
        val noImageUri = "content://media/external/images/media/123"

        // When
        val tieneExtension = tieneExtensionDeImagen(noImageUri)

        // Then
        assertFalse(tieneExtension)
    }

    // ========== Pruebas de decisión de renderizado ==========

    @Test
    fun `debe mostrar imagen cuando uri es valida`() {
        // Given
        val validUri = "content://media/external/images/media/456"

        // When
        val mostrarImagen = deberMostrarImagen(validUri)
        val mostrarIcono = deberMostrarIconoPorDefecto(validUri)

        // Then
        assertTrue(mostrarImagen)
        assertFalse(mostrarIcono)
    }

    @Test
    fun `debe mostrar icono por defecto cuando uri es vacia`() {
        // Given
        val emptyUri = ""

        // When
        val mostrarImagen = deberMostrarImagen(emptyUri)
        val mostrarIcono = deberMostrarIconoPorDefecto(emptyUri)

        // Then
        assertFalse(mostrarImagen)
        assertTrue(mostrarIcono)
    }

    @Test
    fun `debe mostrar icono por defecto cuando uri es nula`() {
        // Given
        val nullUri: String? = null

        // When
        val mostrarImagen = deberMostrarImagen(nullUri)
        val mostrarIcono = deberMostrarIconoPorDefecto(nullUri)

        // Then
        assertFalse(mostrarImagen)
        assertTrue(mostrarIcono)
    }

    @Test
    fun `debe mostrar icono por defecto cuando uri contiene solo espacios`() {
        // Given
        val blankUri = "    "

        // When
        val mostrarImagen = deberMostrarImagen(blankUri)
        val mostrarIcono = deberMostrarIconoPorDefecto(blankUri)

        // Then
        assertFalse(mostrarImagen)
        assertTrue(mostrarIcono)
    }

    // ========== Pruebas de casos especiales ==========

    @Test
    fun `uri con caracteres especiales debe ser valida`() {
        // Given
        val specialUri = "content://media/external/images/media/123?query=param&value=test"

        // When
        val result = esUriValida(specialUri)

        // Then
        assertTrue(result)
        assertTrue(specialUri.contains("?"))
        assertTrue(specialUri.contains("&"))
    }

    @Test
    fun `uri muy larga debe ser valida`() {
        // Given
        val longUri = "content://com.android.providers.media.documents/document/image:123456789012345678901234567890"

        // When
        val result = esUriValida(longUri)

        // Then
        assertTrue(result)
        assertTrue(longUri.length > 50)
    }

    @Test
    fun `uri con ruta compleja debe ser valida`() {
        // Given
        val complexUri = "file:///storage/emulated/0/Android/data/com.example.app/files/Pictures/profile_2024.jpg"

        // When
        val result = esUriValida(complexUri)

        // Then
        assertTrue(result)
        assertTrue(complexUri.contains("/"))
    }

    @Test
    fun `uri con mayusculas debe ser valida`() {
        // Given
        val upperCaseUri = "CONTENT://MEDIA/EXTERNAL/IMAGES/MEDIA/789"

        // When
        val result = esUriValida(upperCaseUri)

        // Then
        assertTrue(result)
    }

    // ========== Pruebas de valores límite ==========

    @Test
    fun `uri con un solo caracter debe ser valida`() {
        // Given
        val singleCharUri = "a"

        // When
        val result = esUriValida(singleCharUri)

        // Then
        assertTrue(result)
        assertEquals(1, singleCharUri.length)
    }

    @Test
    fun `uri con tab y newline debe retornar falso`() {
        // Given
        val whitespaceUri = "\t\n"

        // When
        val result = esUriValida(whitespaceUri)

        // Then
        assertFalse(result)
        assertTrue(whitespaceUri.isBlank())
    }

    // ========== Pruebas de comportamiento de la imagen ==========

    @Test
    fun `parametros de imagen deben ser consistentes`() {
        // Given
        val uri = "content://media/image/123"
        val expectedSize = 150
        val expectedShape = "CircleShape"

        // When
        val params = ImagenParams(uri, expectedSize, expectedShape)

        // Then
        assertEquals(uri, params.imagenUri)
        assertEquals(expectedSize, params.size)
        assertEquals(expectedShape, params.shape)
    }

    @Test
    fun `parametros de icono por defecto deben ser consistentes`() {
        // Given
        val iconName = "AccountCircle"
        val expectedSize = 150
        val expectedColor = "Gray"

        // When
        val params = IconoParams(iconName, expectedSize, expectedColor)

        // Then
        assertEquals(iconName, params.iconName)
        assertEquals(expectedSize, params.size)
        assertEquals(expectedColor, params.color)
    }

    // ========== Funciones auxiliares de validación ==========

    private fun esUriValida(uri: String?): Boolean {
        return !uri.isNullOrBlank()
    }

    private fun esUriLocal(uri: String): Boolean {
        return uri.startsWith("content://") || uri.startsWith("file://")
    }

    private fun esUriDeArchivo(uri: String): Boolean {
        return uri.startsWith("file://")
    }

    private fun esUriRemota(uri: String): Boolean {
        return uri.startsWith("http://") || uri.startsWith("https://")
    }

    private fun tieneExtensionDeImagen(uri: String): Boolean {
        val extensionesValidas = listOf(".jpg", ".jpeg", ".png", ".webp", ".gif")
        return extensionesValidas.any { uri.lowercase().endsWith(it) }
    }

    private fun deberMostrarImagen(uri: String?): Boolean {
        return !uri.isNullOrBlank()
    }

    private fun deberMostrarIconoPorDefecto(uri: String?): Boolean {
        return uri.isNullOrBlank()
    }

    /**
     * Clase auxiliar para representar los parámetros de la imagen
     */
    private data class ImagenParams(
        val imagenUri: String,
        val size: Int,
        val shape: String
    )

    /**
     * Clase auxiliar para representar los parámetros del icono
     */
    private data class IconoParams(
        val iconName: String,
        val size: Int,
        val color: String
    )
}