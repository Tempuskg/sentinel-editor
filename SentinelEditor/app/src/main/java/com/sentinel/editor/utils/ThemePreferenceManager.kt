package com.sentinel.editor.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.themePreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "theme_preferences"
)

private val themeModeKey = stringPreferencesKey("theme_mode")

enum class ThemeMode(
    val storageValue: String,
    val title: String,
    val description: String
) {
    SYSTEM(
        storageValue = "system",
        title = "System",
        description = "Follow the device theme and dynamic colors when available."
    ),
    LIGHT(
        storageValue = "light",
        title = "Light",
        description = "Use the light editor palette."
    ),
    DARK(
        storageValue = "dark",
        title = "Dark",
        description = "Use the dark editor palette."
    );

    companion object {
        fun fromStorageValue(value: String?): ThemeMode {
            return entries.firstOrNull { it.storageValue == value } ?: SYSTEM
        }
    }
}

object ThemePreferenceManager {

    fun themeModeFlow(context: Context): Flow<ThemeMode> {
        return context.themePreferenceDataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences ->
                ThemeMode.fromStorageValue(preferences[themeModeKey])
            }
    }

    suspend fun setThemeMode(context: Context, themeMode: ThemeMode) {
        context.themePreferenceDataStore.edit { preferences ->
            preferences[themeModeKey] = themeMode.storageValue
        }
    }
}