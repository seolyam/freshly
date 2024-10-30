// UserProfileRequest.kt
package com.example.freshly.ui.theme

data class UserProfileRequest(
    val firstName: String,
    val middleInitial: String,
    val lastName: String,
    val birthdate: String,
    val address: String,
    val password: String? = null
)
