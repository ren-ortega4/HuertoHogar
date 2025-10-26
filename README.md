# HuertoHogar - App de E-commerce para Android


## Descripción del Proyecto

**HuertoHogar** es una aplicación móvil nativa para Android, desarrollada con tecnologías modernas como **Jetpack Compose** y el patrón de arquitectura **MVVM**.  
El proyecto simula una **tienda en línea (e-commerce)** de productos frescos y orgánicos, ofreciendo a los usuarios una experiencia de compra **fluida, atractiva e intuitiva**.

La aplicación permite:
- Registro e inicio de sesión de usuarios.
- Exploración de un catálogo de productos clasificados por categorías.
- Búsqueda avanzada de productos.
- Gestión de perfil personal.

El diseño se centra en ofrecer una **experiencia de usuario (UX)** moderna, coherente y visualmente limpia.

---

## Autores

Este proyecto fue desarrollado por:

- [Angel Prado]
- [Danilo Quiroz]
- [Renato Ortega]

---

## Funcionalidades Implementadas

### Autenticación de Usuarios
- **Registro de Nuevos Usuarios:**  
  Formulario con validación en tiempo real (nombre, correo, contraseña, dirección, etc.).
- **Inicio de Sesión:**  
  Acceso mediante credenciales de usuario registradas.
- **Gestión de Perfil:**  
  Visualización de información personal y opción de cerrar sesión.

### Navegación Intuitiva
- **Barra de Navegación Inferior (Footer):**  
  Permite cambiar entre las secciones principales (Inicio, Tienda, Carrito, Perfil).
- **Navegación Condicional:**  
  La barra se oculta automáticamente en pantallas secundarias (Login, Registro).

### Pantalla de Inicio (HomeScreen)
- **Barra de Búsqueda Funcional:**  
  Permite buscar productos por nombre en todo el catálogo.
- **Secciones Dinámicas:**  
  Muestra productos destacados y categorías.
- **Llamadas a la Acción (CTA):**  
  Tarjetas visuales que invitan al usuario a explorar el catálogo completo.

### Catálogo de Productos (ProductsScreen)
- **Vista por Categorías:**  
  Categorías presentadas en cuadrícula con íconos representativos.
- **Cuadrícula de Productos:**  
  Visualización de productos en formato de 2 columnas.
- **Resultados de Búsqueda:**  
  Reutiliza la misma vista para mostrar resultados filtrados.
- **Diseño Adaptativo:**  
  Usa `LazyVerticalGrid` para un scroll fluido y eficiente.

### Diseño y Experiencia de Usuario (UI/UX)
- **Interfaz Moderna con Jetpack Compose:**  
  Toda la UI está construida de forma declarativa.
- **Tema Oscuro y Claro:**  
  Adaptación automática al tema del sistema.
- **Diseño Inmersivo:**  
  Pantallas de login y registro con fondos personalizados y tarjetas semitransparentes.

---

## Tecnologías Utilizadas

- **Kotlin**
- **Jetpack Compose**
- **MVVM (Model-View-ViewModel)**
- **StateFlow y ViewModel**
- **Jetpack Navigation for Compose**
- **Coroutines de Kotlin**

---

## Pasos para Ejecutar el Proyecto

1. **Clonar el Repositorio**
   ```bash
   git clone --branch release/test --single-branch https://github.com/ren-ortega4/HuertoHogar.git
   
2. **Abrir Android Studio**
   **Selecciona File > Open y navega hasta la carpeta donde clonaste el proyecto.**
   **Android Studio detectará la configuración de Gradle y sincronizará el proyecto automáticamente.**

3. **Añadir Recursos Faltantes**
   **Este proyecto utiliza imágenes personalizadas para los fondos y el logotipo. Asegúrate de que los siguientes archivos estén en la carpeta app/src/main/res/drawable/**

4. **Ejecutar la Aplicación**
   **Seleccionar un emulador o conectar dispositivo físico, luego oprimir botón Run 'App'**