package com.example.sparcs_new.data

import android.content.Context
import android.content.SharedPreferences

class DataTokenStore(
    context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val USER_ID = "user_id"
    }

    fun saveAccessToken(accessToken: String) {
        prefs.edit().putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    fun saveRefreshToken(refreshToken: String){
        prefs.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply()
    }

    fun saveUserId(user_id: String){
        prefs.edit().putString(USER_ID, user_id).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clearTokens() {
        prefs.edit().remove(ACCESS_TOKEN_KEY).apply()
        prefs.edit().remove(REFRESH_TOKEN_KEY).apply()
        prefs.edit().remove(USER_ID).apply()
    }
}