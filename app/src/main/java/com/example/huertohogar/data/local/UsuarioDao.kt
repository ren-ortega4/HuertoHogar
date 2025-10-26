package com.example.huertohogar.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huertohogar.model.User

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM USUARIOS ORDER BY id asc")
    suspend fun obtenerUsuarios(): List<User>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertar (user: User)

    @Delete
    suspend fun eliminar(user: User)

    @Query("SELECT * FROM usuarios WHERE correo =:correo AND clave =:clave LIMIT 1")
    suspend fun login(correo: String, clave: String): User?

    @Query("UPDATE usuarios SET fotopefil = :nuevaUri WHERE id = :idUsuario")
    suspend fun actualizarFoto(idUsuario: Int, nuevaUri: String?)
}