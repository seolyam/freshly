// Product.kt
package com.example.freshly.models

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val allergens: String?,
    @SerializedName("image_url") val imageUrl: String
)
