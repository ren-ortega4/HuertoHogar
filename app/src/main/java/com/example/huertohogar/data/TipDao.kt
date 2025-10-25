package com.example.huertohogar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tips: List<Tip>)

    @Query("SELECT * FROM tips")
    fun getAllTips(): Flow<List<Tip>>
}