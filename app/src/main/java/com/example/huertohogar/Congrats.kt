package com.example.huertohogar

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.example.huertohogar.viewmodel.CartViewModel

class Congrats : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CartViewModel().clearCart()

        Toast.makeText(this, "¡Gracias por tu compra!", Toast.LENGTH_LONG).show()

    }
}