// UserProfileRequest.kt
package com.example.freshly.ui.theme

import com.google.gson.annotations.SerializedName

data class UserProfileRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_initial") val middleInitial: String,
    @SerializedName("last_name") val lastName: String,
    val birthdate: String,
    val address: String,
    val password: String? = null
)
