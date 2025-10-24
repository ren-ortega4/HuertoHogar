package com.example.huertohogar.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.huertohogar.data.AppPreference

class UserViewModelFactory(private val appPreferences: AppPreference) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(appPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
