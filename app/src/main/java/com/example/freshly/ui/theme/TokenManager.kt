// TokenManager.kt
package com.example.freshly.ui.theme

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class TokenManager(context: Context) {
    private val sharedPreferencesName = "user_prefs"
    private val tokenKey = "jwt_token"

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        sharedPreferencesName,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        prefs.edit().putString(tokenKey, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(tokenKey, null)
    }

    fun clearToken() {
        prefs.edit().remove(tokenKey).apply()
    }
}
