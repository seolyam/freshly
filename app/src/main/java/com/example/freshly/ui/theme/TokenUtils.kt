package com.example.freshly.utils

import android.util.Base64
import org.json.JSONObject

fun isTokenExpired(token: String): Boolean {
    try {
        val parts = token.split(".")
        if (parts.size != 3) return true

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val json = JSONObject(payload)
        val exp = json.getLong("exp") * 1000 // Convert seconds to milliseconds
        return System.currentTimeMillis() > exp
    } catch (e: Exception) {
        return true
    }
}
