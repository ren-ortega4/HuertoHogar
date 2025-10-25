package com.example.huertohogar.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities =[Usuario::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null



        fun getDatabase(context: Context): AppDataBase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "mi_db"
                ).build()
                INSTANCE=instance
                instance
            }
        }

    }
}