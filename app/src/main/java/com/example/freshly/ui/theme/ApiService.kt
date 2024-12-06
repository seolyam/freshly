// ApiService.kt
package com.example.freshly.ui.theme
import com.example.freshly.models.ProductResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
// Import the request and response models
import com.example.freshly.ui.theme.RegisterRequest
import com.example.freshly.ui.theme.LoginRequest
import com.example.freshly.ui.theme.RegisterResponse
import com.example.freshly.ui.theme.LoginResponse
import com.example.freshly.ui.theme.UserProfileRequest // Import UserProfileRequest
import com.example.freshly.ui.theme.UserProfileResponse // Import UserProfileResponse

interface ApiService {
    @GET("/products")
    suspend fun getProducts(): Response<ProductResponse>

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @POST("/update-user-info")
    suspend fun updateUserExtras(
        @Header("Authorization") token: String,
        @Body request: UserExtraInfoRequest
    ): Response<GenericResponse>



    @POST("/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UserProfileRequest
    ): Response<UserProfileResponse>
}
