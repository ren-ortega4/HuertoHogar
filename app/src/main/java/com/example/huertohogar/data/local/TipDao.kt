package com.example.huertohogar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huertohogar.model.Tip
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(tips: List<Tip>)

    @Query("SELECT * FROM tips")
    fun getAllTips(): Flow<List<Tip>>

    @Query("DELETE FROM tips")
    suspend fun deleteAllTips()
}