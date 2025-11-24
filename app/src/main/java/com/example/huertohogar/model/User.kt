package com.example.huertohogar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val region: String,
    val contrasena: String,
    val fecha_registro: String, // Se enviará la fecha como un texto (String)
    val estado: Boolean,
    val rol: RolRequest,
    val fotopefil: String? = null,
)
data class RolRequest(
    val id_rol: Int
)