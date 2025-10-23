package com.example.huertohogar.repository

import com.example.huertohogar.model.Product
import com.example.huertohogar.R

object ProductRepository {
    val products = listOf(
        Product(id = 1, name = "Leche Natural", price = "$3.800", imagesRes = R.drawable.destacado2),
        Product(id = 2, name = "Miel Orgánica", price = "$5.000", imagesRes = R.drawable.destacado1),
        Product(id = 3, name = "Platános Cavendish", price = "$800/Kg", imagesRes = R.drawable.destacado3),
        Product(id = 4, name = "Manzanas Fuji", price ="1.200/Kg", imagesRes = R.drawable.producto01f),
        Product(id = 5, name = "Zanahorias organicas", price = "900/Kg", imagesRes = R.drawable.producto04v),
        Product(id = 6, name = "Espinacas Frescas", price = "700/bolsa", imagesRes = R.drawable.producto03v)
    )
}