package com.example.huertohogar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huertohogar.model.Tienda
import kotlinx.coroutines.flow.Flow

@Dao
interface TiendaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tienda: List<Tienda>)

    @Query("SELECT * FROM tiendas")
    suspend fun getAllTiendas(): List<Tienda>

    @Query("DELETE FROM tiendas")
    suspend fun deleteAllTiendas()

}