package com.example.sparcs_new.data

import android.content.Context
import android.content.SharedPreferences

class DataTokenStore(
    context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)!!

    fun saveAccessToken(accessToken: String) {
        prefs.edit().putString("access_token", accessToken).apply()
    }

    fun saveRefreshToken(refreshToken: String){
        prefs.edit().putString("refresh_token", refreshToken).apply()
    }

    fun saveUserId(user_id: String){
        prefs.edit().putString("user_id", user_id).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    fun clearTokens() {
        prefs.edit().remove("access_token").apply()
        prefs.edit().remove("refresh_token").apply()
        prefs.edit().remove("user_id").apply()
    }
}