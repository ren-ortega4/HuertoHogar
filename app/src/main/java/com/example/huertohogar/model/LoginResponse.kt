package com.example.huertohogar.model

import  com.google.gson.annotations.SerializedName
data class LoginResponse(
    @SerializedName("token")
    val token : String
)