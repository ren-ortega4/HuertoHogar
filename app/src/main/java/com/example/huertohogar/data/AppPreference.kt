package com.example.huertohogar.data

import android.content.Context



class AppPreference(context: Context)
 {private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

     fun saveRememberUser(remember: Boolean) {
         sharedPreferences.edit().putBoolean("remember_user", remember).apply()
     }

     fun getRememberUser(): Boolean {
         return sharedPreferences.getBoolean("remember_user", false)
     }



 }