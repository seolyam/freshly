package com.example.freshly.ui.theme

data class CartItem(
    val name: String,
    val quantity: Int,
    val price: Double,
    val imageUrl: String? = null
)
