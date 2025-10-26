# Implementación de Room Database para Productos

## 📋 Descripción

Se ha implementado SQLite con la librería Room de Jetpack para gestionar los productos de la aplicación HuertoHogar. Esta implementación proporciona persistencia de datos local, permitiendo operaciones CRUD completas sobre los productos.

## 🏗️ Estructura de la Implementación

### 1. **Entity - Product**
📁 `model/Product.kt`

```kotlin
@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: String,
    val imagesRes: Int,
    val category: ProductCategory
)
```

- **@Entity**: Marca la clase como una tabla de base de datos
- **tableName**: Define el nombre de la tabla como "products"
- **@PrimaryKey**: Define el ID como clave primaria con auto-incremento

### 2. **DAO - ProductDao**
📁 `data/ProductDao.kt`

Interfaz que define las operaciones de base de datos:

**Operaciones de Lectura:**
- `getAllProducts()`: Retorna todos los productos como Flow
- `getProductsByCategory(category)`: Filtra productos por categoría
- `getProductById(id)`: Obtiene un producto específico
- `getAllCategories()`: Retorna categorías únicas

**Operaciones de Escritura:**
- `insertProduct(product)`: Inserta un producto
- `insertProducts(products)`: Inserta múltiples productos
- `updateProduct(product)`: Actualiza un producto
- `deleteProduct(product)`: Elimina un producto
- `deleteAllProducts()`: Elimina todos los productos

### 3. **TypeConverter - Converters**
📁 `data/Converters.kt`

Convierte el enum `ProductCategory` a String y viceversa para almacenamiento en la base de datos:

```kotlin
@TypeConverter
fun fromProductCategory(category: ProductCategory): String
fun toProductCategory(categoryString: String): ProductCategory
```

### 4. **Database - AppDatabase**
📁 `data/ProductDatabase.kt`

Clase abstracta que extiende RoomDatabase:

```kotlin
@Database(entities = [Product::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()
```

**Características:**
- ✅ Singleton pattern para una única instancia
- ✅ Callback para poblar la base de datos con datos iniciales
- ✅ Productos pre-cargados al crear la base de datos

**Productos Iniciales:**
1. Leche Natural ($3.800) - Lácteos
2. Miel Orgánica ($5.000) - Productos Orgánicos
3. Plátanos Cavendish ($800/Kg) - Frutas
4. Manzanas Fuji ($1.200/Kg) - Frutas
5. Zanahorias orgánicas ($900/Kg) - Verduras
6. Espinacas Frescas ($700/bolsa) - Verduras

### 5. **Repository - ProductRepository**
📁 `repository/ProductRepository.kt`

Capa de abstracción entre el DAO y los ViewModels:

```kotlin
class ProductRepository(private val productDao: ProductDao) {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: ProductCategory): Flow<List<Product>>
    fun getAllCategories(): Flow<List<ProductCategory>>
    suspend fun getProductById(productId: Int): Product?
    // ... operaciones CRUD
}
```

### 6. **ViewModel - ProductViewModel**
📁 `viewmodel/ProductViewModel.kt`

ViewModel que gestiona el estado de la UI y coordina las operaciones:

```kotlin
class ProductViewModel(application: Application) : AndroidViewModel(application) {
    val allProducts: StateFlow<List<Product>>
    val allCategories: StateFlow<List<ProductCategory>>
    val productsByCategory: StateFlow<List<Product>>
    // ... funciones para CRUD
}
```

**Características:**
- ✅ Usa `StateFlow` para reactividad
- ✅ Sobrevive a cambios de configuración
- ✅ Maneja coroutines con `viewModelScope`

## 🔄 Flujo de Datos

```
UI (Composables)
    ↕️
ProductViewModel
    ↕️
ProductRepository
    ↕️
ProductDao
    ↕️
Room Database (SQLite)
```

## 📱 Uso en la UI

### En ProductsByCategoryScreen:

```kotlin
@Composable
fun ProductsByCategoryScreen(
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel = viewModel()
) {
    val allProducts by productViewModel.allProducts.collectAsState()
    // La UI se actualiza automáticamente cuando cambian los datos
}
```

### En MainContent:

```kotlin
@Composable
fun MainContent(
    mainViewModel: MainViewModel = viewModel()
) {
    val uiState by mainViewModel.uiState.collectAsState()
    // Los productos se cargan desde la base de datos
}
```

## 🎯 Ventajas de la Implementación

1. **✅ Persistencia de Datos**: Los productos persisten entre sesiones
2. **✅ Reactividad**: Uso de Flow para actualizaciones automáticas en la UI
3. **✅ Type Safety**: Tipo seguro en tiempo de compilación
4. **✅ Async Operations**: Operaciones asíncronas con Coroutines
5. **✅ CRUD Completo**: Soporte completo para crear, leer, actualizar y eliminar
6. **✅ Testeable**: Fácil de mockear y testear
7. **✅ Escalable**: Fácil agregar nuevas entidades y relaciones

## 📦 Dependencias Utilizadas

```gradle
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
```

## 🔧 Archivos Modificados/Creados

### Creados:
- ✅ `data/ProductDao.kt`
- ✅ `data/Converters.kt`
- ✅ `data/ProductDatabase.kt`
- ✅ `viewmodel/ProductViewModel.kt`

### Modificados:
- ✅ `model/Product.kt` - Agregadas anotaciones Room
- ✅ `repository/ProductRepository.kt` - Cambiado de object a class con DAO
- ✅ `viewmodel/MainViewModel.kt` - Actualizado para usar Room
- ✅ `view/screen/ProductByCategoryScreen.kt` - Integrado ProductViewModel

## 💡 Próximos Pasos Sugeridos

1. **Implementar búsqueda de productos** por nombre
2. **Agregar filtros avanzados** (rango de precio, ordenamiento)
3. **Implementar sincronización** con un backend remoto
4. **Agregar caché de imágenes** con Coil
5. **Implementar paginación** para grandes conjuntos de datos
6. **Agregar migraciones** de base de datos para futuras versiones

## 🧪 Testing

Para testear la implementación:

```kotlin
// Crear una base de datos en memoria para tests
@get:Rule
val database = Room.inMemoryDatabaseBuilder(
    context,
    AppDatabase::class.java
).build()
```

---

**Implementado con ❤️ usando Jetpack Compose y Room**
