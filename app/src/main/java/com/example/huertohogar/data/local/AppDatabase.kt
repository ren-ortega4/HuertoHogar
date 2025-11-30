package com.example.huertohogar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huertohogar.R
import com.example.huertohogar.model.CategoryEntity
import com.example.huertohogar.model.Tip
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import com.example.huertohogar.model.Tienda
import com.example.huertohogar.model.User
import com.example.huertohogar.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(entities = [Tip::class, CategoryEntity::class, Product::class, UserEntity::class, Tienda::class], version = 11, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tipDao(): TipDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun tiendaDao(): TiendaDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_hogar_database"
                )
                    .addCallback(DatabaseCallBack(context)) // Pasamos el contexto al callback
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val currentProducts = instance.productDao().getAllProducts().first()
                        if (currentProducts.isEmpty()) {
                            populateProducts(instance.productDao())
                        }
                        val tiendas = instance.tiendaDao().getAllTiendas()
                        if (tiendas.isEmpty()){
                            populateInitialTiendas(instance.tiendaDao())
                        }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
                instance
            }
        }


        private class DatabaseCallBack(private val context: Context) : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase){
                super.onCreate(db)
                // Usamos el INSTANCE que se asignará después de la creación
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        // Toda la lógica de poblado se centraliza aquí
                        populateProducts(database.productDao())
                        populateInitialTiendas(database.tiendaDao())
                    }
                }
            }
        }

        suspend fun populateProducts(productDao: ProductDao){
            productDao.deleteAllProducts()
            val products = listOf(
                Product(
                    id = 1,
                    name = "Leche Natural",
                    price = 3800.0,
                    priceLabel = "$3.800",
                    imagesRes = R.drawable.destacado2,
                    category = ProductCategory.lacteos
                ),
                Product(
                    id = 2,
                    name = "Miel Orgánica",
                    price = 5000.0,
                    priceLabel = "$5.000",
                    imagesRes = R.drawable.destacado1,
                    category = ProductCategory.productosOrganicos
                ),
                Product(
                    id = 3,
                    name = "Platános Cavendish",
                    price = 800.0,
                    priceLabel = "$800/Kg",
                    imagesRes = R.drawable.destacado3,
                    category = ProductCategory.frutas
                ),
                Product(
                    id = 4,
                    name = "Manzanas Fuji",
                    price = 1200.0,
                    priceLabel = "$1.200/Kg",
                    imagesRes = R.drawable.producto01f,
                    category = ProductCategory.frutas
                ),
                Product(
                    id = 5,
                    name = "Zanahorias organicas",
                    price = 900.0,
                    priceLabel = "$900/Kg",
                    imagesRes = R.drawable.producto04v,
                    category = ProductCategory.verduras
                ),
                Product(
                    id = 6,
                    name = "Espinacas Frescas",
                    price = 700.0,
                    priceLabel = "$700/Bolsa",
                    imagesRes = R.drawable.producto03v,
                    category = ProductCategory.verduras
                )
            )
            productDao.insertProducts(products)
        }



        suspend fun populateInitialTiendas(tiendaDao: TiendaDao){
            tiendaDao.deleteAllTiendas()
            val tiendas = listOf(
                Tienda(
                    name = "HuertoHogar Santiago",
                    address = "Salomon Sumal 3420, San Joaquin",
                    phone = "+569 87654321",
                    latitude = -33.664826756779775,
                    longitude = -70.83641809450158,
                    description = "Región Metropolitana, comunidad instalada con atención al cliente desde las 09:00 a 17:00"
                ),
                Tienda(
                    name = "HuertoHogar Viña",
                    address = "Calle Valparaíso 463, Local 128, Galeria Cristal",
                    phone = "+569 87654321",
                    latitude = -33.024143063501725,
                    longitude = -71.55606707327293,
                    description = "Región de Valparaíso, ofrecimiento de productos frescos y atención al cliente desde las 08:00 a 19:00"
                ),
                Tienda(
                    name = "HuertoHogar Valparaíso",
                    address = "Pedro Montt 2506, Valparaíso",
                    phone = "+569 87654321",
                    latitude = -33.04761028467837,
                    longitude = -71.60964935021265,
                    description = "Región de Valparaíso, bódega con variedad de alimentos con atención al cliente desde las 10:00 a 20:00"
                ),
                Tienda(
                    name = "HuertoHogar Villarica",
                    address = "Camilo Henríquez 544",
                    phone = "+569 87654321",
                    latitude = -39.28237326069741,
                    longitude = -72.22751541427724,
                    description = "Región de la Araucanía, experimentación de cultivo con atención al cliente desde las 09:00 a 20:30"
                ),
                Tienda(
                    name = "HuertoHogar Nacimiento",
                    address = "San Martín 443",
                    phone = "+569 87654321",
                    latitude = -37.50125314604244,
                    longitude = -72.67114217467407,
                    description = "Región de Biobío, actividades recreativas a todo público desde las 09:00 a 21:00"
                ),
                Tienda(
                    name = "HuertoHogar Puerto Montt",
                    address = "Calle Antihual 965",
                    phone = "+569 87654321",
                    latitude = -41.45523805665322,
                    longitude = -72.9403582931657,
                    description = "Región de Los Lagos, departamento social con atención al cliente desde las 08:00 a 20:30"
                ),
                Tienda(
                    name = "HuertoHogar Concepción",
                    address = "Freire 1049",
                    phone = "+569 87654321",
                    latitude = -36.82308891365832,
                    longitude = -73.04613343457495,
                    description = "Región de Biobío, departamento de logística con atención al cliente desde las 10:00 a 18:00"
                )
            )
            tiendaDao.insertAll(tiendas)
        }
    }
}
