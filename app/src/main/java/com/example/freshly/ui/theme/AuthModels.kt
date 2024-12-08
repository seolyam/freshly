// AuthModels.kt
package com.example.freshly.ui.theme

// Request Models
data class RegisterRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)


// Response Models
data class RegisterResponse(
    val success: Boolean,
    val message: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String?, // JWT Token
    val refreshToken: String?, // Refresh Token
    val message: String? = null
)


