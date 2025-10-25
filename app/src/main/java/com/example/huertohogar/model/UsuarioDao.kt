package com.example.huertohogar.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM USUARIOS ORDER BY id asc")
    suspend fun obtenerUsuarios(): List<Usuario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar (usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)
}