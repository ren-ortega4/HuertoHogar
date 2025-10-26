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
import com.example.huertohogar.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(entities = [Tip::class, CategoryEntity::class, Product::class, User::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tipDao(): TipDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun usuarioDao(): UsuarioDao

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
                    .addCallback(DatabaseCallBack())
                    .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val currentProducts = instance.productDao().getAllProducts().first()
                        if (currentProducts.isEmpty()){
                            populateProducts(instance.productDao())
                        }
                        val usuarios = instance.usuarioDao().obtenerUsuarios()
                        if (usuarios.isEmpty()){
                            populateInitialUser(instance.usuarioDao())
                        }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
                instance
            }
        }

        private class DatabaseCallBack : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase){
                super.onCreate(db)
                INSTANCE?.let {
                    database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateProducts(database.productDao())
                        populateInitialUser(database.usuarioDao())
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
                    price = "$3.800",
                    imagesRes = R.drawable.destacado2,
                    category = ProductCategory.lacteos
                ),
                Product(
                    id = 2,
                    name = "Miel Orgánica",
                    price = "$5.000",
                    imagesRes = R.drawable.destacado1,
                    category = ProductCategory.productosOrganicos
                ),
                Product(
                    id = 3,
                    name = "Platános Cavendish",
                    price = "$800/Kg",
                    imagesRes = R.drawable.destacado3,
                    category = ProductCategory.frutas
                ),
                Product(
                    id = 4,
                    name = "Manzanas Fuji",
                    price = "$1.200/Kg",
                    imagesRes = R.drawable.producto01f,
                    category = ProductCategory.frutas
                ),
                Product(
                    id = 5,
                    name = "Zanahorias organicas",
                    price = "$900/Kg",
                    imagesRes = R.drawable.producto04v,
                    category = ProductCategory.verduras
                ),
                Product(
                    id = 6,
                    name = "Espinacas Frescas",
                    price = "$700/bolsa",
                    imagesRes = R.drawable.producto03v,
                    category = ProductCategory.verduras
                )
            )
            productDao.insertProducts(products)
        }

        suspend fun populateInitialUser(usuarioDao: UsuarioDao){
            usuarioDao.insertar(
                User(
                    nombre = "Admin",
                    correo = "admin@gmail.com",
                    clave = "12345678",
                    confirmarClave = "12345678",
                    direccion = "admin",
                    region = "admin",
                    aceptaTerminos = true
                )
            )
        }
    }

}