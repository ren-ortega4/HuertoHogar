package com.example.huertohogar.data.repository

import com.example.huertohogar.data.local.UsuarioDao
import com.example.huertohogar.model.User

class UsuarioRepository(private val dao : UsuarioDao) {
    suspend fun obtenerUsuarios(): List<User> = dao.obtenerUsuarios()
    suspend fun insertar(user: User) = dao.insertar(user)
    suspend fun eliminar(user: User) = dao.eliminar(user)
    suspend fun login(correo:String,clave:String): User?{
        return dao.login(correo,clave)
    }
    suspend fun actualizarFoto(id: Int, uri: String?){
        dao.actualizarFoto(id,uri)
    }
}