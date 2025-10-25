package com.example.huertohogar.repository

import com.example.huertohogar.model.Usuario
import com.example.huertohogar.model.UsuarioDao

class UsuarioRepository(private val dao : UsuarioDao) {
    suspend fun obtenerUsuarios(): List<Usuario> = dao.obtenerUsuarios()
    suspend fun insertar(usuario: Usuario) = dao.insertar(usuario)
    suspend fun eliminar(usuario: Usuario) = dao.eliminar(usuario)
}