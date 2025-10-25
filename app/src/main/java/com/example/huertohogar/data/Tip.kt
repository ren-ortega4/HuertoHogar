package com.example.huertohogar.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tips")
data class Tip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val iconName : String,
    val title: String,
    val text: String
)