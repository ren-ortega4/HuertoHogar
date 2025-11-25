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
    suspend fun upsertAll(users: List<UserEntity>) // Muy útil para sincronizar desde la API


    @Delete
    suspend fun delete(user: UserEntity)


    @Query("DELETE FROM usuario")
    suspend fun clearAllUsers()
}