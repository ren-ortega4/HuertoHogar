package com.example.huertohogar.repository

import android.net.Uri
import com.example.huertohogar.model.UserProfile

class ProfileRepository {
    private var ProfileActual = UserProfile(
        id = 1,
        nombre = "Usuario",
        imagenUri = null
    )

    fun getProfile(): UserProfile = ProfileActual

    fun updateImage(uri: Uri?){
        ProfileActual = ProfileActual.copy(imagenUri = uri)
    }
}