package com.example.huertohogar.model

import androidx.compose.runtime.ExperimentalComposeRuntimeApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usuario", indices = [androidx.room.Index(value = ["correo"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) // Room genera automáticamente IDs únicos
    val id: Long = 0, // id local
    val idApi: Int?= null, // id proporcionado de la api externa
    val nombre: String,
    val apellido: String,
    val correo: String,
    val region: String,
    val fecha_registro: String,
    val estado: Boolean,
    val fotopefil: String? = null
)


data class User(

    @SerializedName("id_usuario")
    val id: Long?,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("contrasena")
    val contrasena: String,

    @SerializedName("fecha_registro")
    val fecha_registro: String,

    @SerializedName("estado")
    val estado: Boolean,

    // El rol se ignora en la BD local, pero la API lo usa.
    @SerializedName("rol")
    val rol: RolRequest?,

    @SerializedName("fotopefil")
    val fotopefil: String? = null
)

// --- 3. FUNCIÓN DE MAPEO ---
// Convierte el modelo de red (User) a la entidad de base de datos (UserEntity).
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id ?: 0L,
        idApi = this.id?.toInt(),
        nombre = this.nombre,
        apellido = this.apellido,
        correo = this.correo,
        region = this.region,
        fecha_registro = this.fecha_registro,
        estado = this.estado,
        fotopefil = this.fotopefil
    )
}

// Objeto para el rol, no necesita cambios.
data class RolRequest(
    @SerializedName("id_rol")
    val id_rol: Int
)
