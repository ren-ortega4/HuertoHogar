package com.example.huertohogar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tip::class, CategoryEntity::class], version = 3, exportSchema = false)
abstract class TipDatabase : RoomDatabase() {

    abstract fun tipDao(): TipDao
    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: TipDatabase? = null

        fun getDatabase(context: Context): TipDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TipDatabase::class.java,
                    "tip_database"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

}