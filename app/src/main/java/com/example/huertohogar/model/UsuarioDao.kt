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

    @Query("SELECT * FROM usuarios WHERE correo =:correo AND clave =:clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Query("UPDATE usuarios SET fotopefil = :nuevaUri WHERE id = :idUsuario")
    suspend fun actualizarFoto(idUsuario: Int, nuevaUri: String?)
}