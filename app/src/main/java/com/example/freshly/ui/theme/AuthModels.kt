// AuthModels.kt
package com.example.freshly.ui.theme

// Request Models
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

// Response Models
data class RegisterResponse(
    val message: String?,
    val token: String?
)

data class LoginResponse(
    val token: String?,
    val error: String?,
    val message: String?
)
