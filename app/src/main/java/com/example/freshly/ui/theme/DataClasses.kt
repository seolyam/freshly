package com.example.freshly.ui.theme

import com.google.gson.annotations.SerializedName

data class AddCartItemRequest(
    @SerializedName("productId") val productId: Int, // Backend expects "productId" (camelCase)
    val quantity: Int
)

data class CartItemsResponse(
    @SerializedName("cartItems") val cartItems: List<CartItemDto> // Backend uses "cartItems" (camelCase)
)

data class UpdateCartQuantityRequest(
    val productId: Int,
    val quantity: Int
)

data class CheckoutResponse(
    val success: Boolean,
    val message: String,
    val orderId: Int?
)


data class CartItemDto(
    @SerializedName("productId") val productId: Int, // Backend uses "productId" (camelCase)
    val name: String,
    val price: Double,
    val quantity: Int,
    @SerializedName("imageUrl") val imageUrl: String? // Backend uses "imageUrl" (camelCase)
)

data class CartItem(
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String? = null
)
