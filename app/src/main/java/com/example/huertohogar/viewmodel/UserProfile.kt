package com.example.huertohogar.viewmodel

import android.net.Uri

data class UserProfile (
    val id: Int,
    val nombre: String,
    val imagenUri: Uri? = null
)