package com.example.freshly.ui.theme

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.freshly.utils.isTokenExpired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TokenManager(private val context: Context, private val apiService: ApiService) {

    private val sharedPreferencesName = "user_prefs"
    private val tokenKey = "jwt_token"
    private val refreshTokenKey = "refresh_token"

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        sharedPreferencesName,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Save both access and refresh tokens
    fun saveTokens(jwtToken: String, refreshToken: String) {
        prefs.edit()
            .putString(tokenKey, jwtToken)
            .putString(refreshTokenKey, refreshToken)
            .apply()
    }

    // Get access token
    fun getToken(): String? = prefs.getString(tokenKey, null)

    // Get refresh token
    fun getRefreshToken(): String? = prefs.getString(refreshTokenKey, null)

    // Clear both tokens
    fun clearTokens() {
        prefs.edit()
            .remove(tokenKey)
            .remove(refreshTokenKey)
            .apply()
    }

    // Check if the token is valid
    fun isTokenValid(): Boolean {
        val token = getToken()
        return token != null && !isTokenExpired(token)
    }

    // Get a valid Bearer token
    suspend fun getBearerToken(): String? {
        val token = getToken()
        return if (token != null && !isTokenExpired(token)) {
            "Bearer $token"
        } else {
            refreshToken()
        }
    }

    // Refresh the token using the refresh token
    private suspend fun refreshToken(): String? {
        val refreshToken = getRefreshToken() ?: return null
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<TokenResponse> = apiService.refreshToken(RefreshTokenRequest(refreshToken))
                if (response.isSuccessful) {
                    val newJwtToken = response.body()?.jwtToken
                    val newRefreshToken = response.body()?.refreshToken
                    if (newJwtToken != null && newRefreshToken != null) {
                        saveTokens(newJwtToken, newRefreshToken)
                        "Bearer $newJwtToken"
                    } else {
                        clearTokens()
                        null
                    }
                } else {
                    clearTokens()
                    null
                }
            } catch (e: Exception) {
                clearTokens()
                null
            }
        }
    }
}
