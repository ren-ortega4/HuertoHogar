# HuertoHogar - App de E-commerce para Android 🌱

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-green.svg)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Room-2.6.1-blue.svg)](https://developer.android.com/jetpack/androidx/releases/room)

## 📋 Descripción del Proyecto

**HuertoHogar** es una aplicación móvil nativa para Android que simula una **tienda en línea (e-commerce)** de productos frescos y orgánicos. Desarrollada con **Jetpack Compose** y arquitectura **MVVM**, la aplicación ofrece una experiencia de compra completa, moderna e intuitiva.

### Características Principales
- 🛒 Sistema completo de carrito de compras
- 💳 Integración con MercadoPago para pagos
- 🗄️ Persistencia de datos con Room Database
- 🔐 Sistema de autenticación con API REST
- 🗺️ Visualización de tiendas en mapa interactivo
- 🔔 Sistema de notificaciones
- 🎨 Tema claro y oscuro
- 📱 Diseño responsive y moderno

---

## 👥 Autores

Este proyecto fue desarrollado por:

- **Angel Prado**
- **Danilo Quiroz**
- **Renato Ortega**

---

## ✨ Funcionalidades Implementadas

### 🔐 Autenticación y Gestión de Usuarios
- **Registro de Usuarios:**
  - Formulario completo con validación en tiempo real
  - Campos: nombre, correo, contraseña, teléfono, dirección, etc.
  - Integración con API REST para registro remoto
  - Almacenamiento local con Room Database
  
- **Inicio de Sesión:**
  - Autenticación mediante API REST
  - Validación de credenciales
  - Manejo de tokens JWT
  - Persistencia de sesión con DataStore
  
- **Gestión de Perfil:**
  - Visualización de información del usuario
  - Edición de datos personales
  - Opción de cerrar sesión
  - Sincronización con backend

### 🏪 Catálogo de Productos
- **Base de Datos Local (Room):**
  - Persistencia de productos con SQLite
  - 5 categorías: Frutas, Verduras, Lácteos, Productos Orgánicos, Otros
  - Operaciones CRUD completas
  - Sincronización automática
  
- **Vista por Categorías:**
  - Filtrado dinámico por categoría
  - Tarjetas visuales con iconos representativos
  - Selección interactiva de categorías
  
- **Búsqueda de Productos:**
  - Búsqueda en tiempo real
  - Filtrado por nombre
  - Resultados instantáneos
  - Integración con barra superior

- **Detalles de Producto:**
  - Imágenes de alta calidad
  - Información detallada (nombre, precio, categoría)
  - Selector de cantidad
  - Botón de agregar al carrito
  - Función de compartir producto

### 🛒 Carrito de Compras
- **Gestión Completa:**
  - Agregar/eliminar productos
  - Modificar cantidades
  - Cálculo automático de subtotales y total
  - Persistencia en memoria durante la sesión
  
- **Interfaz Intuitiva:**
  - Vista en lista con imágenes
  - Controles de cantidad (+/-)
  - Botón de eliminar por producto
  - Banner de confirmación de compra exitosa
  
- **Integración con MercadoPago:**
  - Generación de preferencias de pago
  - Checkout mediante Custom Tabs
  - Manejo de deep links para respuesta de pago
  - Confirmación visual de transacción

### 🏠 Pantalla Principal (Home)
- **Splash Screen Animado:**
  - Animación Lottie de bienvenida
  - Transición suave a la app
  
- **Consejos del Día (Tips):**
  - Rotación automática cada 5 segundos
  - Consejos sobre agricultura urbana
  - Almacenamiento en Room Database
  
- **Productos Destacados:**
  - Carrusel horizontal de productos
  - Navegación fluida
  - Click para ver detalles
  
- **Categorías Visuales:**
  - Tarjetas interactivas
  - Diálogos con información detallada
  - Imágenes representativas

### 🗺️ Mapa de Tiendas
- **Visualización Geográfica:**
  - Integración con OSMDroid (OpenStreetMap)
  - Marcadores de tiendas
  - Información al hacer click
  - Zoom y navegación del mapa
  
- **Gestión de Tiendas:**
  - Almacenamiento en Room Database
  - Datos pre-cargados de tiendas
  - Coordenadas geográficas precisas

### 🔔 Sistema de Notificaciones
- **Centro de Notificaciones:**
  - Vista de notificaciones no leídas
  - Indicador visual en barra superior
  - Marca de leído/no leído
  - Almacenamiento persistente
  
- **Tipos de Notificaciones:**
  - Ofertas especiales
  - Nuevos productos
  - Actualizaciones de pedidos

### 🎨 Diseño y Experiencia de Usuario (UI/UX)
- **Jetpack Compose Moderno:**
  - Interfaz 100% declarativa
  - Componentes reutilizables
  - Animaciones fluidas
  
- **Tema Adaptativo:**
  - Modo claro y oscuro
  - Detección automática del sistema
  - Paleta de colores coherente
  
- **Navegación:**
  - Bottom Navigation Bar
  - Top App Bar con búsqueda
  - Navegación condicional (oculta en login/registro)
  - Transiciones suaves entre pantallas
  
- **Animaciones:**
  - Entrada progresiva de elementos (AnimatedEntry)
  - Transiciones de pantalla
  - Efectos visuales al agregar al carrito
  - Splash screen con Lottie

---

## 🏗️ Arquitectura y Tecnologías

### Patrón de Arquitectura
```
📱 UI Layer (Jetpack Compose)
    ↕️
🎯 ViewModel Layer (StateFlow)
    ↕️
📦 Repository Layer
    ↕️
🗄️ Data Layer (Room + Retrofit)
```

### Tecnologías Principales

#### Frontend
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Componentes y tema
- **Navigation Compose** - Navegación entre pantallas
- **Coil** - Carga de imágenes
- **Lottie** - Animaciones vectoriales

#### Backend & Persistencia
- **Room Database** - Base de datos local SQLite
  - Entidades: Product, User, Tienda, Tip, Category, Notificacion
  - DAOs con operaciones CRUD
  - TypeConverters para tipos complejos
  - Flow para reactividad
  
- **Retrofit** - Cliente HTTP para API REST
  - Integración con backend propio
  - Serialización con Gson
  - Interceptors para headers
  
- **DataStore** - Almacenamiento de preferencias
  - Manejo de sesión de usuario
  - Configuraciones de la app

#### Integración de Pagos
- **MercadoPago SDK**
  - Checkout integrado
  - Procesamiento de pagos
  - Custom Tabs para flow de pago
  - Deep Links para callbacks

#### Mapas
- **OSMDroid** - Mapas OpenStreetMap
  - Visualización de ubicaciones
  - Marcadores personalizados
  - Controles de zoom y navegación

#### Testing
- **JUnit 5** - Testing unitario
- **Kotest** - Assertions y testing
- **MockK** - Mocking de dependencias
- **Coroutines Test** - Testing de coroutines
- **Compose UI Test** - Testing de UI

#### Arquitectura y Patrones
- **MVVM** - Model-View-ViewModel
- **Repository Pattern** - Abstracción de fuentes de datos
- **Dependency Injection Manual** - Inyección de dependencias
- **StateFlow & Flow** - Programación reactiva
- **Coroutines** - Operaciones asíncronas
- **Single Source of Truth** - Room como fuente única

---

## 📦 Estructura del Proyecto

```
app/src/main/java/com/example/huertohogar/
├── data/
│   ├── local/              # Room Database
│   │   ├── AppDatabase.kt
│   │   ├── ProductDao.kt
│   │   ├── UsuarioDao.kt
│   │   ├── TiendaDao.kt
│   │   ├── CategoryDao.kt
│   │   └── TipDao.kt
│   └── repository/         # Repositorios
│       ├── ProductRepository.kt
│       ├── UsuarioRepository.kt
│       └── CategoryRepository.kt
├── model/                  # Modelos de datos
│   ├── Product.kt
│   ├── User.kt
│   ├── CartItem.kt
│   ├── Tienda.kt
│   ├── Notificacion.kt
│   └── ...
├── network/                # API y Retrofit
│   ├── ApiService.kt
│   ├── ApiCliente.kt
│   ├── RetrofitInstance.kt
│   └── MercadoPagoApi.kt
├── view/
│   ├── screen/             # Pantallas
│   │   ├── MainContent.kt
│   │   ├── ProductByCategoryScreen.kt
│   │   ├── CartScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── MapScreen.kt
│   │   └── NotificacionesScreen.kt
│   └── components/         # Componentes reutilizables
│       ├── ProductCard.kt
│       ├── CategoryCard.kt
│       ├── WelcomeCard.kt
│       └── ...
├── viewmodel/              # ViewModels
│   ├── ProductViewModel.kt
│   ├── CartViewModel.kt
│   ├── UserViewModel.kt
│   ├── MainViewModel.kt
│   ├── StoreViewModel.kt
│   └── NotificacionesViewModel.kt
├── ui/theme/               # Tema de la app
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── MainActivity.kt         # Actividad principal
```

---

## 🌐 Endpoints y APIs Utilizadas

### APIs Propias (Backend Microservicios)

**Base URL**: `http://tu-servidor.com/api` (Configurar según tu backend)

#### Autenticación
- **POST** `/usuarios/registro` - Registro de nuevos usuarios
  ```json
  {
    "nombre": "string",
    "correo": "string",
    "contrasena": "string",
    "telefono": "string",
    "direccion": "string"
  }
  ```
  
- **POST** `/usuarios/login` - Inicio de sesión
  ```json
  {
    "correo": "string",
    "contrasena": "string"
  }
  ```
  **Response**: `{ "token": "JWT_TOKEN", "usuario": {...} }`

- **GET** `/usuarios/{id}` - Obtener datos del usuario
  **Headers**: `Authorization: Bearer {token}`


### APIs Externas

#### MercadoPago Payment API
- **Base URL**: `https://api.mercadopago.com`
- **Endpoint**: `POST /checkout/preferences`
  - Crear preferencia de pago para checkout
  - Requiere Access Token de MercadoPago
  
**Documentación**: [MercadoPago Developers](https://www.mercadopago.com.ar/developers)

**Configuración requerida**:
```kotlin
// En tu archivo de configuración local
MERCADOPAGO_PUBLIC_KEY=TEST-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
MERCADOPAGO_ACCESS_TOKEN=TEST-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

---

## 🚀 Pasos para Ejecutar el Proyecto

### Requisitos Previos
- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11** o superior
- **Gradle 8.13**
- **Android SDK** (API 24-36)
- Dispositivo físico o emulador Android

### Instalación

1. **Clonar el Repositorio**
   ```bash
   git clone https://github.com/ren-ortega4/HuertoHogar.git
   cd HuertoHogar
   ```

2. **Abrir en Android Studio**
   - Selecciona `File > Open`
   - Navega hasta la carpeta del proyecto
   - Android Studio sincronizará Gradle automáticamente

3. **Configurar Variables de Entorno**
   - Asegúrate de tener configurado el SDK de Android
   - Verifica que las dependencias de Gradle se descarguen correctamente

4. **Compilar el Proyecto**
   ```bash
   ./gradlew clean build
   ```

5. **Ejecutar la Aplicación**
   - Selecciona un emulador o conecta un dispositivo físico
   - Presiona el botón **Run** (▶️) o usa:
   ```bash
   ./gradlew installDebug
   ```

### Recursos Necesarios
El proyecto incluye todos los recursos necesarios en `app/src/main/res/drawable/`:
- Imágenes de productos (PNG)
- Fondos para modo claro y oscuro
- Iconos de categorías
- Logotipo de la aplicación

---

## 📦 APK Firmada y Distribución

### Generación de APK Firmada

El proyecto está configurado para generar APKs firmadas para distribución:

**Ubicación del Keystore**:
```
📁 HuertoHogar/
  ├── huertohogar-release-key.jks  ⚠️ (NO incluido en Git por seguridad)
  └── keystore.properties          ⚠️ (NO incluido en Git)
```

### Instrucciones para Firmar la APK

1. **Generar el Keystore** (primera vez):
   ```bash
   keytool -genkeypair -v -keystore huertohogar-release-key.jks \
     -keyalg RSA -keysize 2048 -validity 10000 -alias huertohogar
   ```

2. **Configurar credenciales** en `keystore.properties`:
   ```properties
   storePassword=TU_PASSWORD_KEYSTORE
   keyPassword=TU_PASSWORD_KEY
   keyAlias=huertohogar
   storeFile=huertohogar-release-key.jks
   ```

3. **Compilar APK Release firmada**:
   ```bash
   ./gradlew assembleRelease
   ```

4. **Ubicación de la APK generada**:
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

5. **Generar Bundle para Google Play** (recomendado):
   ```bash
   ./gradlew bundleRelease
   ```
   Ubicación: `app/build/outputs/bundle/release/app-release.aab`

### ⚠️ Seguridad del Keystore
- El archivo `.jks` y `keystore.properties` están en `.gitignore`
- **NUNCA** subir el keystore al repositorio público
- Guardar backup del keystore en ubicación segura
- Sin el keystore original, no se pueden publicar actualizaciones

---

## 🧪 Testing

El proyecto incluye tests unitarios y de integración:

```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentados
./gradlew connectedAndroidTest
```

### Cobertura de Tests
- ✅ ViewModels (CartViewModel, ProductViewModel)
- ✅ Modelos de datos
- ✅ Componentes de UI
- ✅ Flujos de navegación

---

## 📱 Pantallas de la Aplicación

1. **Splash Screen** - Animación de bienvenida con Lottie
2. **Login** - Inicio de sesión de usuarios
3. **Registro** - Formulario completo de registro
4. **Home** - Pantalla principal con productos destacados y categorías
5. **Tienda** - Catálogo completo de productos por categoría
6. **Carrito** - Gestión del carrito de compras
7. **Perfil** - Información del usuario
8. **Mapa** - Ubicación de tiendas físicas
9. **Notificaciones** - Centro de notificaciones

---

## 🔄 Flujo de Datos

### Productos
```
Room DB → ProductRepository → ProductViewModel → UI (StateFlow)
```

### Autenticación
```
API REST → UsuarioRepository → UserViewModel → UI (StateFlow)
```

### Carrito
```
CartViewModel (in-memory) → UI (StateFlow) → MercadoPago API
```

---

## 📝 Dependencias Principales

```gradle
// Jetpack Compose
implementation("androidx.compose.material3:material3:1.2.0")
implementation("androidx.navigation:navigation-compose:2.6.0")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Retrofit
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// MercadoPago
implementation("com.mercadopago.android.px:checkout:4.53.2")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Coil (imágenes)
implementation("io.coil-kt:coil-compose:2.5.0")

// Lottie (animaciones)
implementation("com.airbnb.android:lottie-compose:6.1.0")

// OSMDroid (mapas)
implementation("org.osmdroid:osmdroid-android:6.1.18")
```

---

## 🎯 Características Técnicas Destacadas

### Reactividad
- Uso extensivo de `StateFlow` para UI reactiva
- Actualización automática de la interfaz ante cambios de datos
- Flow para operaciones asíncronas de Room

### Persistencia Multi-capa
- **Room Database**: Productos, usuarios, tiendas, tips
- **DataStore**: Sesión de usuario y preferencias
- **In-Memory**: Carrito de compras (durante la sesión)

### Arquitectura Limpia
- Separación clara de capas (UI, ViewModel, Repository, Data)
- Single Source of Truth con Room
- Inyección de dependencias manual con factories

### Manejo de Estados
- Estados de carga, éxito y error
- Validación de formularios en tiempo real
- Feedback visual al usuario

---

## 🔮 Futuras Mejoras

- [ ] Implementar filtros avanzados de productos (precio, disponibilidad)
- [ ] Agregar sistema de favoritos
- [ ] Implementar historial de compras
- [ ] Notificaciones push
- [ ] Soporte para múltiples idiomas
- [ ] Sincronización offline-first
- [ ] Integración con más métodos de pago
- [ ] Sistema de reseñas y calificaciones

---

## 🔗 Código Fuente

### Repositorio Principal
```
https://github.com/ren-ortega4/HuertoHogar
```

### Estructura de Repositorios

#### App Móvil (Android)
- **Repositorio**: `ren-ortega4/HuertoHogar` (este repositorio)
- **Tecnología**: Kotlin + Jetpack Compose
- **Ubicación del código fuente**: `/app/src/main/java/com/example/huertohogar/`

#### Microservicios Backend
Para el backend de la aplicación, se utilizan los siguientes microservicios:

1. **Servicio de Autenticación** (Usuarios)
   - Gestión de registro y login
   - Autenticación JWT
   - Gestión de perfiles

2. **Servicio de Productos**
   - CRUD de productos
   - Categorización
   - Búsqueda y filtros

3. **Servicio de Tiendas**
   - Gestión de ubicaciones
   - Información de tiendas físicas

**Nota**: Los microservicios pueden estar en repositorios separados o en el mismo repositorio en carpetas diferentes según la arquitectura elegida.

---

## 👨‍💻 Evidencia de Trabajo Colaborativo

### Estadísticas del Repositorio

Este proyecto fue desarrollado de forma colaborativa por el equipo. Puedes ver la evidencia del trabajo en equipo en:

**Historial de Commits por Autor**:
```bash
git log --format='%aN' | sort -u
git shortlog -s -n --all
```


### Ver Commits por Persona
```bash
# Ver commits de un autor específico
git log --author="Angel Prado" --oneline
git log --author="Danilo Quiroz" --oneline
git log --author="Renato Ortega" --oneline
```

### Branches y Pull Requests
El desarrollo se realizó utilizando:
- Branch principal: `master`
- Branches de feature para cada funcionalidad
- Pull Requests para revisión de código
- Code reviews entre los miembros del equipo

**Evidencia visual**: Ver el gráfico de contribuciones en GitHub:
```
https://github.com/ren-ortega4/HuertoHogar/graphs/contributors
```

---

## 📄 Licencia

Este proyecto es parte de un trabajo académico para **DuocUC** - Desarrollo de Aplicaciones Móviles.

---

## 📞 Contacto

Para consultas sobre el proyecto:

- **Repositorio**: [github.com/ren-ortega4/HuertoHogar](https://github.com/ren-ortega4/HuertoHogar)
- **Issues**: [github.com/ren-ortega4/HuertoHogar/issues](https://github.com/ren-ortega4/HuertoHogar/issues)

---

**Desarrollado con ❤️ usando Jetpack Compose, Room Database y arquitectura MVVM**

### 📊 Estadísticas del Proyecto
- **Lenguaje**: Kotlin 100%
- **Líneas de código**: ~8,000+
- **Pantallas**: 9 principales
- **Tests**: Unitarios + Instrumentados
- **Arquitectura**: MVVM + Clean Architecture
