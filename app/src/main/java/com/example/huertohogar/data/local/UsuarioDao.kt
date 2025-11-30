package com.example.huertohogar.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huertohogar.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario ORDER BY nombre ASC")
    fun getAllUsers(): Flow<List<UserEntity>>


    @Query("SELECT * FROM usuario WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM usuario WHERE estado = 1 LIMIT 1")
    fun getActiveUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(users: List<UserEntity>)
    // útil para sincronizar desde la API

    @Query("DELETE FROM usuario WHERE id = :userId")
    suspend fun deleteById(userId: Long)
}