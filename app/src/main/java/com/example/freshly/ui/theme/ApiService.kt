package com.example.freshly.ui.theme

import com.example.freshly.models.ProductResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("/products")
    suspend fun getProducts(): Response<ProductResponse>

    @POST("/user/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @POST("/update-user-info")
    suspend fun updateUserExtras(
        @Header("Authorization") token: String,
        @Body request: UserExtraInfoRequest
    ): Response<GenericResponse>

    @POST("/user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UserProfileRequest
    ): Response<UserProfileResponse>

    @POST("/cart/update-quantity")
    suspend fun updateCartItemQuantity(
        @Header("Authorization") token: String,
        @Body request: UpdateCartQuantityRequest
    ): Response<GenericResponse>

    @POST("/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String
    ): Response<CheckoutResponse>

    @GET("/cart")
    suspend fun getCartItems(@Header("Authorization") token: String): Response<CartItemsResponse>

    @POST("/cart/add")
    suspend fun addCartItem(
        @Header("Authorization") token: String,
        @Body request: AddCartItemRequest
    ): Response<GenericResponse>
}
