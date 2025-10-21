package com.example.huertohogar.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository = ProfileRepository()): ViewModel(){
    private val _imagenUri = MutableStateFlow<Uri?>(repository.getProfile().imagenUri)

    val imagenUri: StateFlow<Uri?> = _imagenUri

    fun setImage(uri: Uri){
        viewModelScope.launch {
            _imagenUri.value = uri
            repository.updateImage(uri)
        }
    }
}