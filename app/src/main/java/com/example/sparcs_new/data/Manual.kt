package com.example.sparcs_new.data

import android.content.Context

class PreferencesHelper(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunchDone() {
        prefs.edit().putBoolean("is_first_launch", false).apply()
    }
}
