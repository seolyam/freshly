// UserProfileResponse.kt
package com.example.freshly.ui.theme

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val id: Int,

    @SerializedName("first_name") val firstName: String?,
    @SerializedName("middle_initial") val middleInitial: String?,
    @SerializedName("last_name") val lastName: String?,
    val email: String?,
    @SerializedName("birthdate") val birthdate: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("contact_number") val contactNumber: String?
)
