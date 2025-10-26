package com.example.huertohogar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huertohogar.R
import com.example.huertohogar.model.Product
import com.example.huertohogar.model.ProductCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ProductDatabase : RoomDatabase() {
    
    abstract fun productDao(): ProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null
        
        fun getDatabase(context: Context): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.productDao())
                    }
                }
            }
        }
        
        suspend fun populateDatabase(productDao: ProductDao) {
            // Productos iniciales
            val products = listOf(
                Product(id = 1, name = "Leche Natural", price = "$3.800", imagesRes = R.drawable.destacado2, category = ProductCategory.lacteos),
                Product(id = 2, name = "Miel Orgánica", price = "$5.000", imagesRes = R.drawable.destacado1, category = ProductCategory.productosOrganicos),
                Product(id = 3, name = "Platános Cavendish", price = "$800/Kg", imagesRes = R.drawable.destacado3, category = ProductCategory.frutas),
                Product(id = 4, name = "Manzanas Fuji", price ="$1.200/Kg", imagesRes = R.drawable.producto01f, category = ProductCategory.frutas),
                Product(id = 5, name = "Zanahorias organicas", price = "$900/Kg", imagesRes = R.drawable.producto04v, category = ProductCategory.verduras),
                Product(id = 6, name = "Espinacas Frescas", price = "$700/bolsa", imagesRes = R.drawable.producto03v, category = ProductCategory.verduras)
            )
            productDao.insertProducts(products)
        }
    }
}
