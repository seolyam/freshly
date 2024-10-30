// ApiService.kt
package com.example.freshly.ui.theme

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @POST("profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UserProfileRequest
    ): Response<UserProfileResponse>
}
