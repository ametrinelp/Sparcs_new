package com.example.sparcs_new.data


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "theme_pref")

class ThemePreference(private val context: Context) {

    private val isDarkThemeKey = booleanPreferencesKey("dark_theme")

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }

    suspend fun saveTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDarkThemeKey] = isDarkTheme
        }
    }
}
