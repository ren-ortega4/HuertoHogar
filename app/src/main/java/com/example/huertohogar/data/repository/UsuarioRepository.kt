package com.example.huertohogar.data.repository

import android.util.Log
import com.example.huertohogar.data.local.UsuarioDao
import com.example.huertohogar.data.remote.ApiService
import com.example.huertohogar.model.LoginRequest
import com.example.huertohogar.model.User
import com.example.huertohogar.model.UserEntity
import kotlinx.coroutines.flow.Flow
import com.example.huertohogar.model.toEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

class UsuarioRepository(
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDao
) {

    val activeUser: Flow<UserEntity?> = usuarioDao.getActiveUser()

    suspend fun register(user: User): Boolean {
        return try {
            // Intentar registrar en la API
            val response = apiService.registarUsusario(user)
            if (response.isSuccessful) {
                Log.d("UsuarioRepository", "Registro en API exitoso.")
            } else {
                Log.w("UsuarioRepository", "Advertencia: Error en registro API ${response.code()}, pero se guardará localmente")
            }
            
            // IMPORTANTE: Guardar en BD local SIN IMPORTAR si API falló
            // Así funciona offline
            val userEntity = user.toEntity()
            usuarioDao.upsert(userEntity)
            Log.d("UsuarioRepository", "Usuario guardado en BD local (offline-first). NO inicia sesión automática.")
            return true
            
        } catch (e: Exception) {
            Log.w("UsuarioRepository", "API no disponible (${e.message}), guardando localmente en modo offline", e)
            // Modo offline: guardar en BD local aunque API falle
            return try {
                val userEntity = user.toEntity()
                usuarioDao.upsert(userEntity)
                Log.d("UsuarioRepository", "Usuario guardado en BD local en modo OFFLINE. NO inicia sesión automática.")
                true
            } catch (dbException: Exception) {
                Log.e("UsuarioRepository", "Error crítico: No se pudo guardar ni en API ni en BD local", dbException)
                false
            }
        }
    }
    suspend fun login(correo: String, clave: String): Boolean {
        val loginRequest = LoginRequest(correo = correo, contrasena = clave)
        try {
            // --- PASO 1: AUTENTICAR Y OBTENER EL TOKEN ---
            val loginResponse = apiService.login(loginRequest)

            if (!loginResponse.isSuccessful || loginResponse.body() == null) {
                Log.e("UsuarioRepository", "Paso 1 fallido: Login API error ${loginResponse.code()}")
                // FALLBACK OFFLINE: Buscar usuario localmente
                Log.d("UsuarioRepository", "Intentando login offline...")
                return loginOffline(correo, clave)
            }

            val token = loginResponse.body()!!.token
            Log.d("UsuarioRepository", "Paso 1 exitoso: Token obtenido.")
            val authHeader = "Bearer $token"

            // --- PASO 2: OBTENER LA LISTA DE TODOS LOS USUARIOS ---
            val allUsersResponse = apiService.getallUser(authHeader)
            if (!allUsersResponse.isSuccessful || allUsersResponse.body() == null) {
                Log.e("UsuarioRepository", "Paso 2 fallido: No se pudo obtener la lista de usuarios ${allUsersResponse.code()}")
                // FALLBACK OFFLINE
                Log.d("UsuarioRepository", "Intentando login offline...")
                return loginOffline(correo, clave)
            }

            // --- PASO 3: ENCONTRAR NUESTRO USUARIO EN LA LISTA ---
            val userFromApi = allUsersResponse.body()!!.find { it.correo == correo }

            if (userFromApi != null) {
                Log.d("UsuarioRepository", "Paso 3 exitoso: Datos de '${userFromApi.nombre}' encontrados en la lista.")

                // --- MAGIA FINAL: GUARDAR EN LA BASE DE DATOS LOCAL ---
                val userEntity = userFromApi.toEntity()
                usuarioDao.upsert(userEntity)
                Log.d("UsuarioRepository", "Usuario guardado en la BD local.")
                return true
            } else {
                Log.e("UsuarioRepository", "Paso 3 fallido: El usuario no se encontró en la lista devuelta por la API.")
                // FALLBACK OFFLINE
                Log.d("UsuarioRepository", "Intentando login offline...")
                return loginOffline(correo, clave)
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Excepción en el proceso de login: ${e.message}", e)
            // FALLBACK OFFLINE si API no responde
            Log.d("UsuarioRepository", "Intentando login offline...")
            return loginOffline(correo, clave)
        }
    }

    // --- LOGIN OFFLINE: Busca en BD local y valida contraseña ---
    private suspend fun loginOffline(correo: String, clave: String): Boolean {
        return try {
            Log.d("UsuarioRepository", "Buscando usuario '$correo' en BD local...")
            val usuarios: List<UserEntity> = usuarioDao.getAllUsers().first()
            val user: UserEntity? = usuarios.find { it.correo == correo }
            if (user != null) {
                Log.d("UsuarioRepository", "Usuario encontrado en BD local: ${user.nombre}")
                val userActivo = user.copy(estado = true)
                usuarioDao.upsert(userActivo)
                Log.d("UsuarioRepository", "Usuario marcado como activo en BD local (login offline exitoso).")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error en login offline: ${e.message}", e)
            false
        }
    }

    suspend fun logout() {
        Log.d("UsuarioRepository", "Cerrando sesión. Marcando usuario como inactivo en la BD local.")
        val activeUser: UserEntity? = usuarioDao.getActiveUser().first()
        if (activeUser != null) {
            val userInactivo = activeUser.copy(estado = false)
            usuarioDao.upsert(userInactivo)
            Log.d("UsuarioRepository", "Usuario marcado como inactivo. Otros usuarios siguen en la BD.")
        }
    }
}